package com.win.dfas.deploy.vo.response;

import com.win.dfas.deploy.po.*;
import lombok.Data;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 TaskDetailVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/29 13:47
 */
@Data
public class TaskDetailVO extends TaskPO {
    /**
     * 策略信息
     */
    private String strategyName;
    /**
     * 设备组信息
     */
    private String groupName;
    private String logPath;
    private List<String> logInfo;
    private List<AppModulePO> appModules;
    private List<DevicePO> devices;
}
