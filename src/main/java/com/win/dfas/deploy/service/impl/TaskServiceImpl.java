package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.dao.TaskDao;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.service.GroupService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 TaskServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskPO> implements TaskService {

    @Autowired
    private ScheduleCenterService mScheduleService;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private GroupService groupService;
    /**
     * 从任务ID获取对应的策略
     * @param taskId - task id
     */
    @Override
    public StrategyPO selectStrategyByTask(long taskId) {
        StrategyPO strategy = (StrategyPO)this.sqlSessionBatch().selectOne("com.win.dfas.deploy.dao.TaskDao.selectStrategyByTask", taskId);
        return strategy;
    }

    /**
     *  从任务ID获取对应的组设备列表
     * @param taskId
     * @return
     */
    @Override
    public List<DevicePO> selectDeviceByTask(long taskId) {
        List<DevicePO> list = (List)this.sqlSessionBatch().selectList("com.win.dfas.deploy.dao.TaskDao.selectDeviceByTask", taskId);
        return list;
    }

    /**
     * 分页列表
     * @param reqVO
     * @return
     */
    @Override
    public PageVO<TaskDTO> getPageInfo(BaseReqVO reqVO) {
        Page<TaskDTO> page = new Page<>(reqVO.getReqPageNum(),reqVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<TaskPO> queryWrapper = new QueryWrapper<TaskPO>();
        queryWrapper.orderByDesc("create_time");
        IPage<TaskDTO> pageList = this.baseMapper.getPageList(page,queryWrapper);

        return new PageVO(page,pageList.getRecords());
    }

    @Override
    public TaskPO deploy(Long id) {
        this.mScheduleService.deploy(id);
        return this.getById(id);
    }

    @Override
    public TaskPO unDeploy(Long id) {
        this.mScheduleService.undeploy(id);
        return this.getById(id);
    }

    @Override
    public void appSourceScan() {
        this.mScheduleService.appSourceScan();
    }

    @Override
    public StrategyPO getStrategyByTask(TaskPO taskPO) {
        return this.strategyService.getById(taskPO.getStrategyId());
    }

    @Override
    public List<DevicePO> getDevicesByTask(TaskPO taskPO) {
        return this.groupService.getDevicesByGroupId(taskPO.getGroupId());
    }
}
