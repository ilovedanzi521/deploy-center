package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.service.AppModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private AppModuleService appModuleService;

    @Override
    public IService<AppModulePO> getBaseService() {
        return this.appModuleService;
    }
}
