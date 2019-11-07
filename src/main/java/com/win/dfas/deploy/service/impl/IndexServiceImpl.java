package com.win.dfas.deploy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.win.dfas.deploy.dto.StatisticsDTO;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.IndexService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 IndexServiceImpl
 * @类描述 首页控制台实现类
 * @创建人 heshansen
 * @创建时间 2019/11/06 11:03
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppModuleService appModuleService;

    @Override
    public StatisticsVO getTaskStatistics() {
        StatisticsDTO dto = this.taskService.getStatisticsInfo();
        return toStatisticsVO(dto);
    }

    @Override
    public StatisticsVO getAppInstanceStatistics() {
        StatisticsDTO dto = this.appModuleService.getStatisticsInfo();
        return toStatisticsVO(dto);
    }


    private StatisticsVO toStatisticsVO(StatisticsDTO dto){
        StatisticsVO vo = new StatisticsVO();
        if (dto!=null){
            BeanUtil.copyProperties(dto,vo);
            if (vo.getTotal().equals(0)){
                vo.setErrorPercent(0);
                vo.setWarningPercent(0);
                vo.setSuccessPercent(0);
            }else {
                vo.setSuccessPercent(100*NumberUtil.div(vo.getSuccess().floatValue(),vo.getTotal().floatValue(),2));
                vo.setErrorPercent(100*NumberUtil.div(vo.getError().floatValue(),vo.getTotal().floatValue(),2));
                vo.setWarningPercent(100*NumberUtil.div(vo.getWarning().floatValue(),vo.getTotal().floatValue(),2));
            }
        }
        return vo;
    }
}
