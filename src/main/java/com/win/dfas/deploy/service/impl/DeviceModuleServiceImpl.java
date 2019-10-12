package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.DeviceDao;
import com.win.dfas.deploy.dao.DeviceModuleDao;
import com.win.dfas.deploy.po.DeviceModuleRefPO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceModuleServiceImpl extends ServiceImpl<DeviceModuleDao, DeviceModuleRefPO> implements DeviceModuleService {
}
