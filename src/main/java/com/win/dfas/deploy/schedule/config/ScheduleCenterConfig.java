package com.win.dfas.deploy.schedule.config;

import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Configuration
public class ScheduleCenterConfig {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleCenterConfig.class);

    @Bean("deploy_env_bean")
    @ConfigurationProperties(prefix = "deploy-center")
    public DeployEnvBean generateDeployEnv() {
        return new DeployEnvBean();
    }

    @Bean("app_manager")
    public AppManager getAppManager() {
        return new AppManager();
    }

    @Bean(name = "scheduler_task_executor")
    public static ThreadPoolTaskExecutor taskExecutor() {
        logger.info("Start taskExecutor ...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(20);
        // 设置队列容量
        executor.setQueueCapacity(30);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("Scheduler-");
        // 设置拒绝策略
        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;
    }

}
