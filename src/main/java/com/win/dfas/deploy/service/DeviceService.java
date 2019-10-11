package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.vo.request.DeviceReqVO;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 DeviceService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:42
 */
public interface DeviceService extends IService<DevicePO> {
    /**
     * 连接测试
     * @param device
     * @return
     */
    DevicePO connectTest(DevicePO device);
}
