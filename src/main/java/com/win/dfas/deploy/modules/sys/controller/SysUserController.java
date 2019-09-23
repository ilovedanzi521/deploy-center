package com.win.dfas.deploy.modules.sys.controller;

import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.modules.sys.po.SysUserPO;
import com.win.dfas.deploy.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @包名 com.win.dfas.deploy.modules.app.controller
 * @类名 TestController
 * @类描述 接口测试类
 * @创建人 heshansen
 * @创建时间 2019/09/22 15:17
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;


    @SysLog("新增用户")
    @PostMapping("/add")
    public WinResponseData add(@RequestBody SysUserPO sysUserPO){
        sysUserService.save(sysUserPO);
        return WinResponseData.handleSuccess("Success add new sysUserPO", sysUserPO);
    }
    @GetMapping("/infoById")
    public WinResponseData infoById(Integer id){
        SysUserPO sysUserPO = sysUserService.getById(id);
        return WinResponseData.handleSuccess("Success get sysUserPO info", sysUserPO);
    }
}
