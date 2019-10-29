package com.win.dfas.deploy.schedule.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.schedule.AppManager;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ShellUtils {

    /**
     * 通用命令执行脚本
     */
    private final static String sCommShell = "/root/repo/shell.sh";
    /**
     * 执行shell命令，返回执行完后的日志信息
     * @param commands
     * @return
     */
    public static List<String> envExecShell(String... commands) {
        return RuntimeUtil.execForLines(commands);
    }

    /**
     * 执行shell命令，同时写文件
     * @param commands
     * @return 返回日志文件路径
     */
    public static void envExecShell(String writeLogfile, String... commands) {
        Process p = RuntimeUtil.exec(commands);
        try {
            FileUtil.writeFromStream(p.getInputStream(), writeLogfile);
        } catch (Exception e){
            log.error("envExecShell "+commands.toString()+" write log to: "+writeLogfile+" error.", e);
        }
    }

    /**
     * 根据Runtime.exec返回的List<string>,取最后一行判断是否'OK'字符串，
     * 是则返回成功
     * @param resultList
     * @return
     */
    public static boolean isSuccess(List<String> resultList) {
        if(resultList == null || resultList.size() == 0) {
            return false;
        }

        String result = resultList.get(resultList.size()-1);
        if(result == null) {
            return false;
        }
        if(result.equalsIgnoreCase("OK")) {
            return true;
        }
        return false;
    }

    /**
     * 打印shell返回的List<String>日志数据，输出到logger里面
     * @param log
     * @param logList
     */
    public static void listLog(Logger log, List<String> logList) {
        if(log == null || logList == null) {
            return;
        }
        int total = logList.size();
        for(int i=0; i<total; i++) {
            log.info(logList.get(i));
        }
    }

    public static Boolean unZip(String zipFile, String targetDir) throws IOException {
        File cmdFile = new File(sCommShell);
        if(!cmdFile.exists()){
            throw new IOException("未找到指定命令脚本："+sCommShell);
        }
        String[] params = {sCommShell,"unzip", "-o", zipFile, "-d", targetDir};
        log.info("command ==>: "+ Arrays.toString(params));
        List<String> resultList = ShellUtils.envExecShell(params);
        log.info(" <==: "+ Arrays.toString(resultList.toArray()));

        return isSuccess(resultList);
    }

    public static List<String> getAppModules(String strategyShell) {
        if (FileUtil.file(strategyShell).exists()){
            String caller ="list_modules";
            String[] params = { strategyShell, caller };
            List<String> resultList = ShellUtils.envExecShell(params);
            return resultList;
        }else {
            log.warn(strategyShell+"策略脚本文件不存在！");
            return null;
        }
    }
}
