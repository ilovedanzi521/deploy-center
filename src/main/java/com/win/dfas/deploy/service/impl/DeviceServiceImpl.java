package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.common.enumerate.DeviceEnum;
import com.win.dfas.deploy.dao.DeviceDao;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 DeviceServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:43
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceServiceImpl extends ServiceImpl<DeviceDao, DevicePO> implements DeviceService {

    @Autowired
    private ScheduleCenterService mScheduleService;

    public DeviceServiceImpl() {
        //mScheduleService = new ScheduleCenterService();
        //mScheduleService = (ScheduleCenterService)SpringContextUtils.getBean("schedule_center_service");
    }

    @Override
    public DevicePO connectTest(DevicePO device) {
        List<String> resultList = mScheduleService.connectDevice(device);
        boolean isSuccess = false;
        String devName = "unknown";
        if(resultList != null && resultList.size() >= 2) {
            isSuccess = ShellUtils.isSuccess(resultList);
            devName = resultList.get(0);
            log.info("ConnectTest devName="+devName+" isSucc="+isSuccess);
            ShellUtils.listLog(log, resultList);
        }else{
            log.info("ConnectTest failed. resultList ==> ");
            ShellUtils.listLog(log, resultList);
        }

        if(isSuccess) {
            device.setAlias(devName);
            device.setStatus(DeviceEnum.ConnectStatus.NORMAL.getValue());
        } else {
            device.setAlias(devName);
            device.setStatus(DeviceEnum.ConnectStatus.FAILURE.getValue());
        }
        return device;
    }
}
