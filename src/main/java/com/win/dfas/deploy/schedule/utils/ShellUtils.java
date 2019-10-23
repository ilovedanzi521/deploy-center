package com.win.dfas.deploy.schedule.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

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
        try {
            return RuntimeUtil.execForLines(commands);
        } catch (Exception e) {
            log.error("envExecShell "+commands.toString()+" error.", e);
        }
        return null;
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

    public static Boolean unZip(String zipFile, String targetDir) {
        String[] params = {sCommShell,"unzip", "-o", zipFile, "-d", targetDir};
        log.info("command ==>: "+ Arrays.toString(params));
        List<String> resultList = ShellUtils.envExecShell(params);
        log.info(" <==: "+ Arrays.toString(resultList.toArray()));

        return isSuccess(resultList);
    }
}
