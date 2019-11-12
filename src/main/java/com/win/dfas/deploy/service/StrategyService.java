package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.vo.response.StrategyDetailVO;

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
     * @param strategyPath
     * @return
     */
    List<AppModulePO> getAppModulesByStrategyPath(String strategyPath);

    /**
     * 查策略详情
     * @param id
     * @return
     */
    StrategyDetailVO getDetailById(Long id);

    /**
     * 查分页列表
     * @param reqVO
     * @return
     */
    PageVO<StrategyPO> getPageInfo(BaseReqVO reqVO);
}
