package com.win.dfas.deploy.dto;

import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 TaskDTO
 * @类描述 任务管理列表传输对象
 * @创建人 heshansen
 * @创建时间 2019/10/24 13:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDTO extends TaskPO {
    private static final long serialVersionUID = 1989576100608004815L;
    /**
     * 策略信息
     */
    private StrategyPO strategy;
    /**
     * 设备组信息
     */
    private GroupPO group;
}
