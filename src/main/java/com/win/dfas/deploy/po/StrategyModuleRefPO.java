package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 StrategyModuleRefPO
 * @类描述 策略关联应用模块表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:04
 */
@Data
@TableName("dc_strategy_module_ref")
public class StrategyModuleRefPO {

    /**
     * 策略id
     */
    private Long strategyId;
    /**
     * 应用模块id
     */
    private Long moduleId;
}
