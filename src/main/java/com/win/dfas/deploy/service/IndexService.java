package com.win.dfas.deploy.service;

import com.win.dfas.deploy.vo.response.StatisticsVO;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 IndexService
 * @类描述 首页控制台服务接口
 * @创建人 heshansen
 * @创建时间 2019/11/06 11:03
 */
public interface IndexService {
    /**
     * 获取任务统计数
     * @return
     */
    StatisticsVO getTaskStatistics();

    /**
     * 获取应用服务实例统计数
     * @return
     */
    StatisticsVO getAppInstanceStatistics();

}
