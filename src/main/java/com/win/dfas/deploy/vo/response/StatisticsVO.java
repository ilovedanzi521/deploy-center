package com.win.dfas.deploy.vo.response;

import com.win.dfas.deploy.dto.StatisticsDTO;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 StatisticsVO
 * @类描述 统计展示VO
 * @创建人 heshansen
 * @创建时间 2019/11/06 11:13
 */
@Data
public class StatisticsVO extends StatisticsDTO {

    /**
     * 成功百分数
     */
    private double successPercent;
    /**
     * 失败百分数
     */
    private double errorPercent;
    /**
     * 未开始百分数
     */
    private double warningPercent;
}
