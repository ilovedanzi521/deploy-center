package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseRepVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.po.DeviceModuleRefPO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.vo.request.DeviceReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.List;

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
    private DeviceModuleService devModService;

    @Override
    public IService<DevicePO> getBaseService() {
        return this.deviceService;
    }

    @GetMapping("/list")
    public WinResponseData list(){
        List<DevicePO> list = deviceService.list(null);
        return WinResponseData.handleSuccess(list);
    }

    @PostMapping("/connectTest")
    public WinResponseData connectTest(@Validated @RequestBody DevicePO device){
        return WinResponseData.handleSuccess(deviceService.connectTest(device));
    }

    @GetMapping("/connectDev")
    public WinResponseData connectDevice(@RequestParam String ipAddr) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddr);
        DevicePO device = null;
        if(context != null) {
            device = context.getDevice();
        }
        return WinResponseData.handleSuccess(deviceService.connectTest(device));
    }

    @GetMapping("saveDevmod")
    public String save(@RequestParam long devid, @RequestParam long modid) {
        DeviceModuleRefPO devModRef = new DeviceModuleRefPO();
        devModRef.setModuleId(modid);
        devModRef.setDeviceId(devid);
        boolean removed = devModService.remove(new QueryWrapper<DeviceModuleRefPO>().eq(
                "device_id",devModRef.getDeviceId()).eq("module_id",devModRef.getModuleId()));

        return String.valueOf(devModService.save(devModRef));
    }
}
