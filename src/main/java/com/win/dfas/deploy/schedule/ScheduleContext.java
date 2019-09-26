package com.win.dfas.deploy.schedule;

import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.bean.DeployEnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
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
       return "";
    }

    public String packPath() {
        return "";
    }

    /**
     * 查询远程机器的服务状态
     * @return
     *      0   -  服务未运行
     *      PID -  服务已运行(PID>0)
     */
    public int moduleStatus() {
       return 0;
    }

    /**
     * 启动远程服务
     */
    public void moduleStart() {

    }

    /**
     * 停止远程服务
     */
    public void moduleStop() {

    }

    /**
     * 策略安装应用
     */
    public void scriptInstall() {

    }

    /**
     * 策略卸载应用
     */
    public void scriptUninstall() {

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
        logger.info(command, params);
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
