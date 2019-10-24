package com.win.dfas.deploy.dto;

import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import lombok.Data;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.bo
 * @类名 AppModuleDTO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/18 13:27
 */
@Data
public class AppModuleDTO extends AppModulePO {
    private static final long serialVersionUID = -5932600356013745543L;
    private List<DevicePO> devices;
}
