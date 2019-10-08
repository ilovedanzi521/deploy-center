package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.request.GroupVO;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 GroupService
 * @类描述 设备组服务
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:41
 */
public interface GroupService extends IService<GroupPO> {
    /**
     * 获取设备树形分页列表信息
     * @param deviceParamsVO
     * @return
     */
    IPage<GroupPO> getGroupTreePageInfo(DeviceParamsVO deviceParamsVO);

    /**
     * 安全保存组及其关联设备
     * @param groupVO
     * @return
     */
    Boolean safeSave(GroupVO groupVO);

    /**
     * 安全更新设备组及其关联设备
     * @param groupVO
     * @return
     */
    Boolean safeUpdate(GroupVO groupVO);
}
