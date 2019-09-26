package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 TaskPO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:26
 */
@Data
@TableName("dc_task")
public class TaskPO extends BasePO {

    /**
     * 策略id
     */
    private Long strategyId;
    /**
     * 设备组id
     */
    private Long groupId;
    /**
     * 部署状态
     */
    private Integer status;
}
