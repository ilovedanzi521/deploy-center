package com.win.dfas.deploy;

import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class DeployCenterApplication implements ApplicationListener<ContextRefreshedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(DeployCenterApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DeployCenterApplication.class);
        app.run(args);
        Scheduler.get().init();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SpringContextUtils.applicationContext = contextRefreshedEvent.getApplicationContext();
    }
}
