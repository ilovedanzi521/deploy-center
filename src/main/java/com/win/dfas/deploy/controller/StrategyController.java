package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 StrategyController
 * @类描述 策略控制器
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:41
 */
@RestController
@RequestMapping("/strategy")
public class StrategyController extends BaseController<StrategyPO> {
    @Autowired
    private StrategyService strategyService;

    @Override
    public IService<StrategyPO> getBaseService() {
        return this.strategyService;
    }
}
