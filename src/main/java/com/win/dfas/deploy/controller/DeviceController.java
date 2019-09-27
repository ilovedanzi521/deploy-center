package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 DeviceController
 * @类描述 机器控制类
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:42
 */
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController<DevicePO> {
    @Autowired
    private DeviceService deviceService;

    @Override
    public IService<DevicePO> getBaseService() {
        return this.deviceService;
    }
}
