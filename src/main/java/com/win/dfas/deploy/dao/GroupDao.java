package com.win.dfas.deploy.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.win.dfas.deploy.dto.DeviceGroupDTO;
import com.win.dfas.deploy.po.GroupPO;
import org.apache.ibatis.annotations.Param;

/**
 * @包名 com.win.dfas.deploy.dao
 * @类名 GroupDao
 * @类描述 组dao
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:34
 */
public interface GroupDao extends BaseMapper<GroupPO> {

    /**
     * 获取单个设备组信息
     * @param id
     * @return
     */
    DeviceGroupDTO getOne(Long id);
    //IPage 分页器， Wrapper 条件构造器
    IPage<DeviceGroupDTO> getPageList(IPage<DeviceGroupDTO> page, @Param(Constants.WRAPPER) Wrapper query) throws Exception;

}
