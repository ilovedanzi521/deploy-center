package com.win.dfas.deploy.common.enumerate;

/**
 * @包名 com.win.dfas.deploy.common.enumerate
 * @类名 LogEnum
 * @类描述 日志枚举接口
 * @创建人 heshansen
 * @创建时间 2019/09/27 10:44
 */
public class LogEnum {

    /**
     * 控制器枚举类
     */
    public enum Controller{
        AppModuleController("AppModuleController", "应用模块"),
        DeviceController("DeviceController", "设备"),
        GroupController("GroupController", "设备组"),
        StrategyController("StrategyController", "策略"),
        TaskController("TaskController", "任务"),
        SysUserController("SysUserController", "用户信息"),
        ;

        private String name;
        private String value;

        Controller(String name,String value) {
            this.name = name;
            this.value = value;
        }

        public static String getValue(String name) {
            for(Controller enumerate : Controller.values()) {
                if (enumerate.name.equals(name)) {
                    return enumerate.value;
                }
            }
            return null;
        }
    }

    /**
     * 操作枚举类
     */
    public enum Operate{
        save("save", "新增"),
        updateById("updateById", "修改"),
        saveOrUpdate("saveOrUpdate", "修改"),
        removeById("removeById", "删除"),
        removeByIds("removeByIds", "批量删除"),
        ;

        private String name;
        private String value;

        Operate(String name,String value) {
            this.name = name;
            this.value = value;
        }

        public static String getValue(String name) {
            for(Operate enumerate : Operate.values()) {
                if (enumerate.name.equals(name)) {
                    return enumerate.value;
                }
            }
            return null;
        }
    }


    /**
     * 获取操作描述
     * @param methodName
     * @param className
     * @return
     */
    public static String getOperationDesc(String methodName, String className) {
        return Operate.getValue(methodName) + Controller.getValue(className);
    }

}
