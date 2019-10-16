package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.TaskDao;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.service.TaskService;
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
}
