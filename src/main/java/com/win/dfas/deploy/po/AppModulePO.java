package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 AppModulePO
 * @类描述 应用模块表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:38
 */
@Data
@TableName("dc_app_module")
public class AppModulePO extends BasePO{

    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 版本
     */
    private String version;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 帮助信息
     */
    private String help;
}