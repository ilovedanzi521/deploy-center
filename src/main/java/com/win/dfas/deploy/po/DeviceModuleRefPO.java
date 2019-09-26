package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 DeviceModuleRefPO
 * @类描述 机器模块关联表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:53
 */
@Data
@TableName("dc_device_module_ref")
public class DeviceModuleRefPO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 应用模块id
     */
    private Long moduleId;
}
