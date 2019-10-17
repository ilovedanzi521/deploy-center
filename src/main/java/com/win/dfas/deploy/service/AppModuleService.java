package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.AppModulePO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 AppModuleService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:42
 */
public interface AppModuleService extends IService<AppModulePO> {

    public boolean start(String ipAddr, String moduleName);

    public boolean stop(String ipAddr, String moduleName);

    public int status(String ipAddr, String moduleName);
}
