package com.win.dfas.deploy.modules.sys.controller;

import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.modules.sys.entity.User;
import com.win.dfas.deploy.modules.sys.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public WinResponseData add(@RequestBody User user){
        userService.save(user);
        return WinResponseData.handleSuccess("Success add new user",user);
    }
    @GetMapping("/infoById")
    public WinResponseData infoById(Integer id){
        User user = userService.getById(id);
        return WinResponseData.handleSuccess("Success get user info",user);
    }
}
