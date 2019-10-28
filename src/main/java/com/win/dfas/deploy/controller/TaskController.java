package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.dto.TaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/deploy")
    public WinResponseData deploy(@RequestParam Long id) {
        TaskPO taskPO=taskService.deploy(id);
        return WinResponseData.handleSuccess("开始异步部署...");
    }

    @GetMapping("/undeploy")
    public WinResponseData undeploy(@RequestParam Long id) {
        TaskPO taskPO=taskService.unDeploy(id);
        return WinResponseData.handleSuccess("开始异步卸载...");
    }

    @GetMapping("/scan")
    public WinResponseData sacn() {
        taskService.appSourceScan();
        return WinResponseData.handleSuccess("扫描结束");
    }
}

