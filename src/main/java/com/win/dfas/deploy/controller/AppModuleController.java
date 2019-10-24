package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.vo.response.AppModuleTreeVO;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 AppModuleController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:50
 */
@RestController
@RequestMapping("/app/module")
public class AppModuleController extends BaseController<AppModulePO> {
    @Autowired(required = true)
    private AppModuleService appModuleService;

    @Autowired(required = true)
    private ScheduleCenterService mScheduleService;

    @Override
    public IService<AppModulePO> getBaseService() {
        return this.appModuleService;
    }

    /**
     * 分页列表
     * @param reqVO
     * @return
     */
    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody BaseReqVO reqVO){
        PageVO<AppModuleTreeVO> pageVO = this.appModuleService.getAppModuleTreePageInfo(reqVO);
        return WinResponseData.handleSuccess(pageVO);
    }

    @SysLog("上传应用模块")
    @PostMapping("/upload")
    public WinResponseData upload(@RequestParam("file") MultipartFile file){
        this.appModuleService.uploadFile(file);
        return WinResponseData.handleSuccess("上传成功");
    }

    @GetMapping("/list")
    @ResponseBody
    public List<AppModulePO> list() {
       return appModuleService.list();
    }

    @GetMapping("/moduleStart")
    public WinResponseData moduleStart(@RequestParam String ipAddr, String moduleName) {
       boolean isStart = mScheduleService.moduleStart(ipAddr, moduleName);
       return WinResponseData.handleSuccess(isStart);
    }

    @GetMapping("/moduleStatus")
    public WinResponseData moduleStatus(@RequestParam String ipAddr, String moduleName) {
        int status = mScheduleService.moduleStatus(ipAddr, moduleName);
        return WinResponseData.handleSuccess(String.valueOf(status));
    }

    @GetMapping("/moduleStop")
    public WinResponseData moduleStop(@RequestParam String ipAddr, String moduleName) {
        boolean isStop = mScheduleService.moduleStop(ipAddr, moduleName);
        return WinResponseData.handleSuccess(String.valueOf(isStop));
    }
}
