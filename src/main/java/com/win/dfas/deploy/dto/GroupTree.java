package com.win.dfas.deploy.dto;

import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.dto
 * @类名 GroupTree
 * @类描述 设备组树
 * @创建人 heshansen
 * @创建时间 2019/10/08 10:37
 */
@Data
public class GroupTree {
    private Long id;
    /**
     * 组（机器）名称
     */
    private String name;
    /**
     * 设备ip地址
     */
    private String ipAddress;
    /**
     * 设备os操作系统类型
     */
    private String osType;
    /**
     * 设备连接状态
     */
    private Integer status;
    /**
     * 组描述
     */
    private String desc;

    /**
     * 子节点
     */
    private GroupTree children;

}
