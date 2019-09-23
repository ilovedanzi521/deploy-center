package com.win.dfas.deploy.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @包名 com.win.dfas.deploy.config
 * @类名 DataSourceConfig
 * @类描述 数据库配置
 *  * spring Boot 使用事务非常简单，首先使用注解 @EnableTransactionManagement 开启事务支持后，
 *  * 然后在访问数据库的Service方法上添加注解 @Transactional 便可。
 *  *
 *  * 关于事务管理器，不管是JPA还是JDBC等都实现自接口 PlatformTransactionManager
 *  * 如果你添加的是 spring-boot-starter-jdbc 依赖，框架会默认注入 DataSourceTransactionManager 实例。
 *  * 如果你添加的是 spring-boot-starter-data-jpa 依赖，框架会默认注入 JpaTransactionManager 实例。
 * @创建人 heshansen
 * @创建时间 2019/09/22 17:45
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return new DruidDataSource();
    }
}
