package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 GroupPO
 * @类描述 设备组表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:18
 */
@Data
@TableName("dc_group")
public class GroupPO extends BasePO {

    private static final long serialVersionUID = -3030340942837664507L;
    /**
     * 设备组名
     */
    private String name;
    /**
     * 设备组描述
     */
    private String desc;
}
