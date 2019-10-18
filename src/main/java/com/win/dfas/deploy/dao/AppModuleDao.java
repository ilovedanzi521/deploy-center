package com.win.dfas.deploy.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.win.dfas.deploy.bo.AppModuleBO;
import com.win.dfas.deploy.bo.DeviceGroupBO;
import com.win.dfas.deploy.po.AppModulePO;
import org.apache.ibatis.annotations.Param;

/**
 * @包名 com.win.dfas.deploy.dao
 * @类名 AppModuleDao
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:36
 */
public interface AppModuleDao extends BaseMapper<AppModulePO> {
    //IPage 分页器， Wrapper 条件构造器
    IPage<AppModuleBO> getPageList(IPage<AppModuleBO> page, @Param(Constants.WRAPPER) Wrapper query) throws Exception;

}
