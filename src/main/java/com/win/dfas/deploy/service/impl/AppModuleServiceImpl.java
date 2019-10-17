package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 AppModuleServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppModuleServiceImpl extends ServiceImpl<AppModuleDao, AppModulePO> implements AppModuleService {

    @Autowired
    private ScheduleCenterService mScheduleService;

    @Override
    public boolean start(String ipAddr, String moduleName) {
        return mScheduleService.moduleStart(ipAddr, moduleName);
    }

    @Override
    public boolean stop(String ipAddr, String moduleName) {
        return mScheduleService.moduleStop(ipAddr, moduleName);
    }

    @Override
    public int status(String ipAddr, String moduleName) {
        return mScheduleService.moduleStatus(ipAddr, moduleName);
    }

}
