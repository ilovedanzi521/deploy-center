package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.win.dfas.deploy.common.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    private static final long serialVersionUID = 3073640331832451270L;
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
    @NotBlank(message = "ip地址不能为空！",groups = AddGroup.class)
    private String ipAddress;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空！",groups = AddGroup.class)
    private String userName;
    /**
     * 端口
     */
    @NotNull(message = "端口不能为空！",groups = AddGroup.class)
    private Integer port;
    /**
     * os操作系统类型
     */
    @NotBlank(message = "操作系统不能为空！",groups = AddGroup.class)
    private String osType;
    /**
     * 连接状态
     */
    private Integer status;

    /**
     * 设备描述
     */
    private String desc;


    public String toSimpleString() {
       return name+","+ipAddress+","+alias;
    }
}
