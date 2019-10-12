package com.win.dfas.deploy.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.service.impl.TaskServiceImpl;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/deploy")
    public void deploy(@RequestParam long id) {
        QueryWrapper<TaskPO> wp = new QueryWrapper<TaskPO>();
        wp.eq("id",id);
        TaskPO task = taskService.getOne(wp);
        if(task != null) {
            Scheduler.get().depoly(task.getId());
        }
    }

    @GetMapping("/undeploy")
    public void undeploy(@RequestParam long id) {
        QueryWrapper<TaskPO> wp = new QueryWrapper<TaskPO>();
        wp.eq("id",id);
        TaskPO task = taskService.getOne(wp);
        if(task != null) {
            Scheduler.get().undepoly(task.getId());
        }
    }

    @GetMapping("/exec")
    public List<String> exec(@RequestParam String ipAddr, @RequestParam String cmd) {
       String[] params = {
               "/root/repo/exec.sh",
               ipAddr,
               cmd
       };

       log.info("exec.sh "+ipAddr+" "+cmd+", "+Arrays.toString(params));
       return RuntimeUtil.execForLines(params);
    }

    @GetMapping("/shell")
    public List<String> shell(@RequestParam String ipAddr, @RequestParam String cmd) {
        String[] params = {
                "ssh",
                "-p 22",
                ipAddr,
                "/root/repo/shell.sh",
                cmd
        };

        log.info(Arrays.toString(params));
        return RuntimeUtil.execForLines(params);
    }
}

