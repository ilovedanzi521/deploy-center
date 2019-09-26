package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 DevicePO
 * @类描述 设备表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 11:50
 */
@Data
@TableName("dc_devcie")
public class DevicePO extends BasePO{

    /**
     * 机器名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 端口
     */
    private Integer port;
    /**
     * os操作系统类型
     */
    private String osType;
    /**
     * 连接状态
     */
    private Integer status;

}
