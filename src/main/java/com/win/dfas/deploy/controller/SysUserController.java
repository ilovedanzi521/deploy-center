package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.SysUserPO;
import com.win.dfas.deploy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.modules.app.controller
 * @类名 TestController
 * @类描述 用户接口测试类
 * @创建人 heshansen
 * @创建时间 2019/09/22 15:17
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController<SysUserPO> {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public IService<SysUserPO> getBaseService() {
        this.setBaseAppName("用户信息");
        return this.sysUserService;
    }
}
