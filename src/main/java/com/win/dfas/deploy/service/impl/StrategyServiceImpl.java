package com.win.dfas.deploy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.dao.StrategyDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.util.SpringContextUtils;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.vo.response.StrategyDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 StrategyServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:44
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class StrategyServiceImpl extends ServiceImpl<StrategyDao, StrategyPO> implements StrategyService {
    @Override
    public List<AppModulePO> getAppModules(StrategyPO strategy){
        DeployEnvBean mEnvConfig = Scheduler.get().getDeployEnvConfig();
        String shellFile = "";
        if(!StrUtil.isEmpty(strategy.getPath())) {
            shellFile = mEnvConfig.getScriptsDir() + File.separator + strategy.getPath();
        }else {
            log.error(strategy.getName()+"-策略脚本不存在！");
            return null;
        }
        return getStrategyAppModuleList(shellFile);
    }

    private List<AppModulePO> getStrategyAppModuleList(String filePath){
        List<String> resultList =ShellUtils.getAppModules(filePath);
        AppManager appManager = Scheduler.get().getAppManager();
        List<AppModulePO> modulePOS = new ArrayList<AppModulePO>();
        if (CollectionUtil.isNotEmpty(resultList)){
            for(String path: resultList) {
                AppModulePO appModulePO = appManager.getModuleByPath(path);
                if (appModulePO!=null){
                    modulePOS.add(appModulePO);
                }
            }
        }
        return modulePOS;
    }

    /**
     * 获取策略详情
     * TODO 查出绑定应用列表，可初始化扫描脚本时(Scheduler.get().getAppManager().scan())进行更新。
     * @param id
     * @return
     */
    @Override
    public StrategyDetailVO getDetailById(Long id) {
        StrategyPO strategyPO = this.getById(id);
        StrategyDetailVO detailVO = new StrategyDetailVO();
        BeanUtils.copyProperties(strategyPO,detailVO);
        if (detailVO!=null && StrUtil.isNotBlank(detailVO.getPath())){
            DeployEnvBean envBean = Scheduler.get().getDeployEnvConfig();
            String filePath = envBean.getScriptsDir()+File.separator+detailVO.getPath();
//            detailVO.setAppModules(getStrategyAppModuleList(filePath));
            detailVO.setShellContent(FileUtil.readUtf8Lines(filePath));
        }
        return detailVO;
    }

    @Override
    public PageVO<StrategyPO> getPageInfo(BaseReqVO reqVO) {
        Page<StrategyPO> page = new Page<>(reqVO.getReqPageNum(),reqVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<StrategyPO> queryWrapper = new QueryWrapper<StrategyPO>();
        queryWrapper.orderByDesc("create_time");
        IPage<StrategyPO> pageList = this.baseMapper.selectPage(page,queryWrapper);

        return new PageVO(page);
    }
}
