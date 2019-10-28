package com.win.dfas.deploy.service;

import com.win.dfas.deploy.po.DevicePO;

import java.io.IOException;
import java.util.List;

public interface ScheduleCenterService {
    /**
     * 启动部署
     * @param id - TaskID
     */
    void deploy(long id);

    /**
     * 卸载部署
     * @param id - TaskID
     */
    void undeploy(long id);

    /**
     * 获取部署状态
     * @param id
     * @return
     */
    int getDeployStatus(long id);

    /**
     * 根据ipAddr获取Device对象
     * @param ipAddr
     * @return
     */
    DevicePO getDevice(String ipAddr);

    /**
     * 连接设备,检测远程设备是否打通ssh免密
     * @param dev
     * @return
     */
    List<String> connectDevice(DevicePO dev);

    /**
     * 新增设备后需要向调度中心添加，否则需要重启加载
     * @param dev
     */
    void addDevice(DevicePO dev);

    /**
     * 删除设备后需要向调度中心删除，否则需要重启加载
     * @param dev
     */
    void delDevice(DevicePO dev);

    /**
     * 当升级安装包后，需要调用该函数重新扫描应用和策略表，否则要重启
     */
    void appSourceScan();

    /**
     * 检查模块当前状态
     * @param ipAddr
     * @param moduleName
     * @return
     *      pid - 服务已启动
     *      <=0 - 服务未启动
     */
    int moduleStatus(String ipAddr, String moduleName);

    /**
     * 启动当前模块
     * @param ipAddr
     * @param moduleName
     * @return
     *      true  - 已启动 pid>0
     *      false - 未启动
     */
    boolean moduleStart(String ipAddr, String moduleName);
    /**
     * 停止当前模块
     * @param ipAddr
     * @param moduleName
     * @return
     *      true  - 已停止
     *      false - 未停止
     */
    boolean moduleStop(String ipAddr, String moduleName);

    /**
     * 解压文件到仓库
     *
     * @param zipFile
     * @param repoDir
     * @return
     */
    Boolean upgradAppModule(String zipFile, String repoDir) throws IOException;
}
