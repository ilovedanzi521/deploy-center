package com.win.dfas.deploy.service;

import com.win.dfas.deploy.po.DevicePO;

import java.util.List;

public interface ScheduleCenterService {
    /**
     * 启动部署
     * @param id - TaskID
     */
    public void deploy(long id);

    /**
     * 卸载部署
     * @param id - TaskID
     */
    public void undeploy(long id);

    /**
     * 获取部署状态
     * @param id
     * @return
     */
    public int getDeployStatus(long id);

    /**
     * 根据ipAddr获取Device对象
     * @param ipAddr
     * @return
     */
    public DevicePO getDevice(String ipAddr);

    /**
     * 连接设备,检测远程设备是否打通ssh免密
     * @param dev
     * @return
     */
    public List<String> connectDevice(DevicePO dev);

    /**
     * 新增设备后需要向调度中心添加，否则需要重启加载
     * @param dev
     */
    public void addDevice(DevicePO dev);

    /**
     * 删除设备后需要向调度中心删除，否则需要重启加载
     * @param dev
     */
    public void delDevice(DevicePO dev);

    /**
     * 当升级安装包后，需要调用该函数重新扫描应用和策略表，否则要重启
     */
    public void upgraded();

    public int moduleStatus(DevicePO dev, String moduleName);

    public void moduleStart(DevicePO dev, String moduleName);

    public void moduleStop(DevicePO dev, String moduleName);

}
