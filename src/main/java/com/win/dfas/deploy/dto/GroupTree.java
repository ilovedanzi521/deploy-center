package com.win.dfas.deploy.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

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
     * 用户名
     */
    private String userName;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 设备连接状态
     */
    private Integer status;
    /**
     * 描述
     */
    private String desc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 子节点
     */
    private List<GroupTree> children;

}
