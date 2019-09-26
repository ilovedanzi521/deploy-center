package com.win.dfas.deploy.schedule;

import com.win.dfas.deploy.schedule.bean.DeployEnvConfig;
import com.win.dfas.deploy.schedule.bean.TaskExecutorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RestController
public class Scheduler {
    private final static Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private static Scheduler mInstance = null;

    private AppManager mAppManager = null;

    @Autowired
    private DeployEnvConfig mEnvConfig;

    private TaskExecutorConfig mTaskExecutorBean;

    private ThreadPoolTaskExecutor mTaskExecutor = null;

    // 本地模块扫描锁
    private AtomicBoolean mScanLock = new AtomicBoolean(false);

    // 初始化锁
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

    public boolean init() {
        if(mInitLock.compareAndSet(false, true)) {
            mTaskExecutor = TaskExecutorConfig.taskExecutor();
            mTaskExecutor.initialize();

            mAppManager = new AppManager();
            mAppManager.init();

            logger.info(mEnvConfig == null ? "envConfig not init." : mEnvConfig.toString());
        }
        return mInitLock.get();
    }

    /**
     * 从数据库Device表中生成对应的ScheduleContext与Device对应信息
     */
    public void loadDevices() {

    }

    public void addDevice() {

    }

    public void delDevice() {

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

    private boolean scanModules() {
       return false;
    }

    private boolean scanScripts() {
        return false;
    }
}

