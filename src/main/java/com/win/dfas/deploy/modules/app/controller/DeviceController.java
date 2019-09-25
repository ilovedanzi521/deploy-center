package com.win.dfas.deploy.modules.app.controller;

import com.win.dfas.common.vo.WinResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.modules.app.controller
 * @类名 DeviceController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/25 15:07
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @GetMapping("/list")
    public WinResponseData list(){

        return WinResponseData.handleSuccess("");
    }
}
