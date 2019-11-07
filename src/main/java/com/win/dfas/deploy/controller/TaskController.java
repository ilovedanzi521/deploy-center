package com.win.dfas.deploy.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.dto.TaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 TaskController
 * @类描述 任务管理
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

    /**
     * 分页列表
     * @param reqVO
     * @return
     */
    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody BaseReqVO reqVO){
        PageVO<TaskDTO> pageVO = this.taskService.getPageInfo(reqVO);
        return WinResponseData.handleSuccess(pageVO);
    }
    @SysLog("添加任务")
    @PostMapping("/safeSave")
    public WinResponseData safeSave(@RequestBody TaskPO taskPO) {
        this.taskService.safeSave(taskPO);
        return WinResponseData.handleSuccess(taskPO);
    }
    @SysLog("部署任务")
    @GetMapping("/deploy")
    public WinResponseData deploy(@RequestParam Long id) {
        this.taskService.deploy(id);
        return WinResponseData.handleSuccess("启动异步部署...");
    }

    @SysLog("一键部署")
    @PostMapping("/oneKeyDeploy")
    public WinResponseData oneKeyDeploy(@RequestBody TaskPO taskPO) {
        this.taskService.oneKeyDeploy(taskPO);
        return WinResponseData.handleSuccess(taskPO);
    }

    @SysLog("卸载任务")
    @GetMapping("/undeploy")
    public WinResponseData undeploy(@RequestParam Long id) {
        this.taskService.unDeploy(id);
        return WinResponseData.handleSuccess("启动异步卸载...");
    }

    @GetMapping("/scan")
    public WinResponseData sacn() {
        taskService.appSourceScan();
        return WinResponseData.handleSuccess("扫描结束");
    }

    @GetMapping("/detail/{id}")
    public WinResponseData getDetailById(@PathVariable Long id){

        return WinResponseData.handleSuccess(taskService.getDetailById(id));
    }
    @GetMapping("/logInfo")
    public WinResponseData logInfo(@RequestParam String filePath){
        log.info("filePath="+filePath);
        try {
            filePath = URLDecoder.decode(filePath,"UTF-8");
            log.info("new filePath="+filePath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<String> lines = FileUtil.readUtf8Lines(filePath);
        return WinResponseData.handleSuccess(lines);
    }

    @GetMapping("/log/device")
    public WinResponseData deviceLog(@RequestParam String ipAddress,@RequestParam String strategyName){
        log.info("ipAddress="+ipAddress+",strategyName"+strategyName);
        try {
            strategyName = URLDecoder.decode(strategyName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("参数解析异常：strategyName="+strategyName,e);
            throw new BaseException("参数解析异常：strategyName="+strategyName);
        }
        List<String> lines = this.taskService.getDeployLogInfo(ipAddress,strategyName);

        return WinResponseData.handleSuccess(lines);
    }

}

