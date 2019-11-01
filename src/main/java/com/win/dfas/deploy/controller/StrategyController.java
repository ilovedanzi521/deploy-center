package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody BaseReqVO reqVO){
        PageVO<StrategyPO> pageVO = this.strategyService.getPageInfo(reqVO);
        return WinResponseData.handleSuccess(pageVO);
    }
    @GetMapping("/detail/{id}")
    public WinResponseData detailById(@PathVariable Long id){

        return WinResponseData.handleSuccess(this.strategyService.getDetailById(id));
    }
}
