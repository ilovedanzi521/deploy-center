package com.win.dfas.deploy.dto;

import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.GroupPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.bo
 * @类名 DeviceGroupDTO
 * @类描述 设备组BO
 * @创建人 heshansen
 * @创建时间 2019/10/09 10:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceGroupDTO extends GroupPO {

    private static final long serialVersionUID = -4267496105279989533L;

    /**
     * 设备列表
     */
    private List<DevicePO> devices;
}
