package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.impl.AppModuleServiceImpl;
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

    @Override
    public IService<AppModulePO> getBaseService() {
        return this.appModuleService;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<AppModulePO> list() {
       return appModuleService.list();
    }
}
