package com.win.dfas.deploy.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.bean.DeployEnvConfig;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.core.io.file.FileReader;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @包名 com.win.dfas.deploy.schedule.AppManager
 * @类名 AppManager
 * @类描述 模块(应用)和策略的发布版本描述信息管理
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
public class AppManager {
    private final static Logger logger = LoggerFactory.getLogger(AppManager.class);

    @Autowired
    private DeployEnvConfig mEnvConfig;
    @Autowired
    private AppModuleService mModuleService;
    @Autowired
    private StrategyService mStrategyService;

    private Map<String, AppModulePO> mModuleFiles = new HashMap<String, AppModulePO>();
    private Map<String, StrategyPO> mStrategyFiles = new HashMap<String, StrategyPO>();

    private AtomicBoolean mScanLock = new AtomicBoolean(false);

    public boolean init() {
        return scan()==0;
    }

    public int scan() {
        logger.info("Start scan ...");
        int scanResult=1;
        if (mScanLock.compareAndSet(false, true)) {
            loadReleaseDescFile(mModuleFiles, mStrategyFiles);

            checkModulesUpdate(mModuleFiles);
            checkStrategyUpdate(mStrategyFiles);

            scanResult=0;
            mScanLock.set(false);
        }

        logger.info("Scan done. result="+scanResult);
        return scanResult;
    }

    /**
     * 从数据库读取应用模块信息到内存
     */
    private void loadModules(Map<String,AppModulePO> moduleMap) {
        List<AppModulePO> list = mModuleService.list(null);
        int total = list.size();
        for(int i=0; i<total; i++) {
            AppModulePO module = list.get(i);
            moduleMap.put(module.getPath(), module);
        }
    }

    /**
     * 从数据库读取策略信息到内存
     */
    private void  loadStrategy(Map<String,StrategyPO> strategyMap) {
        List<StrategyPO> list = mStrategyService.list(null);
        int total = list.size();
        for(int i=0; i<total; i++) {
            StrategyPO strategy = list.get(i);
            strategyMap.put(strategy.getPath(), strategy);
        }
    }

    /**
     * 从仓库中读取发布版本描述文件
     */
    private void loadReleaseDescFile(Map<String, AppModulePO> moduleMaps, Map<String, StrategyPO> strategyMaps) {
        final String releaseFile = mEnvConfig.getHomeDir()+"/"+mEnvConfig.getReleaseDescFile();
        boolean isFile = FileUtil.isFile(releaseFile);
        if(isFile) {
            FileReader fr = new FileReader(releaseFile, "utf-8");
            List<String> frLines = fr.readLines();
            int total = frLines.size();
            boolean parseModuleFlag=false;
            boolean parseScriptFlag=false;
            for(int i=0; i<total; i++) {
                String line = StrUtil.trim(frLines.get(i));
                if (StrUtil.isEmpty(line) || line.startsWith("#")) {
                    continue;
                } else if(line.startsWith("[module]")) {
                    parseModuleFlag=true;
                    parseScriptFlag=false;
                } else if(line.startsWith("[strategy]")) {
                    parseModuleFlag=false;
                    parseScriptFlag=true;
                } else {
                    if(parseModuleFlag) {
                       AppModulePO moduleOne = parseModule(line);
                       if(moduleOne != null) {
                           moduleMaps.put(moduleOne.getPath(), moduleOne);
                       }
                    } else if(parseScriptFlag) {
                       StrategyPO strategyOne = parseStrategy(line);
                       if(strategyOne != null) {
                           strategyMaps.put(strategyOne.getPath(), strategyOne);
                       }
                    }
                }
            } // for(int i=0; i<total; i++) {
        } // if(isFile) {
    }

    private AppModulePO parseModule(String line) {
        String[] elements = StrUtil.splitToArray(line, ' ');
        if(elements != null && elements.length >= 6) {
            String path = elements[0];
            String name = elements[1];
            String pack_dir = elements[2];
            String pack_ver = elements[3];
            String pack_file = elements[4];
            String allow_delete = elements[5];

            String packPath = pack_dir+"/"+pack_ver+"/"+pack_file;

            logger.debug("parseModule lines: shellName="+path+" moudleName="+name+" packpath="+packPath);
            AppModulePO module = new AppModulePO();
            module.setName(name);
            module.setPath(path);
            module.setPack_dir(pack_dir);
            module.setPack_ver(pack_ver);
            module.setPack_file(pack_file);
            module.setAllow_delete(Integer.parseInt(allow_delete));
            return module;
        }
        return null;
    }

    private StrategyPO parseStrategy(String line) {
        String[] elements = StrUtil.splitToArray(line, ' ');
        if(elements != null && elements.length >= 2) {
            String path = elements[0];
            String name = elements[1];
            String allow_delete = elements[2];

            logger.debug("parseStrategy lines: shellName="+path+" name="+name+" allow_delete="+allow_delete);
            StrategyPO strategy = new StrategyPO();
            strategy.setName(name);
            strategy.setPath(path);
            strategy.setAllow_delete(Integer.parseInt(allow_delete));
            return strategy;
        }
        return null;
    }

    public AppModulePO getModuleById(Long id) {
        return mModuleService.getById(id);
    }

    public AppModulePO getModuleByName(String name) {
        return mModuleFiles.get(name);
    }

    public StrategyPO getStrategyById(Long id) {
        return mStrategyService.getById(id);
    }

    public StrategyPO getStrategyByName(String name) {
        return mStrategyFiles.get(name);
    }

    /**
     * 检查mAppModulesList是否和数据库版本匹配，
     * 不匹配则更新数据库
     * @return
     *      true - 有更新
     *      false - 没有更新版本
     */
    private boolean checkModulesUpdate(Map<String, AppModulePO> moduleFileMap) {
        // 1. 检查modules
        // 获取db已经存在的modules
        Map<String,AppModulePO> moduleDbMap = new HashMap<String,AppModulePO>();
        loadModules(moduleDbMap);

        // 2. 获取release文件发布的modules
        Iterator<AppModulePO> it = moduleFileMap.values().iterator();
        while(it.hasNext()) {
            AppModulePO module = it.next();
            AppModulePO moduleDb = moduleDbMap.get(module.getPath());
            if(moduleDb == null) {
                // 2.1 新模块发现,添加
                logger.debug("checkAndUpdate found new module: "+module.toString());
                mModuleService.saveOrUpdate(module);

            }else if(!moduleDb.equals(module)){
                String packPath=module.getPack_dir()+"/"+module.getPack_ver()+"/"+module.getPack_file();

                // 2.2 发现模块有变更,更新
                logger.debug("checkAndUpdate has old module updated. ");
                logger.debug("OLD ==>");
                logger.debug(moduleDb.toString());
                logger.debug("New ==>");
                logger.debug(module.toString());

                // 2.3 更新module表
                moduleDb.setName(module.getName());
                moduleDb.setPath(module.getPath());
                moduleDb.setPack_dir(module.getPack_dir());
                moduleDb.setPack_ver(module.getPack_ver());
                moduleDb.setPack_file(module.getPack_file());
                moduleDb.setHelp(module.getHelp());
                moduleDb.setDesc(module.getDesc());
                moduleDb.setAllow_delete(module.getAllow_delete());
                mModuleService.saveOrUpdate(moduleDb);

                // 2.4 已经比较过，从列表中删除
                moduleDbMap.remove(module.getPath());
            }
        }

        // 3. 删除无效module
        if(moduleDbMap.size() > 0) {
           Iterator<AppModulePO> dbIt = moduleDbMap.values().iterator();
           while(dbIt.hasNext()) {
               AppModulePO moduleDb = dbIt.next();
               mModuleService.removeById(moduleDb.getId());
           }
        }

        return true;
    }

    private boolean checkStrategyUpdate(Map<String, StrategyPO> strategyFileMap) {
        // 1. 检查modules
        // 获取db已经存在的strategy
        Map<String,StrategyPO> strategyDbMap = new HashMap<String,StrategyPO>();
        loadStrategy(strategyDbMap);

        // 2. 获取release文件发布的strategy
        Iterator<StrategyPO> it = strategyFileMap.values().iterator();
        while(it.hasNext()) {
            StrategyPO strategy = it.next();
            StrategyPO strategyDb = strategyDbMap.get(strategy.getPath());
            if(strategyDb == null) {
                // 2.1 新模块发现,添加
                logger.debug("checkAndUpdate found new strategy: "+strategy.toString());
                mStrategyService.saveOrUpdate(strategy);
            }else if(!strategyDb.equals(strategy)) {
                // 2.2 发现策略有变更,更新
                logger.debug("checkAndUpdate has old strategy updated. ");
                logger.debug("OLD ==>");
                logger.debug(strategyDb.toString());
                logger.debug("New ==>");
                logger.debug(strategy.toString());

                // 2.3 更新module表
                strategyDb.setPath(strategy.getPath());
                mStrategyService.update(strategyDb, null);

                // 2.4 已经比较过，从列表中删除
                strategyDbMap.remove(strategyDb.getPath());
            }
        }

        // 3. 删除无效module
        if(strategyDbMap.size() > 0) {
            Iterator<StrategyPO> dbIt = strategyDbMap.values().iterator();
            while(dbIt.hasNext()) {
                StrategyPO strategy = dbIt.next();
                mStrategyService.removeById(strategy.getId());
            }
        }

        return true;
    }
}
