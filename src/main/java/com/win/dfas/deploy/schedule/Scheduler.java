package com.win.dfas.deploy.schedule;

import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.common.enumerate.DeployEnum;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.schedule.context.DeployTask;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @包名 com.win.dfas.deploy.schedule.Schduler
 * @类名 Scheduler
 * @类描述 调度中心管理者
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
@Slf4j
public class Scheduler {
    private final static String TAG = "Scheduler";

    private static Scheduler mInstance = null;

    private AppManager mAppManager = null;

    private DeployEnvBean mEnvConfig;

    private DeviceService mDeviceService;

    private TaskService mTaskService;

    private ThreadPoolTaskExecutor mTaskExecutor = null;
    /**
     * 保存远程机器的SecduleContext对象map结构
     */
    private Map<String, ScheduleContext>  mContextMap = new Hashtable<String,ScheduleContext>();

    /**
     * 初始化锁
     */
    private AtomicBoolean mInitLock = new AtomicBoolean(false);

    public static Scheduler get() {
        if(mInstance == null) {
            synchronized (Scheduler.class) {
                if(mInstance == null) {
                    mInstance = new Scheduler();
                }
            }
        }
        return mInstance;
    }

    private Scheduler() {
    }

    public boolean init() {
        if(mInitLock.compareAndSet(false, true)) {
            mEnvConfig = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
            mTaskExecutor = SpringContextUtils.getBean("scheduler_task_executor", ThreadPoolTaskExecutor.class);
            mDeviceService = SpringContextUtils.getBean(DeviceService.class);
            mTaskService = SpringContextUtils.getBean(TaskService.class);
            mAppManager = SpringContextUtils.getBean("app_manager", AppManager.class);

            log.info("taskExecutor instance="+mTaskExecutor);
            log.info("deviceService: "+mDeviceService);
            log.info("taskService: "+mTaskService);
            log.info("deployEnv: "+mEnvConfig.toString());
            log.info("appManager: "+mAppManager);

            loadDevices();
            mAppManager.init();
        }
        return mInitLock.get();
    }

    public AppManager getAppManager() {
       return mAppManager;
    }

    public DeployEnvBean getDeployEnvConfig(){
        return this.mEnvConfig;
    }

    public ScheduleContext getSechduleContext(String host) {
       return mContextMap.get(host);
    }

    /**
     * 从数据库读取设备信息到内存
     */
    private void loadDevices() {
        List<DevicePO> list = mDeviceService.list(null);
        if(list == null || list.size() ==0) {
            return;
        }
        log.info("loadDevices ===> ");
        int total = list.size();
        for(int i=0; i<total; i++) {
            DevicePO device = list.get(i);
            log.info(device.toSimpleString());
            addDevice(device);
        }
    }

    private DevicePO findDeviceByIp(String ipAddr) {
        ScheduleContext remoteContext = mContextMap.get(ipAddr);
        if(remoteContext != null) {
            return remoteContext.getDevice();
        }
        return null;
    }

    /**
     * 添加设备到调度中心，保存在内存
     * @param device
     */
    public void addDevice(DevicePO device) {
        if(device == null) {
            return;
        }

        ScheduleContext context = mContextMap.get(device.getIpAddress());
        if(context != null) {
            context.setDevice(device);
        } else {
            context = new ScheduleContext(mAppManager, mEnvConfig, device);
        }
        mContextMap.put(device.getIpAddress(), context);
    }

    /**
     * @param device     * 删除设备从调度中心,从内存中删除
     */
    public boolean delDevice(DevicePO device) {
        if(device != null && !StrUtil.isEmpty(device.getIpAddress())) {
            return mContextMap.remove(device.getIpAddress()) != null;
        }
        return false;
    }

    public ScheduleContext getRemoteContext(String ipAddr) {
        return mContextMap.get(ipAddr);
    }

    public ScheduleContext getRemoteContext(DevicePO device) {
        if(device == null || StrUtil.isEmpty(device.getIpAddress())) {
            return null;
        }
        return mContextMap.get(device.getIpAddress());
    }

    public void depoly(long taskId) {
        // 1. 获取组和策略的信息
        TaskPO taskObj = mTaskService.getById(taskId);
        log.info(TAG, "deploy taskId="+taskId+" status="+taskObj.getStatus());

        // 2. 更新任务状态为deploy_doing
        taskObj.setStatus(DeployEnum.TaskStatus.DEPLOY_UNDERWAY.getValue());
        mTaskService.updateById(taskObj);

        // 3. 创建新的异步任务
        DeployTask task = new DeployTask(DeployTask.CMD_DEPLOY, taskObj);
        mTaskExecutor.execute(task);
    }

    public void undepoly(long taskId) {
        // 1. 获取组和策略的信息
        TaskPO taskObj = mTaskService.getById(taskId);
        log.info(TAG, "undeploy taskId="+taskId+" status="+taskObj.getStatus());

        // 2. 更新任务状态为deploy_doing
        taskObj.setStatus(DeployEnum.TaskStatus.UNINSTALL_UNDERWAY.getValue());
        mTaskService.updateById(taskObj);

        // 3. 创建新的异步任务
        DeployTask task = new DeployTask(DeployTask.CMD_UNDEPLOY, taskObj);
        mTaskExecutor.execute(task);
    }

    public boolean delModule() {
        // 1. 删除该模块对应的设备服务列表

        // 2. 模块表设置删除标志 or 删除该模块脚本文件
        return false;
    }

    public boolean delScript() {
        // 1. 删除任务中对应的策略

        // 2. 删除该策略脚本文件
       return false;
    }

    /**
     * 更新完应用后执行,用来重新扫描应用和策略
     * @return
     */
    public boolean upgradeAfter() {
        return mAppManager.scan()==0;
    }
}

