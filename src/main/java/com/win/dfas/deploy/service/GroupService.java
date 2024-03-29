package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.dto.DeviceGroupDTO;
import com.win.dfas.deploy.dto.GroupTree;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.request.GroupVO;
import com.win.dfas.deploy.vo.response.PageVO;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 GroupService
 * @类描述 设备组服务
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:41
 */
public interface GroupService extends IService<GroupPO> {
    /**
     * 获取设备组树形分页列表信息
     * @param deviceParamsVO
     * @return
     */
    PageVO<GroupTree> getGroupTreePageInfo(DeviceParamsVO deviceParamsVO);

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

    /**
     * 获取设备组详情
     * @param id
     * @return
     */
    DeviceGroupDTO getInfo(Long id);

    /**
     * 获取设备组分页信息
     * @param deviceParamsVO
     * @return
     */
    IPage<DeviceGroupDTO> getPageList(DeviceParamsVO deviceParamsVO);

    /**
     * 安全删除设备组（包括其关联信息）
     * @param id
     * @return
     */
    Boolean safeRemove(Long id);

    /**
     * 安全批量删除组（包括其关联信息）
     * @param ids
     * @return
     */
    Boolean safeRemoveBatch(List<Long> ids);

    /**
     * 通过组id查询设备列表
     * @param groupId
     * @return
     */
    List<DevicePO> getDevicesByGroupId(Long groupId);
}
