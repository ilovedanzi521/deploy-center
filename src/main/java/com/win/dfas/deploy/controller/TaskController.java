package com.win.dfas.deploy.controller;

import com.win.dfas.deploy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 TaskController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:48
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;


}