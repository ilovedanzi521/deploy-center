package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;

import java.io.IOException;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 StrategyService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:40
 */
public interface StrategyService extends IService<StrategyPO> {
    /**
     * 查策略绑定应用模块列表
     * @param strategy
     * @return
     */
    List<AppModulePO> getAppModules(StrategyPO strategy);
}
