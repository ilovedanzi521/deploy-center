package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.service.impl.TaskServiceImpl;
import com.win.dfas.deploy.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 TaskController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:41
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<TaskPO> {
    private final static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpServletRequest mHttpRequest;

    @Autowired
    private Environment env;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @Override
    public IService<TaskPO> getBaseService() {
        return this.taskService;
    }

    @RequestMapping("/find")
    @ResponseBody
    public StrategyPO queryTask(HttpServletRequest request, HttpServletResponse response, @RequestParam int id) {
        logger.info("task service impl: "+taskServiceImpl + " param id="+id+" contextPath="+request.getContextPath()+" servletPath="+request.getServletPath());
        logger.info(mHttpRequest.getQueryString());
        logger.info(applicationContext.getApplicationName()+ " "+ applicationContext.getDisplayName()+" "+applicationContext.getId());
        StrategyPO strategy = taskServiceImpl.selectStrategyByTask(id);
        logger.info("query task: " + strategy);

        return strategy;
    }

    @RequestMapping("/device")
    @ResponseBody
    public List<DevicePO> queryDevice(HttpServletRequest request, HttpServletResponse response, @RequestParam int id) {
        List<DevicePO> list = taskServiceImpl.selectDeviceByTask(id);
        logger.info("device list: " + list.size());
        for(int i=0; i<list.size(); i++) {
            DevicePO dev = list.get(i);
            logger.info("device: " +  dev);
        }

        return list;
    }
}
