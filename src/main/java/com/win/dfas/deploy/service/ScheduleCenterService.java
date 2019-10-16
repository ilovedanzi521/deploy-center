package com.win.dfas.deploy.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度中心服务提供者
 */
@Slf4j
public class ScheduleCenterService {
    private final static String TAG = "ScheduleCenterService";

    @Autowired
    private TaskService taskService;

    @Autowired
    @Qualifier("app_manager")
    private AppManager appManager;

    public ScheduleCenterService() {

    }

    /**
     * 启动部署
     * @param id - TaskID
     */
    public void deploy(long id) {
        QueryWrapper<TaskPO> wp = new QueryWrapper<TaskPO>();
        wp.eq("id",id);
        TaskPO task = taskService.getOne(wp);
        if(task != null) {
            Scheduler.get().depoly(task.getId());
        }
    }

    /**
     * 卸载部署
     * @param id - TaskID
     */
    public void undeploy(long id) {
        QueryWrapper<TaskPO> wp = new QueryWrapper<TaskPO>();
        wp.eq("id",id);
        TaskPO task = taskService.getOne(wp);
        if(task != null) {
            Scheduler.get().undepoly(task.getId());
        }
    }

    /**
     * 获取部署状态
     * @param id
     * @return
     */
    public int getDeployStatus(long id) {
        TaskPO task = taskService.getById(id);
        if(task != null) {
            return task.getStatus();
        }
        return 0;
    }

    /**
     * 连接设备,检测远程设备是否打通ssh免密
     * @param dev
     * @return
     */
    public List<String> connectDevice(DevicePO dev) {
        if(dev == null || StrUtil.isEmpty(dev.getIpAddress())) {
            return null;
        }

        List<String> resultList = null;
        try {
            ScheduleContext context = Scheduler.get().getRemoteContext(dev);
            resultList = context.devConnect();
        } catch (Exception e) {
            log.error("connect device exception.", e);
        }
        return resultList;
    }
}
