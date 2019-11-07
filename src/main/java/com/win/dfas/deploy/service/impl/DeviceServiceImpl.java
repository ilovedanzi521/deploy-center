package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.common.enumerate.DeviceEnum;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.common.validator.ValidatorUtils;
import com.win.dfas.deploy.common.validator.group.AddGroup;
import com.win.dfas.deploy.dao.DeviceDao;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.DeviceService;
import com.win.dfas.deploy.service.GroupDeviceRefService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    @Autowired
    private DeviceModuleService deviceModuleService;
    @Autowired
    private GroupDeviceRefService groupDeviceRefService;

    @Override
    public DevicePO connectTest(DevicePO device) {
        if (device.getId()==null){
            //新增设备
            this.safeSave(device);
        }
        //调度中心测试设备连通性
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
        //更新设备状态
        this.updateById(device);
//        更新调度中心设备
        Scheduler.get().addDevice(device);
        return device;
    }

    @Override
    public Boolean safeSave(DevicePO devicePO) {
        ValidatorUtils.validateEntity(devicePO, AddGroup.class);
        DevicePO existPo = this.getOne(new QueryWrapper<DevicePO>().eq("ip_address",devicePO.getIpAddress()));
        if (existPo!=null){
            throw new BaseException("当前设备已经存在，禁止重复添加设备！");
        }
        Boolean saved = this.save(devicePO);
        if (!saved){
            throw new BaseException("设备添加失败，请联系管理员！");
        }
        Scheduler.get().addDevice(devicePO);
        return saved;
    }

    /**
     * 安全删除单个设备（包括更新调度中心设备上下文）
     * @param id
     * @return
     */
    @Override
    public Boolean safeRemove(Long id) {
        beforeSafeRemove(id);
//        删除设备
        return this.removeById(id);
    }

    private void beforeSafeRemove(Long id) {
        DevicePO existPo = this.getById(id);
        if (existPo == null){
            throw new BaseException(existPo.getIpAddress()+"机器已经不存在！");
        }
        if(detectedDeviceModule(id)){
            throw new BaseException(existPo.getIpAddress()+"机器已经部署任务，无法进行安全删除！");
        }
//        删除组中的设备
        this.groupDeviceRefService.removeByDeviceId(id);
//        删除调度中心设备
        Scheduler.get().delDevice(existPo);
    }

    private boolean detectedDeviceModule(Long deviceId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("device_id",deviceId);
        int count = this.deviceModuleService.count(queryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    /**
     * 安全批量删除设备（包括更新调度中心设备上下文）
     * @param ids
     * @return
     */
    @Override
    public Boolean safeRemoveBatch(List<Long> ids) {
        for (Long id : ids) {
            beforeSafeRemove(id);
        }
        return this.removeByIds(ids);
    }
}
