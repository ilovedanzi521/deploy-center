package com.win.dfas.deploy.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.win.dfas.deploy.dto.StatisticsDTO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.dto.TaskDTO;
import org.apache.ibatis.annotations.Param;

/**
 * @包名 com.win.dfas.deploy.dao
 * @类名 TaskDao
 * @类描述 任务管理数据dao层
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:38
 */
public interface TaskDao extends BaseMapper<TaskPO> {
    /**
     * 查分页列表-自定义
     * @param page
     * @param query
     * @return
     */
    IPage<TaskDTO> getPageList(IPage<TaskDTO> page, @Param(Constants.WRAPPER) Wrapper query);

    /**
     * 查统计信息
     * @return
     */
    StatisticsDTO selectStatisticsInfo();
}
