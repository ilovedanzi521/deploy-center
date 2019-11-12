package com.win.dfas.deploy.schedule.context;


import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.common.enumerate.DeployEnum;
import com.win.dfas.deploy.po.*;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 部署任务
 */
@Slf4j
public class DeployTask implements Runnable{
    private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TASK = "[TASK]-";
    private static String LOG_SUFFIX=".log";
    /**
     * 部署和卸载命令字 1-部署；2-卸载
     */
    public final static int CMD_DEPLOY = 1;
    public final static int CMD_UNDEPLOY = 2;

    /*消息类型： underway（进行中)、success（成功）、failure（失败）*/
    public final static String MSG_UNDERWAY = "underway";
    public final static String MSG_SUCCESS = "success";
    public final static String MSG_FAILURE = "failure";

    private int mCmd;
    private long mTaskId;
    private TaskPO mTaskPO;
    private String logDir;

    @Autowired
    private TaskService mTaskService;
    @Autowired
    private DeviceModuleService mDeviceModuleService;

    @Autowired
    private ThreadPoolTaskExecutor mTaskExecutor;
    private DeployEnvBean mEnvConfig;

    public DeployTask(int cmd, TaskPO task) {
        this.mCmd = cmd;
        this.mTaskId = task.getId();
        this.mTaskPO = task;
        this.mTaskService = SpringContextUtils.getBean(TaskService.class);
        this.mDeviceModuleService = SpringContextUtils.getBean(DeviceModuleService.class);
        this.mEnvConfig = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
        this.mTaskExecutor = SpringContextUtils.getBean("scheduler_task_executor", ThreadPoolTaskExecutor.class);
        initLogPath();
    }

    private void initLogPath() {
        String logsDir = this.mEnvConfig.getLogsDir();
        if (!logsDir.endsWith(File.separator)){
            logsDir+=File.separator;
        }
        this.logDir = logsDir+ this.mTaskId + File.separator;
    }


    @Override
    public void run() {
        String logHeader =TASK + (mCmd==CMD_DEPLOY ? "deploy":"unDeploy")+"==> ";
        writeTaskLog(logHeader +"Start id="+mTaskId +" deployStatus="+mTaskPO.getStatus(), FileWriterTask.WRITE);

        // 1. 从taskId中查询出策略
        final StrategyPO strategy = mTaskService.getStrategyByTask(mTaskPO);
        if(strategy == null) {
            writeTaskLog(logHeader +"strategy is not exist!", null);
            updateStatus(mCmd,MSG_FAILURE);
            return;
        }else {
            writeTaskLog(logHeader +"strategy is "+strategy.getName(), null);
        }
        // 2. 从taskId中查询出设备列表
        List<DevicePO> devList = mTaskService.getDevicesByTask(mTaskPO);
        if(devList == null || devList.size() == 0) {
            writeTaskLog(logHeader +"not find any devices!", null);
            updateStatus(mCmd,MSG_FAILURE);
            return;
        }else {
            writeTaskLog(logHeader +"Find total "+devList.size() +" devices todo.", null);
        }

        // 获取device对应的ScheduleContext对象
        List<ScheduleContext> remoteContextList = new ArrayList<ScheduleContext>();
        int devTotal = devList.size();
        String msg= logHeader +"Began asyn remote devices: ";
        for(int i=0; i<devTotal; i++) {
            DevicePO device = devList.get(i);
            ScheduleContext remoteContext = Scheduler.get().getRemoteContext(device);
            remoteContextList.add(remoteContext);
            msg+="["+device.toSimpleString()+"];";
        }
        if(remoteContextList.size() == 0) {
            writeTaskLog(logHeader +"no valid remote devices", null);
            updateStatus(mCmd,MSG_FAILURE);
            return;
        }else {
            writeTaskLog(msg, null);
        }

        // 3. 批量执行发布命令
        int subTaskSize = remoteContextList.size();
        final CountDownLatch taskCount = new CountDownLatch(subTaskSize);
        for(int i=0; i<subTaskSize; i++) {
            ScheduleContext remoteContext = remoteContextList.get(i);
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
            // 5. 设置部署状态DEPLOY_DONE or DEPLOY_ERROR
            updateStatus(mCmd,MSG_SUCCESS);
            writeTaskLog(logHeader +"All Finished!", null);
        } catch (InterruptedException e) {
            writeTaskLog(logHeader +"Interrupted Failure, "+e.toString()+", "+e.getMessage(), null);
            updateStatus(mCmd,MSG_FAILURE);
            return;
        }

    }

    /**
     * 该任务是具体执行远程单台机器的部署任务
     */
    private class RemoteDeployTask implements Runnable {
        private static final String TASK = "RemoteDeployTask";
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
            try {
                DevicePO device = remoteContext.getDevice();
                String logHeader = TASK+ "["+ device.toSimpleString()+"]==> ";
                writeTaskLog(logHeader +"start init environment... ",null);
                // 1. 初始化远程节点的环境
                boolean isInit = remoteContext.initRemoteDevice();
                if (!isInit){
                    writeTaskLog(logHeader +"init error!",null);
                    updateStatus(CMD_DEPLOY,MSG_FAILURE);
                    return;
                }else {
                    writeTaskLog(logHeader +"init finished!",null);
                }

                // 2. 创建一个策略实现类，类型为java微服务,
                //    通过list_modules获取策略需要的模块,并设置到strategyImpl中
                StrategyInterface strategyImpl = StrategyFactory.createStrategyImpl(StrategyFactory.STRATEGY_TYPE_JAVA_MS, remoteContext, strategy);
                List<AppModulePO> moduleObjList = strategyImpl.list_modules();
                strategyImpl.setListModules(moduleObjList);
                writeTaskLog(logHeader +"start deploy... ",null);
                // 3. 执行deploy命令
                boolean result = strategyImpl.deploy();
                // 4. 执行完成后，添加和更新服务到设备对应的表中
                if (result) {
                    writeTaskLog(logHeader +"deploy success!",null);
                    int moduleTotal = moduleObjList.size();
                    writeTaskLog(logHeader + "Batch update deviceRefModules, size is " + moduleObjList.size(),null);
                    List<DeviceModuleRefPO> devModList = new ArrayList<>();
                    for (AppModulePO appModulePO: moduleObjList) {
                        DeviceModuleRefPO refObj = new DeviceModuleRefPO();
                        refObj.setDeviceId(device.getId());
                        refObj.setModuleId(appModulePO.getId());
                        devModList.add(refObj);
                    }
                    if(devModList.size() >0 ) {
                        mDeviceModuleService.updateBatch(devModList);
                    }
                    writeTaskLog(logHeader +"Batch update finished!",null);
                    updateStatus(CMD_DEPLOY,MSG_SUCCESS);
                }else {
                    writeTaskLog(logHeader +"deploy failure!",null);
                }
            } catch (Exception e) {
                writeTaskLog(TASK +"deploy error!"+e.getMessage(),null);
                updateStatus(CMD_DEPLOY,MSG_FAILURE);
            } finally {
                taskCount.countDown();
            }
        }
    }


    /**
     * 该任务是具体执行远程单台机器的部署任务
     */
    private class RemoteUndeployTask implements Runnable {
        private static final String TASK = "RemoteUndeployTask";
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
            try {
                DevicePO device = remoteContext.getDevice();
                String logHeader = TASK+ "[" + device.getName() + "," + device.getIpAddress() + "]==> ";
                writeTaskLog(logHeader +"start unDeploy... ",null);
                // 1. 创建一个策略实现类，类型为java微服务
                StrategyInterface strategyImpl = StrategyFactory.createStrategyImpl(StrategyFactory.STRATEGY_TYPE_JAVA_MS, remoteContext, strategy);

                // 2. 通过list_modules获取策略需要的模块,并设置到strategyImpl中
                List<AppModulePO> moduleObjList = strategyImpl.list_modules();
                strategyImpl.setListModules(moduleObjList);

                // 3. 执行deploy命令
                boolean result = strategyImpl.undeploy();

                // 4. 执行完成后，删除服务和设备对应关系
                if (result) {
                    writeTaskLog(logHeader +"unDeploy success. ",null);
                    int moduleTotal = moduleObjList.size();
                    List<DeviceModuleRefPO> devModList = new ArrayList<>();
                    String appMsg = logHeader + "Remove relative appModule size is " + moduleObjList.size()+",list=";
                    for (int i = 0; i < moduleTotal; i++) {
                        AppModulePO module = moduleObjList.get(i);
                        DeviceModuleRefPO refObj = new DeviceModuleRefPO();
                        refObj.setDeviceId(device.getId());
                        refObj.setModuleId(module.getId());
                        devModList.add(refObj);
                        appMsg+="["+ module.toFilePathString() +"];";
                    }
                    writeTaskLog(appMsg,null);
                    if(devModList.size()> 0) {
                        mDeviceModuleService.removeBatch(devModList);
                    }
                    writeTaskLog(logHeader +"unDeploy finished success!",null);
                    updateStatus(CMD_UNDEPLOY,MSG_SUCCESS);
                }else {
                    writeTaskLog(logHeader +"unDeploy failure!",null);
                }
                log.info(logHeader + " [" + device.getName() + "," + device.getIpAddress() + "] undeploy end. result=" + result);
            } catch (Exception e) {
                writeTaskLog(TASK +"unDeploy error!"+e.toString()+","+e.getMessage(),null);
                updateStatus(CMD_UNDEPLOY,MSG_FAILURE);
            } finally {
                taskCount.countDown();
            }
        }
    }

    /**
     * 更新任务状态
     * @param mCmd
     * @param msgType
     */
    private void updateStatus(int mCmd, String msgType) {
        if (MSG_UNDERWAY.equals(msgType)){
            if (mCmd==CMD_DEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.DEPLOY_UNDERWAY.getValue());
            }else if (mCmd==CMD_UNDEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.UNINSTALL_UNDERWAY.getValue());
            }
        } else if (MSG_SUCCESS.equals(msgType)) {
            if (mCmd==CMD_DEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.DEPLOY_SUCCESS.getValue());
            }else if (mCmd==CMD_UNDEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.UNINSTALL_SUCCESS.getValue());
            }
        } else if (MSG_FAILURE.equals(msgType)) {
            if (mCmd==CMD_DEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.DEPLOY_FAILURE.getValue());
            }else if (mCmd==CMD_UNDEPLOY){
                mTaskPO.setStatus(DeployEnum.TaskStatus.UNINSTALL_FAILURE.getValue());
            }
        }
        mTaskService.updateById(mTaskPO);
    }

    /**
     * 异步写入日志消息
     * @param line 日志消息
     * @param operate WRITE-覆盖；APPEND-后面增加一行
     */
    private void writeTaskLog(String line, String operate) {
        log.info(line);
        String logFile = this.logDir+mTaskPO.getId()+ LOG_SUFFIX;

        if (StrUtil.isEmpty(operate)){
            //默认在日志文件后面累加
            operate = FileWriterTask.APPEND;
        }else if (FileWriterTask.WRITE.equals(operate)){
            //如果是覆盖，更新任务日志文件路径
            mTaskPO.setLogPath(logFile);
        }
        List<String> lines=new ArrayList<>(1);
        lines.add(df.format(new Date())+" "+line);
//        异步写入日志消息
        FileWriterTask logTask = new FileWriterTask(logFile,lines, operate);
        mTaskExecutor.submit(logTask);
    }

    public static void main(String[] args){
        String logDir = "X:\\test\\13\\log.txt";
        List<String> lines=new ArrayList<>(1);
        lines.add(logDir);
        FileWriterTask task = new FileWriterTask(logDir,lines,FileWriterTask.APPEND);
        task.run();
    }

}

