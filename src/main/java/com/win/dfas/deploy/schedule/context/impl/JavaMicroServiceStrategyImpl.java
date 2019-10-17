package com.win.dfas.deploy.schedule.context.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.schedule.context.StrategyFactory;
import com.win.dfas.deploy.schedule.context.StrategyInterface;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java Micro Service的策略实现类,不同策略需要的模块不同，
 * 部署需要的参数也不同，所以分别建立实现类,模块依赖通过list_modules函数输出。
 * 该类需要两个模块支持：
 *  1. Java 微服务的安装模块，定义为: mJavaMsModule
 *  2. Java sdk安装模块, 定义为: mJdkModule
 */
@Slf4j
public class JavaMicroServiceStrategyImpl implements StrategyInterface {
    private final static String TAG = "JavaMicroServiceStrategyImpl";
    private AppManager mAppManager;
    private ScheduleContext mContext;

    private StrategyPO mStrategy;
    private AppModulePO mJavaMsModule;
    private AppModulePO mJdkModule;

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

    public void setStrategy(StrategyPO strategy) {
        this.mStrategy = strategy;
    }

    public void setJavaMicroServiceModule(AppModulePO javaMsModule) {
        this.mJavaMsModule = javaMsModule;
    }

    public void setJavaModule(AppModulePO javaModule) {
        this.mJdkModule = javaModule;
    }

    public AppModulePO getJavaModule() {
        return this.mJdkModule;
    }

    @Override
    public boolean deploy() {
        String caller = "deploy";
        return strategyExecuteJavaMs(caller, mStrategy, mJavaMsModule, mJdkModule);
    }

    @Override
    public boolean undeploy() {
        String caller = "undeploy";
        return strategyExecuteJavaMs(caller, mStrategy, mJavaMsModule, mJdkModule);
    }

    @Override
    public void setListModules(List<AppModulePO> moduleList) {
        int moduleTotal = moduleList.size();
        for(int i=0; i<moduleTotal; i++) {
            AppModulePO module = moduleList.get(i);
            if(isUseJdk() && getJavaModule() == null) {
                AppModulePO jdkModule = StrategyFactory.getJavaSdkModule(StrategyFactory.JDK_MODULE_NAME);
                setJavaModule(jdkModule);
            }
            // NOTE: 暂仅支持一个javasdk和一个java 微服务的安装
            else if(isJavaMicroService()) {
                setJavaMicroServiceModule(module);
            }
        }
    }

    /**
     * 获取策略中需使用的模块
     * 注意：在本地执行即可，不用远程执行
     */
    @Override
    public List<AppModulePO> list_modules() {
        DevicePO host = mContext.getDevice();
        String remoteShellPath = mContext.getStrategyShellPath(mStrategy.getPath());
        String command = remoteShellPath;

        String caller ="list_modules";
        String[] params = {
                "ssh",
                "-p",
                String.valueOf(host.getPort()),
                host.getIpAddress(),
                command,
                caller
        };
        log.info("exec list_modules: [" + command + "] params: "+ Arrays.toString(params));

        List<String> resultList = ShellUtils.envExecShell(params);
        ShellUtils.listLog(log, resultList);

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
        } else {
            log.info(strategy.toString());
            log.info(jmsModule.toString());
            log.info(jdkModule.toString());
        }

        // 创建该模块保存的日志目录, 日志目录在本地创建
        String logDir = mContext.getLogDir();
        String logFilename = mContext.getLogFile(strategy.getName());
        String[] mkParams = {"mkdir", "-p", logDir};
        log.info(TAG+" exec "+Arrays.toString(mkParams));

        List<String> mkResults = RuntimeUtil.execForLines(mkParams);
        FileUtil.writeLines(mkResults, logFilename, "utf-8");

        DevicePO host = mContext.getDevice();
        String remoteShellPath = mContext.getStrategyShellPath(strategy.getPath());
        String command = remoteShellPath;
        String[] scriptParams = {
                "ssh",
                "-p",
                String.valueOf(host.getPort()),
                host.getIpAddress(),
                command,
                caller,
                "--PACK_DIR=" +jmsModule.getPackDir(),
                "--PACK_VER=" +jmsModule.getPackVer(),
                "--PACK_FILE="+jmsModule.getPackFile(),
                "--JDK_DIR="  +jdkModule.getPackDir(),
                "--JDK_VER="  +jdkModule.getPackVer(),
                "--JDK_FILE=" +jdkModule.getPackFile()
        };
        log.info(TAG+" exec "+Arrays.toString(scriptParams));

        //改用Runtime.exec命令
        ShellUtils.envExecShell(logFilename, scriptParams);

        log.info(TAG+" strategy java_ms "+caller+" write logs to file: "+logFilename);
        return true;
    }
}
