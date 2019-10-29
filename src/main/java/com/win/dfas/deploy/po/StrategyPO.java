package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 StrategyPO
 * @类描述 部署策略表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dc_strategy")
public class StrategyPO extends BasePO{

    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 帮助信息
     */
    private String help;

    /**
     * 删除标记
     */
    private int allow_delete;
}
