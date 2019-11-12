package com.win.dfas.deploy.common.enumerate;

import java.util.Map;

/**
 * @包名 com.win.dfas.deploy.common.enumerate
 * @类名 DeployEnum
 * @类描述 部署中心枚举类
 * @创建人 heshansen
 * @创建时间 2019/10/28 13:54
 */
public class DeployEnum {

    /**
     * 任务状态
     */
    public enum TaskStatus{
        DEPLOY_NONE("未部署", 0),
        DEPLOY_UNDERWAY("部署中", 1),
        DEPLOY_SUCCESS("部署成功", 2),
        DEPLOY_FAILURE("部署失败", 3),
        UNINSTALL_UNDERWAY("卸载中", 4),
        UNINSTALL_SUCCESS("卸载成功", 5),
        UNINSTALL_FAILURE("卸载失败", 6)
        ;

        private String name;
        private Integer value;

        TaskStatus(String name,Integer value) {
            this.name = name;
            this.value = value;
        }

        public static String getName(Integer value) {
            for(TaskStatus enumerate : TaskStatus.values()) {
                if (enumerate.value.equals(value)) {
                    return enumerate.name;
                }
            }
            return null;
        }
        public static Boolean isDeployd(Integer value){
            if (DEPLOY_UNDERWAY.value.equals(value)
                    || DEPLOY_SUCCESS.value.equals(value)
                    || UNINSTALL_UNDERWAY.value.equals(value)
                    || UNINSTALL_FAILURE.value.equals(value)){
                return true;
            }
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
}
