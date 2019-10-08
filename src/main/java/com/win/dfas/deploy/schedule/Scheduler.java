package com.win.dfas.deploy.schedule;

import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

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
@Component
public class Scheduler {
    private final static Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private static Scheduler mInstance = null;

    private AppManager mAppManager = null;

    private DeployEnvBean mEnvConfig;

    private DeviceService mDeviceService;

    private ThreadPoolTaskExecutor mTaskExecutor = null;

    private Map<String, DevicePO>  mDeviceMap     = new Hashtable<String,DevicePO>();

    /**
     * 本地模块扫描锁
     */
    private AtomicBoolean mScanLock = new AtomicBoolean(false);
    /**
     * 初始化锁
     */
    private AtomicBoolean mInitLock = new AtomicBoolean(false);

    /**
     * 保存远程机器的SecduleContext对象map结构
     */
    private Map<String, ScheduleContext> mContextMaps;

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

            logger.info("taskExecutor instance="+mTaskExecutor);
            logger.info("deviceService: "+mDeviceService);
            logger.info("deployEnv: "+mEnvConfig);

            synchronized (mDeviceMap) {
                loadDevices(mDeviceMap);
            }

            mAppManager = new AppManager();
            mAppManager.init();

            logger.info(mEnvConfig == null ? "envConfig not init." : mEnvConfig.toString());
        }
        return mInitLock.get();
    }

    public AppManager getAppManager() {
       return mAppManager;
    }

    public ScheduleContext getSechduleContext(String host) {
       return mContextMaps.get(host);
    }

    /**
     * 从数据库读取设备信息到内存
     */
    private void loadDevices(Map<String,DevicePO> deviceMap) {
        List<DevicePO> list = mDeviceService.list(null);
        if(list == null || list.size() ==0) {
            return;
        }
        int total = list.size();
        for(int i=0; i<total; i++) {
            DevicePO device = list.get(i);
            deviceMap.put(device.getIpAddress(), device);
        }
    }

    public void addDevice(DevicePO device) {
        if(device != null && !StrUtil.isEmpty(device.getIpAddress())) {
            mDeviceMap.put(device.getIpAddress(), device);
        }
    }

    public void delDevice(DevicePO device) {
        if(device != null && !StrUtil.isEmpty(device.getIpAddress())) {
            mDeviceMap.remove(device.getIpAddress());
        }
    }

    public void depoly() {
        // 0. 创建新的异步任务

        // 1. 获取组和策略的信息

        // 2. 根据组得到host列表，轮询执行 install 命令

        // 3. 检查host列表的部署结果
    }

    public void undepoly() {

    }

    public boolean delModule() {
       return false;
    }

    public boolean delScript() {
       return false;
    }

    /**
     * 对比发布文件,扫描modules和scripts新增功能
     * @return
     */
    public boolean scan() {
        return false;
    }

}

