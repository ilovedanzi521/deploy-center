package com.win.dfas.deploy.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @包名 com.win.dfas.deploy.config
 * @类名 MybatisConfig
 * @类描述 mybatis-plus配置类
 * @创建人 heshansen
 * @创建时间 2019/09/22 17:59
 */
@Configuration
@MapperScan("com.win.dfas.deploy.modules.*.dao")
public class MybatisPlusConfig {

}
