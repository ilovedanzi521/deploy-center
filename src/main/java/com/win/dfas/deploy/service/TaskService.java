package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.dto.TaskDTO;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 TaskService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:39
 */
public interface TaskService extends IService<TaskPO> {
    /**
     * 从任务ID获取对应的策略
     * @param taskId - task id
     */
    public StrategyPO selectStrategyByTask(long taskId);

    /**
     *  从任务ID获取对应的组设备列表
     * @param taskId
     * @return
     */
    public List<DevicePO> selectDeviceByTask(long taskId);

    /**
     * 分页列表查询
     * @param reqVO
     * @return
     */
    PageVO<TaskDTO> getPageInfo(BaseReqVO reqVO);
}
