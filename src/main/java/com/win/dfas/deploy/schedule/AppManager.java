package com.win.dfas.deploy.schedule;

import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @包名 com.win.dfas.deploy.schedule.AppManager
 * @类名 AppManager
 * @类描述 模块(应用)和策略的发布版本描述信息管理
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
public class AppManager {
    private final static Logger logger = LoggerFactory.getLogger(AppManager.class);

    private AtomicBoolean mScanLock = new AtomicBoolean(false);

    private Map<String, AppModulePO> mModuleList = null;
    private Map<String, StrategyPO> mStrategyList = null;

    public boolean init() {
        return scan();
    }

    public boolean scan() {
        logger.info("Start scan ...");
        if(mScanLock.compareAndSet(false, true)) {
            boolean scan1 = scanModules();
            boolean scan2 = scanScripts();

            mScanLock.set(false);
            return scan1 && scan2;
        } else {
            return false;
        }
    }

    private boolean scanModules() {
        if(mModuleList == null) {
            mModuleList = new HashMap<String, AppModulePO>();
        }
        return false;
    }

    private boolean scanScripts() {
        if(mStrategyList == null) {
            mStrategyList = new HashMap<String, StrategyPO>();
        }
        return false;
    }

    /**
     * 检查mAppModulesList是否和数据库版本匹配，
     * 不匹配则更新数据库
     * @return
     *      true - 有更新
     *      false - 没有更新版本
     */
    private boolean checkAndUpdate() {
        return false;
    }
}

