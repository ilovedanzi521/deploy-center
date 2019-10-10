package com.win.dfas.deploy.schedule.context;

import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.context.impl.JavaMicroServiceStrategyImpl;

public class StrategyFactory {
    public final static int STRATEGY_TYPE_JAVA_MS = 1;
    public final static int STRATEGY_TYPE_JAVA_SDK = 2;

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
}
