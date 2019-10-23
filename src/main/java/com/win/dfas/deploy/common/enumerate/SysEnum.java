package com.win.dfas.deploy.common.enumerate;

/**
 * @包名 com.win.dfas.deploy.common.enumerate
 * @类名 SysEnum
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/22 13:56
 */
public class SysEnum {

    public enum SpringActive {
        DEV("开发环境", "dev"),
        TEST("测试环境", "test"),
        PROD("生产环境", "prod");

        private String name;
        private String value;

        SpringActive(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static String getName(String value) {
            for (SpringActive enumerate : SpringActive.values()) {
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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
