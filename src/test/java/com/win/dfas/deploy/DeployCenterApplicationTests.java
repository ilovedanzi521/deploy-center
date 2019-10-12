package com.win.dfas.deploy;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.system.SystemUtil;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeployCenterApplicationTests {

    @Autowired
    TaskService mTaskService;

    @Test
    public void contextLoads() {
        String [] cmd={"cmd","/C","dir" };
        log.info("exec params: "+ Arrays.toString(cmd));

        List<String> list = RuntimeUtil.execForLines(cmd);
        for(int i=0; i<list.size(); i++) {
            log.info(list.get(i));
        }

        /*
        Scheduler.get().init();
        TaskPO task = mTaskService.getOne(null);
        long taskId=task.getId();
        Scheduler.get().depoly(taskId);
        */
    }

}
