package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.vo.request.DeviceReqVO;
import com.win.dfas.deploy.vo.response.PageVO;

import java.util.List;

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

    /**
     * 安全保存（包括更新调度中心设备上下文）
     * @param devicePO
     * @return
     */
    Boolean safeSave(DevicePO devicePO);

    /**
     * 安全删除单个设备（包括更新调度中心设备上下文）
     * @param id
     * @return
     */
    Boolean safeRemove(Long id);

    /**
     * 安全批量删除设备（包括更新调度中心设备上下文）
     * @param ids
     * @return
     */
    Boolean safeRemoveBatch(List<Long> ids);

    /**
     * 查分页列表
     * @param reqVO
     * @return
     */
    PageVO<DevicePO> getPageInfo(BaseReqVO reqVO);
}
