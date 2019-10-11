package com.win.dfas.deploy.schedule.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
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
    private DeployEnvBean mEnvConfig;
    private DevicePO mHost;

    /**
     * 通用命令执行脚本
     */
    private final static String sCommShell = "shell.sh";

    public ScheduleContext(AppManager app, DeployEnvBean envCfg, DevicePO device) {
       this.mAppManager = app;
       this.mEnvConfig =  envCfg;
       this.mHost = device;
    }

    public void setDevice(DevicePO device) {
        this.mHost = device;
    }

    public DevicePO getDevice() {
        return mHost;
    }

    public AppManager getAppManager() {
        return mAppManager;
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
    public String getSshHeadStr() {
        return  "ssh -p "+mHost.getPort()+" "+mHost.getIpAddress()+" ";
    }

    /**
     * 执行模块的packPath命令,返回packPath执行的结果
     * @param moduleName
     * @return
     */
    public String packPath(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = getSshHeadStr()+remoteShellPath;
        String[] params = {"packPath"};
        logger.info("packPath command: "+command+" params: ", params.toString());

        String resultStr = envExecShell(command, params);
        logger.info("packPath return: \n" + resultStr);
        return resultStr;
    }

    /**
     * 获取模块脚本shell的绝对路径
     * @param moduleName
     * @return
     */
   public String getModuleShellPath(String moduleName) {
        AppModulePO module = mAppManager.getModuleByName(moduleName);
        String shellPath = mEnvConfig.getHomeDir()+File.separator+"modules"+File.separator+module.getPath();
        return shellPath;
    }

    /**
     * 获取策略脚本shell的绝对路径
     * @param strategyName
     * @return
     */
    public String getStrategyShellPath(String strategyName) {
        StrategyPO strategy = mAppManager.getStrategyByName(strategyName);
        String shellPath = mEnvConfig.getHomeDir()+File.separator+"scripts"+File.separator+strategy.getPath();
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

    /**
     * 返回策略执行的日志保存路径
     * @return
     */
    public String getLogPath() {
        String logDir = mEnvConfig.getLogsDir()+File.separator+mHost.getName();
        return logDir;
    }

    /**
     * 获取策略保存的日志文件路径
     * @param strategyName
     * @return
     */
    public String getLogFile(String strategyName) {
        String logFile = getLogPath()+File.separator+strategyName+".log";
        return logFile;
    }
    /**
     * 查看策略日志
     * @param strategyName
     * @return
     */
    public String scriptViewLog(String strategyName) {
        return FileUtil.readString(getLogFile(strategyName),"utf-8");
    }

    /**
     * 执行命令
     * @param command
     * @param params
     * @return
     */
    public String envExecShell(String command, String[] params) {
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
