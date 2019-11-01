package com.win.dfas.deploy.dto;

import com.win.dfas.deploy.po.DeviceModuleRefPO;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.dto
 * @类名 DeviceModuleRefDTO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/31 15:25
 */
@Data
public class DeviceModuleRefDTO extends DeviceModuleRefPO {
    /**
     * 应用模块名
     */
    private String moduleName;
    /**
     * 机器名称
     */
    private String deviceName;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 服务状态
     */
    private Integer status;

}
