package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.GroupDeviceRefDao;
import com.win.dfas.deploy.po.GroupDeviceRefPO;
import com.win.dfas.deploy.service.GroupDeviceRefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 GroupDeviceRefServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/08 15:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupDeviceRefServiceImpl extends ServiceImpl<GroupDeviceRefDao, GroupDeviceRefPO> implements GroupDeviceRefService {

    @Override
    public boolean removeByDeviceId(Long deviceId) {
        QueryWrapper<GroupDeviceRefPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        return this.remove(queryWrapper);
    }
}
