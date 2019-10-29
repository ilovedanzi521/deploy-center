package com.win.dfas.deploy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.StrategyDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
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
        DeployEnvBean mEnvConfig = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
        String shellFile = "";
        if(!StrUtil.isEmpty(strategy.getPath())) {
            shellFile = mEnvConfig.getScriptsDir() + File.separator + strategy.getPath();
        }else {
            log.error(strategy.getName()+"-策略脚本不存在！");
            return null;
        }
        List<String> resultList =ShellUtils.getAppModules(shellFile);
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
}
