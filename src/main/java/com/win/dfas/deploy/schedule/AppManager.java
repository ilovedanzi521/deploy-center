package com.win.dfas.deploy.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.win.dfas.deploy.dao.DeviceModuleDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DeviceModuleRefPO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.core.io.file.FileReader;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @包名 com.win.dfas.deploy.schedule.AppManager
 * @类名 AppManager
 * @类描述 模块(应用)和策略的发布版本描述信息管理
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
@Slf4j
public class AppManager {

    private DeployEnvBean mEnvConfig;
    private AppModuleService mModuleService;
    private StrategyService mStrategyService;
    private DeviceModuleDao mDeviceModuleDao;

    private Map<String, AppModulePO> mModuleFiles = new Hashtable<String, AppModulePO>();
    private Map<String, StrategyPO> mStrategyFiles = new Hashtable<String, StrategyPO>();

    private AtomicBoolean mScanLock = new AtomicBoolean(false);

    public AppManager() {
    }

    public boolean init() {
        mEnvConfig = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
        mModuleService = SpringContextUtils.getBean(AppModuleService.class);
        mStrategyService = SpringContextUtils.getBean(StrategyService.class);
        mDeviceModuleDao = SpringContextUtils.getBean(DeviceModuleDao.class);

        return scan()==0;
    }

    public int scan() {
        log.info("Start scan ...");
        int scanResult=1;
        if (mScanLock.compareAndSet(false, true)) {
            mModuleFiles.clear();
            mStrategyFiles.clear();
            loadReleaseDescFile(mModuleFiles, mStrategyFiles);

            checkModulesUpdate(mModuleFiles);
            checkStrategyUpdate(mStrategyFiles);

            // 重新从数据库中读取modules和straties,否则PO将没有ID
            mModuleFiles.clear();
            loadModules(mModuleFiles);
            mStrategyFiles.clear();
            loadStrategy(mStrategyFiles);

            Iterator<AppModulePO> it = mModuleFiles.values().iterator();
            while(it.hasNext()) {
                log.info(it.next().toString());
            }

            Iterator<StrategyPO> st = mStrategyFiles.values().iterator();
            while(st.hasNext()) {
                log.info(st.next().toString());
            }

            scanResult=0;
            mScanLock.set(false);

        }

        log.info("Scan done. result="+scanResult);
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
        final String releaseFile = mEnvConfig.getHomeDir()+ File.separator+mEnvConfig.getReleaseDescFile();
        boolean isFile = FileUtil.isFile(releaseFile);
        log.info("loadReleaseDescFile: "+releaseFile+ " isFile: "+isFile);
        if(isFile) {
            FileReader fr = new FileReader(releaseFile, "utf-8");
            List<String> frLines = fr.readLines();
            int total = frLines.size();
            boolean parseModuleFlag=false;
            boolean parseScriptFlag=false;
            for(int i=0; i<total; i++) {
                String line = StrUtil.trim(frLines.get(i));
                log.debug("loadReleaseDescFile line"+i+": "+line);
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
        List<String> elements = StrUtil.splitTrim(line, "\t");
        if(elements != null && elements.size() >= 6) {
            String path = elements.get(0);
            String name = elements.get(1);
            String pack_dir = elements.get(2);
            String pack_ver = elements.get(3);
            String pack_file = elements.get(4);
            String allow_delete = elements.get(5);

            String packPath = pack_dir+File.separator+pack_ver+File.separator+pack_file;

            log.debug("parseModule lines: shellName="+path+" moudleName="+name+" packpath="+packPath);
            AppModulePO module = new AppModulePO();
            module.setName(name);
            module.setPath(path);
            module.setPackDir(pack_dir);
            module.setPackVer(pack_ver);
            module.setPackFile(pack_file);
            module.setAllowDelete(Integer.parseInt(allow_delete));
            return module;
        }
        return null;
    }

    private StrategyPO parseStrategy(String line) {
        List<String> elements = StrUtil.splitTrim(line, "\t");
        if(elements != null && elements.size() >= 2) {
            String path = elements.get(0);
            String name = elements.get(1);
            String allow_delete = elements.get(2);

            log.debug("parseStrategy lines: shellName="+path+" name="+name+" allow_delete="+allow_delete);
            StrategyPO strategy = new StrategyPO();
            strategy.setName(name);
            strategy.setPath(path);
            strategy.setAllow_delete(Integer.parseInt(allow_delete));
            return strategy;
        }
        return null;
    }

    public AppModulePO getModuleById(Long id) {
        Iterator<AppModulePO> it = mModuleFiles.values().iterator();
        while(it.hasNext()) {
            AppModulePO m = it.next();
            if (m != null && id.equals(m.getId())) {
                return m;
            }
        }
        return null;
    }

    public AppModulePO getModuleByPath(String path) {
        return mModuleFiles.get(path);
    }

    public AppModulePO getModuleByName(String name) {
        if(StrUtil.isEmpty(name)){
            return null;
        }

        Iterator<AppModulePO> it = mModuleFiles.values().iterator();
        while(it.hasNext()) {
            AppModulePO m = it.next();
            if (m != null && name.equals(m.getName())) {
               return m;
            }
        }
        return null;
    }

    public StrategyPO getStrategyById(Long id) {
        return mStrategyService.getById(id);
    }

    public StrategyPO getStrategyByPath(String path) {
        return mStrategyFiles.get(path);
    }

    public StrategyPO getStrategyByName(String name) {
        if(StrUtil.isEmpty(name)){
            return null;
        }

        Iterator<StrategyPO> it = mStrategyFiles.values().iterator();
        while(it.hasNext()) {
            StrategyPO m = it.next();
            if (m != null && name.equals(m.getName())) {
                return m;
            }
        }
        return null;
    }
    /**
     * 检查mAppModulesList是否和数据库版本匹配，
     * 不匹配则更新数据库,数据库有多余则删除,
     * 同时更新设备模块关联表
     * @return
     *      true - 有更新
     *      false - 没有更新版本
     */
    private boolean checkModulesUpdate(Map<String, AppModulePO> moduleFileMap) {
        log.info("checkModuleUpdate start.");
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
                log.debug("checkAndUpdate found new module: "+module.toString());
                mModuleService.saveOrUpdate(module);

            }else if(!moduleDb.equals(module)){
                String packPath=module.getPackDir()+"/"+module.getPackVer()+"/"+module.getPackFile();

                // 2.2 发现模块有变更,更新
                log.debug("checkAndUpdate has old module updated. ");
                log.debug("OLD ==>");
                log.debug(moduleDb.toString());
                log.debug("New ==>");
                log.debug(module.toString());

                // 2.3 更新module表
                moduleDb.setName(module.getName());
                moduleDb.setPath(module.getPath());
                moduleDb.setPackDir(module.getPackDir());
                moduleDb.setPackVer(module.getPackVer());
                moduleDb.setPackFile(module.getPackFile());
                moduleDb.setHelp(module.getHelp());
                moduleDb.setDesc(module.getDesc());
                moduleDb.setAllowDelete(module.getAllowDelete());
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

               DeviceModuleRefPO ref = new DeviceModuleRefPO();
               ref.setModuleId(moduleDb.getId());
               QueryWrapper<DeviceModuleRefPO> wrapper = new QueryWrapper<>();
               wrapper.setEntity(ref);
               log.info("checkModuleUpdate remove deviceModule "+ref.toString());
               mDeviceModuleDao.delete(wrapper);

               log.info("checkModuleUpdate remove "+moduleDb.toString());
               mModuleService.removeById(moduleDb.getId());
           }
        }

        log.info("checkModuleUpdate end.");
        return true;
    }

    private boolean checkStrategyUpdate(Map<String, StrategyPO> strategyFileMap) {
        log.info("checkStrategyUpdate start.");
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
                log.debug("checkAndUpdate found new strategy: "+strategy.toString());
                mStrategyService.saveOrUpdate(strategy);
            }else if(!strategyDb.equals(strategy)) {
                // 2.2 发现策略有变更,更新
                log.debug("checkAndUpdate has old strategy updated. ");
                log.debug("OLD ==>");
                log.debug(strategyDb.toString());
                log.debug("New ==>");
                log.debug(strategy.toString());

                // 2.3 更新module表
                strategyDb.setPath(strategy.getPath());
                strategyDb.setAllow_delete(strategy.getAllow_delete());
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
                log.info("checkStrategyUpdate remove "+strategy.toString());
                mStrategyService.removeById(strategy.getId());
            }
        }

        log.info("checkStrategyUpdate end.");
        return true;
    }
}
