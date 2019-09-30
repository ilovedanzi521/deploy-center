package com.win.dfas.deploy.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.bean.DeployEnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @包名 com.win.dfas.deploy.schedule.SchduleContext
 * @类名 ScheduleContext
 * @类描述 对远程机器执行远程动作的上下文Context
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
public class ScheduleContext {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleContext.class);

    private AppManager mAppManager;

    private DeployEnvConfig mEnvConfig;

    private DevicePO mHost;

    /**
     * 通用命令执行脚本
     */
    private final static String sCommShell = "shell.sh";

    public ScheduleContext(AppManager app, DeployEnvConfig env, DevicePO device) {
       this.mAppManager = app;
       this.mEnvConfig = env;
       this.mHost = device;
    }
    /**
     * 测试远程机器是否能正常连接
     * @return
     *      hostname - 返回主机名字
     */
    public String devConnect() {
        String command = getSshHeadStr()+mEnvConfig.getHomeDir()+"/"+sCommShell;
        String[] params = {"hostname"};
        logger.info("devConnect command: "+command+" params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("devConnect return: \n" + resultStr);
        return resultStr;
    }

    /**
     * 获取ssh远程执行命令头部
     * @return
     */
    private String getSshHeadStr() {
        return  "ssh -p "+mHost.getPort()+" "+mHost.getIpAddress()+" ";
    }

    public String packPath(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = getSshHeadStr()+remoteShellPath;
        String[] params = {"packPath"};
        logger.info("packPath command: "+command+" params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("packPath return: \n" + resultStr);
        return resultStr;
    }

   public String getModuleShellPath(String moduleName) {
        AppModulePO module = mAppManager.getModuleByName(moduleName);
        String shellPath = mEnvConfig.getHomeDir()+"/modules/"+module.getPath();
        return shellPath;
    }

    public String getStrategyShellPath(String strategyName) {
        StrategyPO strategy = mAppManager.getStrategyByName(strategyName);
        String shellPath = mEnvConfig.getHomeDir()+"/scripts/"+strategy.getPath();
        return shellPath;
    }

    /**
     * 查询远程机器的服务状态
     * @return
     *      0   -  服务未运行
     *      PID -  服务已运行(PID>0)
     *      <0  -  异常
     */
    public int moduleStatus(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = getSshHeadStr()+remoteShellPath;

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        String[] params = {"status",
                "--PACK_DIR="+module.getPack_dir(),
                "--PACK_VER="+module.getPack_ver(),
                "--PACK_FILE="+module.getPack_file()};
        logger.info("moduleStatus command: " + command + " params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("moduleStatus return: \n" + resultStr);

        String[] arrayStr = StrUtil.split(resultStr, "\n");
        if(arrayStr != null && arrayStr.length >= 1) {
            return Integer.parseInt(arrayStr[0]);
        }
        return -1;
    }

    /**
     * 启动远程服务
     */
    public void moduleStart(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = getSshHeadStr()+remoteShellPath;

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        String[] params = {"start",
                "--PACK_DIR="+module.getPack_dir(),
                "--PACK_VER="+module.getPack_ver(),
                "--PACK_FILE="+module.getPack_file()};
        logger.info("moduleStart command: " + command + " params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("moduleStart return: \n" + resultStr);
    }

    /**
     * 停止远程服务
     */
    public void moduleStop(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = getSshHeadStr()+remoteShellPath;

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        String[] params = {"stop",
                "--PACK_DIR="+module.getPack_dir(),
                "--PACK_VER="+module.getPack_ver(),
                "--PACK_FILE="+module.getPack_file()};
        logger.info("moduleStop command: " + command + " params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("moduleStop return: \n" + resultStr);
    }

    public void strategyExecuteJavaMs(String caller, Long strategyID, Long javaMsModuleID, Long jdkModuleID) {
        AppModulePO javaMsModule = mAppManager.getModuleById(javaMsModuleID);
        AppModulePO jdkModule = mAppManager.getModuleById(jdkModuleID);
        StrategyPO strategy = mAppManager.getStrategyById(strategyID);

        strategyExecuteJavaMs(caller, strategy, javaMsModule, jdkModule);
    }

    public void strategyExecuteJavaMs(String caller, String strategyName, String javaMsModuleName, String jdkModuleName) {
        StrategyPO strategy = mAppManager.getStrategyByName(strategyName);
        AppModulePO jdkModule = mAppManager.getModuleByName(jdkModuleName);
        AppModulePO jmsModule = mAppManager.getModuleByName(javaMsModuleName);

        strategyExecuteJavaMs(caller, strategyName, javaMsModuleName, jdkModuleName);
    }

    /**
     * 执行策略调用其函数
     * params:
     *      caller - 调用的函数名： "deploy", "undeploy"
     *      strategyName - 策略名称
     *      javaMsModuleName - java服务模块名
     *      jdkModuleName - jdk模块名
     */
    public void strategyExecuteJavaMs(String caller, StrategyPO strategy, AppModulePO jmsModule, AppModulePO jdkModule) {
        if(StrUtil.isEmpty(caller) || strategy==null || jmsModule==null || jdkModule==null) {
           logger.error("strategyExecuteJavaMs parameter error: caller="+caller+
                   "strategy="+strategy+
                   "jmsModule="+jmsModule+
                   "jdkModule="+jdkModule);
           return;
        }
        String remoteShellPath = getModuleShellPath(strategy.getName());
        String command = getSshHeadStr()+remoteShellPath;

        // 创建该模块保存的日志目录
        String logDir = mEnvConfig.getHomeDir()+"/logs/"+strategy.getName();
        String logFilename = logDir+"/"+strategy.getName();
        String[] mkParams = {"-p", logDir };
        envExecShell("mkdir", mkParams);

        String[] params = { caller,
                "--PACK_DIR="+jmsModule.getPack_dir(),
                "--PACK_VER="+jmsModule.getPack_ver(),
                "--PACK_FILE="+jmsModule.getPack_file(),
                "--JDK_DIR="+jdkModule.getPack_dir(),
                "--JDK_VER="+jdkModule.getPack_ver(),
                "--JDK_FILE="+jdkModule.getPack_file()
        };
        logger.info("strategy java_ms "+caller+" command: " + command + " params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("strategy java_ms "+caller+" return: \n" + resultStr);

        // 写日志文件
        logger.info("strategy java_ms "+caller+" write logs to file: "+logFilename);
        File logFile = FileUtil.writeString(resultStr, logFilename, "utf-8");
    }

    /**
     * 获取策略中已使用的模块
     * 注意：在本地执行即可，不用远程执行
     */
    public String[] strategyListModules(String strategyName) {
        String remoteShellPath = getModuleShellPath(strategyName);
        String command = remoteShellPath;

        StrategyPO strategy = mAppManager.getStrategyByName(strategyName);
        String[] params = {"list_modules" };
        logger.info("moduleStop command: " + command + " params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("moduleStop return: \n" + resultStr);

        String[] arrayStr = StrUtil.split(resultStr, "\n");
        return arrayStr;
    }

    /**
     * 查看策略日志
     * @param strategyId
     * @return
     */
    public String scriptViewLog(String strategyId) {
        return "";
    }

    public static String envExecShell(String command, String[] params) {
        logger.info(command, params.toString());
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            Process p = Runtime.getRuntime().exec(command, params);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            sb = new StringBuffer(1024);
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            logger.info(sb.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }
}
