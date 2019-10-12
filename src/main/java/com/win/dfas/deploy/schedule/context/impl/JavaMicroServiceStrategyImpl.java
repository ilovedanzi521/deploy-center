package com.win.dfas.deploy.schedule.context.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.schedule.context.StrategyInterface;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java Micro Service的策略实现类,不同策略需要的模块不同，
 * 部署需要的参数也不同，所以分别建立实现类,模块依赖通过list_modules函数输出。
 * 该类需要两个模块支持：
 *  1. Java 微服务的安装模块，定义为: mJavaMsModule
 *  2. Java sdk安装模块, 定义为: mJavaModule
 */
@Slf4j
public class JavaMicroServiceStrategyImpl implements StrategyInterface {

    private AppManager mAppManager;
    private ScheduleContext mContext;

    private StrategyPO mStrategy;
    private AppModulePO mJavaMsModule;
    private AppModulePO mJavaModule;

    public JavaMicroServiceStrategyImpl(ScheduleContext context) {
        this(context, null);
    }

    public JavaMicroServiceStrategyImpl(ScheduleContext context, StrategyPO strategy) {
        this.mContext = context;
        mAppManager = SpringContextUtils.getBean("app_manager", AppManager.class);
        setStrategy(strategy);
    }

    @Override
    public boolean isUseJdk() {
        return true;
    }

    @Override
    public boolean isJavaMicroService() {
        return true;
    }

    @Override
    public void setListModules(List<AppModulePO> moduleList) {
        int moduleTotal = moduleList.size();
        for(int i=0; i<moduleTotal; i++) {
            AppModulePO module = moduleList.get(i);
            if("Java-SDK".equalsIgnoreCase(module.getName()) && isUseJdk()) {
                setJavaModule(module);
            }
            // NOTE: 暂仅支持一个javasdk和一个java 微服务的安装
            if(isJavaMicroService()) {
                setJavaMicroServiceModule(module);
            }
        }
    }


    public void setStrategy(StrategyPO strategy) {
        this.mStrategy = strategy;
    }

    public void setJavaMicroServiceModule(AppModulePO javaMsModule) {
        this.mJavaMsModule = javaMsModule;
    }

    public void setJavaModule(AppModulePO javaModule) {
        this.mJavaModule = javaModule;
    }

    @Override
    public boolean deploy() {
        String caller = "deploy";
        return strategyExecuteJavaMs(caller, mStrategy, mJavaMsModule, mJavaModule);
    }

    @Override
    public boolean undeploy() {
        String caller = "undeploy";
        return strategyExecuteJavaMs(caller, mStrategy, mJavaMsModule, mJavaModule);
    }

    /**
     * 获取策略中需使用的模块
     * 注意：在本地执行即可，不用远程执行
     */
    @Override
    public List<AppModulePO> list_modules() {
        String remoteShellPath = mContext.getStrategyShellPath(mStrategy.getPath());
        String command = remoteShellPath;

        String caller ="list_modules";
        String[] params = { command, "list_modules" };
        log.info("exec list_modules: [" + command + "] params: "+ Arrays.toString(params));

        List<String> resultList = RuntimeUtil.execForLines(params);
        //String resultStr = mContext.envExecShell(command, params);
        log.info("exec "+Arrays.toString(params)+" result ===> ");
        for(int i=0; i<resultList.size(); i++) {
            log.info(resultList.get(i));
        }

        List<AppModulePO> moduleObjList = new ArrayList<AppModulePO>();
        int total = resultList.size();
        for(int i=0; i<total; i++) {
            String path = resultList.get(i);
            AppModulePO module = mAppManager.getModuleByPath(path);
            moduleObjList.add(module);
        }
        return moduleObjList;
    }

    /**
     * 执行JavaMs的策略调用
     * @param caller - 调用的函数名: "deploy", "undeploy"
     * @param strategyID - 策略ID
     * @param javaMsModuleID - JavaMs模块ID
     * @param jdkModuleID - JDK模块ID
     */
    public boolean strategyExecuteJavaMs(String caller, Long strategyID, Long javaMsModuleID, Long jdkModuleID) {
        AppModulePO javaMsModule = mAppManager.getModuleById(javaMsModuleID);
        AppModulePO jdkModule = mAppManager.getModuleById(jdkModuleID);
        StrategyPO strategy = mAppManager.getStrategyById(strategyID);

        return strategyExecuteJavaMs(caller, strategy, javaMsModule, jdkModule);
    }

    /**
     * 执行JavaMs的策略调用
     * @param caller - 调用的函数名: "deploy", "undeploy"
     * @param strategyName - 策略名
     * @param javaMsModuleName - JavaMs模块名
     * @param jdkModuleName - JDK模块名
     */
    public boolean strategyExecuteJavaMs(String caller, String strategyName, String javaMsModuleName, String jdkModuleName) {
        StrategyPO strategy = mAppManager.getStrategyByName(strategyName);
        AppModulePO jdkModule = mAppManager.getModuleByName(jdkModuleName);
        AppModulePO jmsModule = mAppManager.getModuleByName(javaMsModuleName);

        return strategyExecuteJavaMs(caller, strategyName, javaMsModuleName, jdkModuleName);
    }

    /**
     * 执行策略调用其函数
     * params:
     *      @param caller - 调用的函数名： "deploy", "undeploy"
     *      @param strategy  - 策略名称
     *      @param jmsModule - java服务模块名
     *      @param jdkModule - jdk模块名
     */
    public boolean strategyExecuteJavaMs(String caller, StrategyPO strategy, AppModulePO jmsModule, AppModulePO jdkModule) {
        if(StrUtil.isEmpty(caller) || strategy==null || jmsModule==null || jdkModule==null) {
            log.error("strategyExecuteJavaMs parameter error: caller="+caller+
                    " strategy="+strategy+
                    " jmsModule="+jmsModule+
                    " jdkModule="+jdkModule);
            return false;
        }
        String remoteShellPath = mContext.getStrategyShellPath(strategy.getPath());
        String command = mContext.getSshHeadStr()+remoteShellPath;

        // 创建该模块保存的日志目录
        String logDir = mContext.getLogPath();
        String logFilename = mContext.getLogFile(strategy.getName());
        String[] mkParams = {"-p", logDir};
        mContext.envExecShell("mkdir", mkParams);

        String[] params = { caller,
                "--PACK_DIR=" +jmsModule.getPack_dir(),
                "--PACK_VER=" +jmsModule.getPack_ver(),
                "--PACK_FILE="+jmsModule.getPack_file(),
                "--JDK_DIR="  +jdkModule.getPack_dir(),
                "--JDK_VER="  +jdkModule.getPack_ver(),
                "--JDK_FILE=" +jdkModule.getPack_file()
        };
        log.info("strategy java_ms "+caller+" command: " + command + " params: ", params.toString());

        改用Runtime.exec命令
        String resultStr = mContext.envExecShell(command, params);
        log.info("strategy java_ms "+caller+" return: \n" + resultStr);

        // 写日志文件
        log.info("strategy java_ms "+caller+" write logs to file: "+logFilename);
        File logFile = FileUtil.writeString(resultStr, logFilename, "utf-8");
        return true;
    }
}
