package com.win.dfas.deploy.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @包名 com.win.dfas.deploy.dto
 * @类名 StatisticsDTO
 * @类描述 统计数dto
 * @创建人 heshansen
 * @创建时间 2019/11/06 13:23
 */
@Data
public class StatisticsDTO implements Serializable {

    private static final long serialVersionUID = -7300647886911407125L;
    /**
     * 总数
     */
    private Integer total;
    /**
     * 成功数
     */
    private Integer success;
    /**
     * 失败数
     */
    private Integer error;
    /**
     * 未开始数
     */
    private Integer warning;
}
