package com.win.dfas.deploy.common.annotation;

import java.lang.annotation.*;

/**
 * @包名 com.win.dfas.deploy.common.annotation
 * @类名 SysLogPO
 * @类描述 系统日志注解
 * @创建人 heshansen
 * @创建时间 2019/09/23 16:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
