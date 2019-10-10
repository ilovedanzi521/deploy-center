package com.win.dfas.deploy.schedule.context;

import com.win.dfas.deploy.po.AppModulePO;

import java.util.List;

/**
 * 各种策略需要实现的接口
 */
public interface StrategyInterface {
    /**
     * 该策略是否安装Java微服务模块
     * @return
     */
    public boolean isJavaMicroService();

    /**
     * 该策略是否需要用jdk
     * @return
     */
    public boolean isUseJdk();

    /**
     * 启动部署
     * @return
     */
    public boolean deploy();

    /**
     * 启动卸载
     * @return
     */
    public boolean undeploy();

   /**
     * 设置策略依赖模块
     * @param moduleList
     */
    public void setListModules(List<AppModulePO> moduleList);

    /**
     * 获取策略所依赖的模块
     * @return
     */
    public List<AppModulePO> list_modules();
}
