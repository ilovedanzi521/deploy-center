package com.win.dfas.deploy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 调度中心服务提供者
 */
@Slf4j
@Service
public class ScheduleCenterServiceImpl implements ScheduleCenterService {
    private final static String TAG = "ScheduleCenterServiceImpl";

    @Autowired
    private TaskService taskService;

    /**
     * 启动部署
     * @param id - TaskID
     */
    @Override
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
    @Override
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
    @Override
    public int getDeployStatus(long id) {
        TaskPO task = taskService.getById(id);
        if(task != null) {
            return task.getStatus();
        }
        return 0;
    }

    /**
     * 根据ipAddr获取Device对象
     * @param ipAddr
     * @return
     */
    @Override
    public DevicePO getDevice(String ipAddr) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddr);
        if(context != null) {
            return context.getDevice();
        }
        return null;
    }

    /**
     * 连接设备,检测远程设备是否打通ssh免密
     * @param dev
     * @return
     */
    @Override
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

    /**
     * 新增设备后需要向调度中心添加，否则需要重启加载
     * @param dev
     */
    @Override
    public void addDevice(DevicePO dev) {
       if(dev != null) {
           Scheduler.get().addDevice(dev);
       }
    }

    /**
     * 删除设备后需要向调度中心删除，否则需要重启加载
     * @param dev
     */
    @Override
    public void delDevice(DevicePO dev) {
        if(dev != null) {
            Scheduler.get().delDevice(dev);
        }
    }

    /**
     * 当升级安装包后，需要调用该函数重新扫描应用和策略表，否则要重启
     */
    @Override
    public void appSourceScan() {
       Scheduler.get().getAppManager().scan();
    }

    /**
     * 查询远程机器的服务状态
     * @return
     *      0   -  服务未运行
     *      PID -  服务已运行(PID>0)
     *      <0  -  异常
     */
    @Override
    public int moduleStatus(String ipAddr, String moduleName) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddr);
        if(context != null) {
            return context.moduleStatus(moduleName);
        }
       return -1;
    }

    /**
     * 启动模块
     * @param ipAddr
     * @param moduleName
     */
    @Override
    public boolean moduleStart(String ipAddr, String moduleName) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddr);
        if(context != null) {
            return context.moduleStart(moduleName);
        }

        return false;
    }

    /**
     * 停止模块
     * @param ipAddr
     * @param moduleName
     */
    @Override
    public boolean moduleStop(String ipAddr, String moduleName) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddr);
        if(context != null) {
            return context.moduleStop(moduleName);
        }
        return false;
    }

    /**
     * 升级应用模块
     * @param zipFile
     * @param repoDir
     * @return
     */
    @Override
    public Boolean upgradAppModule(String zipFile, String repoDir) {
        Boolean isSuccess = ShellUtils.unZip(zipFile,repoDir);
        if (isSuccess){
            //当升级安装包后，需要调用该函数重新扫描应用和策略表，否则要重启
            this.appSourceScan();
        }
        return isSuccess;
    }
}
