package com.win.dfas.deploy.schedule.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.schedule.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @包名 com.win.dfas.deploy.schedule.SchduleContext
 * @类名 ScheduleContext
 * @类描述 对远程机器执行远程动作的上下文Context
 * @创建人 chenji
 * @创建时间 2019/09/25
 */
@Slf4j
public class ScheduleContext {
    private final static String TAG = "ScheduleContext";
    private AppManager mAppManager;
    private DeployEnvBean mEnvConfig;
    private DevicePO mHost;
    private AtomicBoolean mIsInit = new AtomicBoolean(false);

    /**
     * 初始化远程机器环境的shell脚本
     */
    private final static String sInitShell = "init.sh";

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
     * 初始化远程设备环境
     * @return
     */
    public boolean initRemoteDevice()  {
        if (mIsInit.get() == false) {
            String command = mEnvConfig.getHomeDir() + File.separator + sInitShell;
            // 要加上ssh头部
            String[] params = {
                    command, mHost.getIpAddress(), String.valueOf(mHost.getPort())};
            List<String> resultList = ShellUtils.envExecShell(params);
            mIsInit.set(ShellUtils.isSuccess(resultList));

            if(resultList == null) {
                log.info("initRemoteDevice ===> ");
                ShellUtils.listLog(log, resultList);
            }
        }
        return mIsInit.get();
    }

    /**
     * 测试远程机器是否能正常连接
     * @return
     *      hostname - 返回主机名字
     */
    public List<String> devConnect() {
        // 如果remote host没有初始化，则调用远程命令之前必须初始化
        initRemoteDevice();
        String command = mEnvConfig.getHomeDir()+File.separator+sCommShell;
        String[] params = {
                "ssh",
                "-p",
                String.valueOf(mHost.getPort()),
                mHost.getIpAddress(),
                command,
                "hostname"
        };
        log.info("devConnect command: "+Arrays.toString(params));

        List<String> resultList = ShellUtils.envExecShell(params);
        return resultList;
    }

    /**
     * 执行模块的packPath命令,返回packPath执行的结果
     * @param moduleName
     * @return
     */
    public String packPath(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = remoteShellPath;
        String[] params = {
                "ssh",
                "-p",
                String.valueOf(mHost.getPort()),
                mHost.getIpAddress(),
                command,
                "packPath"
        };
        log.info("packPath command: "+command+" params: ", params.toString());

        // 如果remote host没有初始化，则调用远程命令之前必须初始化
        initRemoteDevice();

        List<String> resultList = ShellUtils.envExecShell(params);
        if(resultList != null && resultList.size()>0) {
            return resultList.get(0);
        }

        return "";
    }

    /**
     * 获取模块脚本shell的绝对路径
     * @param moduleName
     * @return
     */
   public String getModuleShellPath(String moduleName) {
        AppModulePO module = mAppManager.getModuleByName(moduleName);
        if(module != null && !StrUtil.isEmpty(module.getName())) {
            String shellPath = mEnvConfig.getHomeDir() + File.separator + "modules" + File.separator + module.getPath();
            return shellPath;
        }
        return "";
    }

    /**
     * 获取策略脚本shell的绝对路径
     * @param path
     * @return
     */
    public String getStrategyShellPath(String path) {
        StrategyPO strategy = mAppManager.getStrategyByPath(path);
        String shellPath = path;
        if(strategy != null && !StrUtil.isEmpty(strategy.getPath())) {
            shellPath = mEnvConfig.getHomeDir()+File.separator + "scripts" + File.separator + strategy.getPath();
        }
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
        String command = remoteShellPath;
        String caller = "status";

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        if(module == null || StrUtil.isEmpty(module.getName())) {
            return -1;
        }

        String[] params = {
                "ssh",
                "-p",
                String.valueOf(mHost.getPort()),
                mHost.getIpAddress(),
                command,
                caller,
                "--PACK_DIR="+module.getPackDir(),
                "--PACK_VER="+module.getPackVer(),
                "--PACK_FILE="+module.getPackFile()
        };
        log.info("moduleStatus command: " + command + " params: "+ Arrays.toString(params));

        // 如果remote host没有初始化，则调用远程命令之前必须初始化
        initRemoteDevice();

        List<String> resultList = ShellUtils.envExecShell(params);

        ShellUtils.listLog(log, resultList);
        boolean isSuccess = ShellUtils.isSuccess(resultList);
        int pid=0;
        if(resultList != null && resultList.size()>0) {
            try {
                pid = Integer.parseInt(resultList.get(0));
            } catch (Exception e) {
                log.error("get pid error.", e);
            }
        }

        log.info("moduleStatus return: " + isSuccess);
        return pid;
    }

    /**
     * 启动远程服务
     */
    public boolean moduleStart(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = remoteShellPath;

        // 如果remote host没有初始化，则调用远程命令之前必须初始化
        initRemoteDevice();

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        if(module == null || StrUtil.isEmpty(module.getName())) {
            return false;
        }

        String[] params = {
                "ssh",
                "-p",
                String.valueOf(mHost.getPort()),
                mHost.getIpAddress(),
                command,
                "start",
                "--PACK_DIR="+module.getPackDir(),
                "--PACK_VER="+module.getPackVer(),
                "--PACK_FILE="+module.getPackFile()
        };
        log.info("moduleStart command: " + command + " params: " + Arrays.toString(params));

        List<String> resultList = ShellUtils.envExecShell(params);

        ShellUtils.listLog(log, resultList);
        boolean isSucc = ShellUtils.isSuccess(resultList);
        return isSucc;
    }


    /**
     * 停止服务
     * @param moduleName
     * @return true - 停止成功
     *         false - 停止失败
     */
    public boolean moduleStop(String moduleName) {
        String remoteShellPath = getModuleShellPath(moduleName);
        String command = remoteShellPath;
        String caller = "stop";

        // 如果remote host没有初始化，则调用远程命令之前必须初始化
        initRemoteDevice();

        AppModulePO module = mAppManager.getModuleByName(moduleName);
        if(module == null || StrUtil.isEmpty(module.getName())) {
            return false;
        }

        String[] params = {
                "ssh",
                "-p",
                String.valueOf(mHost.getPort()),
                mHost.getIpAddress(),
                command,
                "stop",
                "--PACK_DIR="+module.getPackDir(),
                "--PACK_VER="+module.getPackVer(),
                "--PACK_FILE="+module.getPackFile()
        };
        log.info("moduleStop command: " + command + " params: " + Arrays.toString(params));

        List<String> resultList = ShellUtils.envExecShell(params);

        ShellUtils.listLog(log, resultList);
        boolean isSucc = ShellUtils.isSuccess(resultList);
        return isSucc;
    }

    /**
     * 返回策略执行的日志保存路径
     * @return
     */
    public String getLogDir() {
        String logDir = mEnvConfig.getLogsDir()+File.separator+mHost.getName();
        return logDir;
    }

    /**
     * 获取策略保存的日志文件路径
     * @param strategyName
     * @return
     */
    public String getLogFile(String strategyName) {
        String logFile = getLogDir()+File.separator+strategyName+".log";
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
}
