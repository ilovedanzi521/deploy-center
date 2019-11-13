package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @Autowired
    private ScheduleCenterService scheduleService;

    @Override
    public IService<DevicePO> getBaseService() {
        return this.deviceService;
    }

    @SysLog("安全新增设备")
    @PostMapping("/safeSave")
    public WinResponseData safeSave(@RequestBody DevicePO devicePO){
        Boolean success = this.deviceService.safeSave(devicePO);
        if (!success){
            return WinResponseData.handleError("保存失败！");
        }
        return WinResponseData.handleSuccess("保存成功！");
    }
    @SysLog("安全删除设备")
    @DeleteMapping("/safeRemove/{id}")
    public WinResponseData safeRemove(@PathVariable Long id){

        Boolean success = this.deviceService.safeRemove(id);
        if (!success){
            return WinResponseData.handleError("删除设备失败！");
        }
        return WinResponseData.handleSuccess("删除设备成功！");
    }

    @SysLog("安全批量删除设备")
    @PostMapping("/safeRemoveBatch")
    public WinResponseData safeRemove(@RequestBody Long[] ids){

        Boolean success = this.deviceService.safeRemoveBatch(Arrays.asList(ids));
        if (!success){
            return WinResponseData.handleError("批量删除设备失败！");
        }
        return WinResponseData.handleSuccess("批量删除设备成功！");
    }

    @PostMapping("/connectTest")
    public WinResponseData connectTest(@RequestBody DevicePO device){

        DevicePO dev = deviceService.connectTest(device);
        return WinResponseData.handleSuccess(dev);
    }

    @GetMapping("/connectDev")
    public WinResponseData connectDevice(@RequestParam String ipAddr ) {
        DevicePO dev = scheduleService.getDevice(ipAddr);
        if (dev==null){
            return WinResponseData.handleError("请先添加设备!");
        }
        return connectTest(dev);
    }

    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody BaseReqVO baseReqVO) {
        PageVO<DevicePO> pageVO = this.deviceService.getPageInfo(baseReqVO);
        return WinResponseData.handleSuccess(pageVO);
    }
}
