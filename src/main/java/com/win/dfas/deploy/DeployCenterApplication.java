package com.win.dfas.deploy;

import com.win.dfas.deploy.schedule.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeployCenterApplication {
    private final static Logger logger = LoggerFactory.getLogger(DeployCenterApplication.class);

    // for hold global variable
    private static Scheduler gScheduler = null;

    public static void main(String[] args) {
        SpringApplication.run(DeployCenterApplication.class, args);
        Scheduler.get().init();
    }
}
