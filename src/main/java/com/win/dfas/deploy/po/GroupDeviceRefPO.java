package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 GroupDeviceRefPO
 * @类描述 设备组与设备关联表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:24
 */
@Data
@TableName("dc_group_device_ref")
public class GroupDeviceRefPO {
    /**
     * 设备组id
     */
    private Long groupId;
    /**
     * 设备id
     */
    private Long deviceId;
}
