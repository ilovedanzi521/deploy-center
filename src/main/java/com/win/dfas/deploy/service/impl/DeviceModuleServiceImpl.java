package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.DeviceModuleDao;
import com.win.dfas.deploy.po.DeviceModuleRefPO;
import com.win.dfas.deploy.service.DeviceModuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceModuleServiceImpl extends ServiceImpl<DeviceModuleDao, DeviceModuleRefPO> implements DeviceModuleService {

    @Override
    public boolean updateBatch(Collection<DeviceModuleRefPO> list) {
        if(list == null || list.size()==0) {
            return false;
        }
        Iterator<DeviceModuleRefPO> it = list.iterator();
        while(it.hasNext()) {
            DeviceModuleRefPO devModObj = it.next();
            DeviceModuleRefPO existRef = this.getOne(new QueryWrapper<DeviceModuleRefPO>()
                    .eq("device_id",devModObj.getDeviceId())
                    .eq("module_id",devModObj.getModuleId()));
            if (existRef == null){
                this.save(devModObj);
            }
        }
        return true;
    }

    @Override
    public boolean removeBatch(Collection<DeviceModuleRefPO> list) {
        if(list == null || list.size()==0) {
            return false;
        }
        Iterator<DeviceModuleRefPO> it = list.iterator();
        while(it.hasNext()) {
            DeviceModuleRefPO devModObj = it.next();
            boolean removed=remove(new QueryWrapper<DeviceModuleRefPO>()
                    .eq("device_id",devModObj.getDeviceId())
                    .eq("module_id",devModObj.getModuleId()));
        }
        return true;
    }
}
