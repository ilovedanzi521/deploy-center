package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 TaskController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:41
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<TaskPO> {
    @Autowired
    private TaskService taskService;

    @Override
    public IService<TaskPO> getBaseService() {
        return this.taskService;
    }

}

