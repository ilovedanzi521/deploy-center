package com.win.dfas.deploy.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.win.dfas.deploy.dto.AppModuleDTO;
import com.win.dfas.deploy.dto.DeviceModuleRefDTO;
import com.win.dfas.deploy.po.AppModulePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.dao
 * @类名 AppModuleDao
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:36
 */
public interface AppModuleDao extends BaseMapper<AppModulePO> {
    //IPage 分页器， Wrapper 条件构造器
    IPage<AppModuleDTO> getPageList(IPage<AppModuleDTO> page, @Param(Constants.WRAPPER) Wrapper query) throws Exception;

    /**
     * 获取单个应用模块的实例列表（运行在哪些机器上）
     * @param id
     * @return
     */
    List<DeviceModuleRefDTO> getInstanceList(Long id);
}
