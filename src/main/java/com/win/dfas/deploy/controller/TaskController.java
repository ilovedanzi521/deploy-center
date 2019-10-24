package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.PageVO;
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

    @Autowired
    private ScheduleCenterService mScheduleService;

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
        PageVO<TaskPO> pageVO = this.taskService.getPageInfo(reqVO);
        return WinResponseData.handleSuccess(pageVO);
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

