package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ScheduleCenterService mScheduleService;

    public TaskController() {
    }

    @Override
    public IService<TaskPO> getBaseService() {
        return this.taskService;
    }

    @GetMapping("/deploy")
    public void deploy(@RequestParam Long id) {
        mScheduleService.deploy(id);
    }

    @GetMapping("/undeploy")
    public void undeploy(@RequestParam Long id) {
        mScheduleService.undeploy(id);
    }

    @GetMapping("/scan")
    public void sacn(@RequestParam Long id) {
        mScheduleService.appSourceScan();
    }
}

