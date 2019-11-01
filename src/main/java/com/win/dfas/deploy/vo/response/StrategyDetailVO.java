package com.win.dfas.deploy.vo.response;

import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import lombok.Data;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 StrategyDetailVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/31 21:09
 */
@Data
public class StrategyDetailVO extends StrategyPO {
    /**
     * 脚本详情
     */
    private List<String> shellContent;
    /**
     * 策略绑定应用模块
     */
    private List<AppModulePO> appModules;
}
