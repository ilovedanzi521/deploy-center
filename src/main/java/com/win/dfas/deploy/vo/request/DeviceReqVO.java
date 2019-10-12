package com.win.dfas.deploy.vo.request;

import com.win.dfas.deploy.common.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @包名 com.win.dfas.deploy.vo.request
 * @类名 DeviceReqVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/11 18:04
 */
@Data
public class DeviceReqVO implements Serializable {
    private static final long serialVersionUID = 3782294739324137145L;
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
}
