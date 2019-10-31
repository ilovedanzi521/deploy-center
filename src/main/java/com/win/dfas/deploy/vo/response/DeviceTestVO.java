package com.win.dfas.deploy.vo.response;

import com.win.dfas.deploy.common.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 DeviceTestVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/30 20:40
 */
@Data
public class DeviceTestVO {
    /**
     * ip地址
     */
    @NotBlank(message = "ip地址不能为空！")
    private String ipAddress;
    /**
     * 用户名
     */
//    @NotBlank(message = "用户名不能为空！")
    private String userName;
    /**
     * 端口
     */
//    @NotNull(message = "端口不能为空！")
    private Integer port;
}
