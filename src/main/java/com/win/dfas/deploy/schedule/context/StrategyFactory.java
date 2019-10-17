package com.win.dfas.deploy.schedule.context;

import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.context.impl.JavaMicroServiceStrategyImpl;
import com.win.dfas.deploy.util.SpringContextUtils;

/**
 * 工厂方法为解耦创建各种类型策略实现，以及Java模块各类型实现
 */
public class StrategyFactory {
    public final static int STRATEGY_TYPE_JAVA_MS = 1;
    public final static int STRATEGY_TYPE_JAVA_SDK = 2;

    /**
     * 创建策略实现类
     * @param type
     * @param remoteContext
     * @param strategy
     * @return
     */
    public static StrategyInterface createStrategyImpl(int type, ScheduleContext remoteContext, StrategyPO strategy) {
        switch (type) {
            case STRATEGY_TYPE_JAVA_MS:
                return new JavaMicroServiceStrategyImpl(remoteContext, strategy);
            case STRATEGY_TYPE_JAVA_SDK:
                return null;
            default:
                return null;
        }
    }

    public final static String JDK_MODULE_NAME = "Java-SDK";

    /**
     * 用来获取当前需要的ava sdk模块的方法
     * @param moduleName
     * @return
     */
    public static AppModulePO getJavaSdkModule(String moduleName) {
        AppManager app = SpringContextUtils.getBean("app_manager", AppManager.class);
        AppModulePO module = app.getModuleByName(moduleName);
        return module;
    }
}
