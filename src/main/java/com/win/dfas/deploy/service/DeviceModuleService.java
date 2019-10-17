package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.DeviceModuleRefPO;

import java.util.Collection;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 DeviceModuleService
 * @类描述 TODO:
 * @创建人 chenji
 * @创建时间 2019/10/10
 */
public interface DeviceModuleService extends IService<DeviceModuleRefPO> {

    /**
     * 更新批量DeviceModuleRefPO记录
     * @param list
     * @return
     */
    public boolean updateBatch(Collection<DeviceModuleRefPO> list);

    /**
     * 删除批量DeviceModuleRefPO记录
     * @param list
     * @return
     */
    public boolean removeBatch(Collection<DeviceModuleRefPO> list);
}
