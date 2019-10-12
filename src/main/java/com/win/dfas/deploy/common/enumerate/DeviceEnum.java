package com.win.dfas.deploy.common.enumerate;

/**
 * @包名 com.win.dfas.deploy.common.enumerate
 * @类名 DeviceEnum
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/11 18:07
 */
public class DeviceEnum {

    public enum ConnectStatus{
        NOT("未连接", 0),
        FAILURE("连接失败", -1),
        NORMAL("连接正常", 1)
        ;

        private String name;
        private Integer value;

        ConnectStatus(String name,Integer value) {
            this.name = name;
            this.value = value;
        }

        public static String getName(Integer value) {
            for(ConnectStatus enumerate : ConnectStatus.values()) {
                if (enumerate.value.equals(value)) {
                    return enumerate.name;
                }
            }
            return null;
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
