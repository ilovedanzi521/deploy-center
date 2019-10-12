package com.win.dfas.deploy.schedule.context;

import com.alibaba.druid.proxy.jdbc.WrapperProxy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.win.dfas.deploy.dao.DeviceModuleDao;
import com.win.dfas.deploy.po.*;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.impl.TaskServiceImpl;
import com.win.dfas.deploy.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.sql.Wrapper;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 部署任务
 */
public class DeployTask implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(DeployTask.class);

    /**
     * 部署和卸载命令字
     */
    public final static int CMD_DEPLOY = 1;
    public final static int CMD_UNDEPLOY = 2;

    /**
     * 部署和卸载状态
     */
    public final static int TASK_CREATED = 0;
    public final static int DEPLOY_DOING  = 1;
    public final static int DEPLOY_DONE   = 2;
    public final static int DEPLOY_ERROR  = 3;
    public final static int UNDEPLOY_DOING  = 4;
    public final static int UNDEPLOY_DONE   = 5;
    public final static int UNDEPLOY_ERROR  = 6;

    private int mCmd;
    private long mTaskId;
    private TaskPO mTaskPO;

    private TaskServiceImpl mTaskImpl;
    private DeviceModuleService mDeviceModuleService;

    @Autowired
    private ThreadPoolTaskExecutor mTaskExecutor;

    public DeployTask(int cmd, TaskPO task) {
        mCmd = cmd;
        mTaskId = task.getId();
        mTaskPO = task;
        mTaskImpl = SpringContextUtils.getBean(TaskServiceImpl.class);
        mDeviceModuleService = SpringContextUtils.getBean(DeviceModuleService.class);
        mTaskExecutor = SpringContextUtils.getBean("scheduler_task_executor", ThreadPoolTaskExecutor.class);
    }

    private void saveStatus(int status) {
        mTaskPO.setStatus(status);
        mTaskImpl.updateById(mTaskPO);
    }

   @Override
    public void run() {
        int status= mCmd==CMD_DEPLOY ? DEPLOY_DOING : UNDEPLOY_DOING;
        logger.info("DeployTask","Start id="+mTaskId);

        // 1. 从taskId中查询出策略
       final StrategyPO strategy = mTaskImpl.selectStrategyByTask(mTaskId);
        if(strategy == null) {
           status= mCmd==CMD_DEPLOY ? DEPLOY_ERROR : UNDEPLOY_ERROR;
           saveStatus(status);
           return;
        }
        logger.info("DeployTask", "find strategy="+strategy.toString() + " deployStatus="+status);

        // 2. 从taskId中查询出设备列表
        List<DevicePO> devList = mTaskImpl.selectDeviceByTask(mTaskId);
        if(devList == null || devList.size() == 0) {
            status= mCmd==CMD_DEPLOY ? DEPLOY_ERROR : UNDEPLOY_ERROR;
            saveStatus(status);
            return;
        }
        logger.info("DeployTask", "find devices total="+devList.size()+" deployStatus="+status);
        logger.info("DeployTask", "devices: "+ devList.toString());

        // 获取device对应的ScheduleContext对象
        List<ScheduleContext> remoteContextList = new ArrayList<ScheduleContext>();
        int devTotal = devList.size();
        for(int i=0; i<devTotal; i++) {
            DevicePO device = devList.get(i);
            ScheduleContext remoteContext = Scheduler.get().getRemoteContext(device);
            remoteContextList.add(remoteContext);
        }
        if(remoteContextList.size() == 0) {
            status= mCmd==CMD_DEPLOY ? DEPLOY_ERROR : UNDEPLOY_ERROR;
            saveStatus(status);
            return;
        }

        // 3. 批量执行发布命令
        final CountDownLatch taskCount = new CountDownLatch(remoteContextList.size());
        int taskTotal = remoteContextList.size();
        for(int i=0; i<taskTotal; i++) {
            final ScheduleContext remoteContext = remoteContextList.get(i);
            Runnable singleTask = null;
            if(mCmd == DeployTask.CMD_DEPLOY) {
                singleTask = new RemoteDeployTask(strategy, remoteContext, taskCount);
            } else if(mCmd == DeployTask.CMD_UNDEPLOY) {
                singleTask = new RemoteUndeployTask(strategy, remoteContext, taskCount);
            }
            mTaskExecutor.submit(singleTask);
        }

        // 4. 等待远程节点全部执行完成。
        try{
            taskCount.await();
            status= mCmd==CMD_DEPLOY ? DEPLOY_DONE : UNDEPLOY_DONE;
        } catch (InterruptedException e) {
            status= mCmd==CMD_DEPLOY ? DEPLOY_ERROR : UNDEPLOY_ERROR;
            logger.error("DeployTask", "taskCount await interrupted.", e);
        }

       // 5. 设置部署状态DEPLOY_DONE or DEPLOY_ERROR
       saveStatus(status);
    }

    /**
     * 该任务是具体执行远程单台机器的部署任务
     */
    private class RemoteDeployTask implements Runnable {
        private final static String TAG = "RemoteDeployTask ";
        private ScheduleContext remoteContext;
        private StrategyPO strategy;
        private CountDownLatch taskCount;

        public RemoteDeployTask(StrategyPO strategy,ScheduleContext context, CountDownLatch countDown) {
            this.strategy = strategy;
            this.remoteContext = context;
            this.taskCount = countDown;
        }

        @Override
        public void run() {
            DevicePO device = remoteContext.getDevice();
            logger.info(TAG, "["+device.getName()+","+device.getIpAddress()+"] deploy start.");

            // 1. 创建一个策略实现类，类型为java微服务
            StrategyInterface strategyImpl = StrategyFactory.createStrategyImpl(StrategyFactory.STRATEGY_TYPE_JAVA_MS, remoteContext, strategy);

            // 2. 通过list_modules获取策略需要的模块,并设置到strategyImpl中
            List<AppModulePO> moduleObjList = strategyImpl.list_modules();
            strategyImpl.setListModules(moduleObjList);

            // 3. 执行deploy命令
            boolean result = false;
            try {
                result = strategyImpl.deploy();
            } catch (Exception e) {
                logger.info(TAG, "deploy exception. ", e);
            }

            // 4. 执行完成后，添加和更新服务到设备对应的表中
            if(result) {
                int moduleTotal = moduleObjList.size();
                logger.info(TAG, "saveOrUpdate deviceModule size: "+moduleTotal);

                for(int i=0; i<moduleTotal; i++) {
                    AppModulePO module = moduleObjList.get(i);
                    DeviceModuleRefPO refObj = new DeviceModuleRefPO();
                    refObj.setDeviceId(device.getId());
                    refObj.setModuleId(module.getId());
                    mDeviceModuleService.saveOrUpdate(refObj);
                    logger.info(TAG, "update deviceModuleRefPO "+i+": " + refObj.toString());
                }
            }
            logger.info(TAG, "["+device.getName()+","+device.getIpAddress()+"] deploy end. result="+result);
            taskCount.countDown();
        }
    };

    /**
     * 该任务是具体执行远程单台机器的部署任务
     */
    private class RemoteUndeployTask implements Runnable {
        private final static String TAG = "RemoteUndeployTask ";
        private ScheduleContext remoteContext;
        private StrategyPO strategy;
        private CountDownLatch taskCount;

        public RemoteUndeployTask(StrategyPO strategy,ScheduleContext context, CountDownLatch countDown) {
            this.strategy = strategy;
            this.remoteContext = context;
            this.taskCount = countDown;
        }

        @Override
        public void run() {
            DevicePO device = remoteContext.getDevice();
            logger.info(TAG, "["+device.getName()+","+device.getIpAddress()+"] undeploy start.");

            // 1. 创建一个策略实现类，类型为java微服务
            StrategyInterface strategyImpl = StrategyFactory.createStrategyImpl(StrategyFactory.STRATEGY_TYPE_JAVA_MS, remoteContext, strategy);

            // 2. 通过list_modules获取策略需要的模块,并设置到strategyImpl中
            List<AppModulePO> moduleObjList = strategyImpl.list_modules();
            strategyImpl.setListModules(moduleObjList);

            // 3. 执行deploy命令
            boolean result = false;
            try {
                result = strategyImpl.undeploy();
            } catch (Exception e) {
                logger.info(TAG, "undeploy exception.", e);
            }

            // 4. 执行完成后，删除服务和设备对应关系
            if(result) {
                int moduleTotal = moduleObjList.size();
                logger.info(TAG, "saveOrUpdate deviceModule size: "+moduleTotal);

                for(int i=0; i<moduleTotal; i++) {
                    AppModulePO module = moduleObjList.get(i);
                    DeviceModuleRefPO refObj = new DeviceModuleRefPO();
                    refObj.setDeviceId(device.getId());
                    refObj.setModuleId(module.getId());

                    UpdateWrapper<DeviceModuleRefPO> wrapper = new UpdateWrapper<>();
                    wrapper.eq("deviceId", refObj.getDeviceId());
                    wrapper.eq("moduleId", refObj.getModuleId());
                    mDeviceModuleService.remove(wrapper);
                    logger.info(TAG, "delete deviceModuleRefPO "+i+": " + refObj.toString());
                }
            }
            logger.info(TAG, "["+device.getName()+","+device.getIpAddress()+"] undeploy end. result="+result);
            taskCount.countDown();
        }
    };
}
