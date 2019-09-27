package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.SysLogPO;
import com.win.dfas.deploy.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 SysLogController
 * @类描述 日志控制器
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:41
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController<SysLogPO> {
    @Autowired
    private SysLogService sysLogService;

    @Override
    public IService<SysLogPO> getBaseService() {
        return this.sysLogService;
    }
}
