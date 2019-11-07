package com.win.dfas.deploy.controller;

import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.service.IndexService;
import com.win.dfas.deploy.vo.response.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 IndexController
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/11/06 11:00
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/statistics/task")
    public WinResponseData taskStatistics(){
        StatisticsVO statisticsVO = this.indexService.getTaskStatistics();
        return WinResponseData.handleSuccess(statisticsVO);
    }
    @GetMapping("/statistics/appInstance")
    public WinResponseData appInstanceStatistics(){
        StatisticsVO statisticsVO = this.indexService.getAppInstanceStatistics();
        return WinResponseData.handleSuccess(statisticsVO);
    }

}
