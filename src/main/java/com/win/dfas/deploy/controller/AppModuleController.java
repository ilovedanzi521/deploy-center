package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.bo.AppModuleBO;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.dto.GroupTree;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.impl.AppModuleServiceImpl;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.response.AppModuleTreeVO;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.SysexMessage;
import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

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
    private DeployEnvBean mEnvConfig;

    @Autowired(required = true)
    private ScheduleCenterService mScheduleService;

    @Override
    public IService<AppModulePO> getBaseService() {
        return this.appModuleService;
    }

    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody BaseReqVO reqVO){
        PageVO<AppModuleTreeVO> pageVO = this.appModuleService.getAppModuleTreePageInfo(reqVO);
        return WinResponseData.handleSuccess(pageVO);
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
