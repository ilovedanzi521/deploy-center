package com.win.dfas.deploy.po;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 AppModulePO
 * @类描述 应用模块表PO
 * @创建人 heshansen
 * @创建时间 2019/09/26 13:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dc_app_module")
public class AppModulePO extends BasePO{

    /**
     * 名称
     */
    private String name;
    /**
     * 脚本路径
     */
    private String path;
    /**
     * 包目录，相对目录
     */
    private String packDir;
    /**
     * 包版本
     */
    private String packVer;
    /**
     * 包文件名
     */
    private String packFile;

    /**
     * 描述信息
     */
    private String desc;
    /**
     * 帮助信息
     */
    private String help;

    /**
     * 删除模块标记
     */
    private int allowDelete;

    public String toFilePathString() {
        if (StrUtil.isNotBlank(this.path) && !this.path.endsWith(File.separator)){
            return this.path+File.separator+this.name;
        }
        return this.path+this.name;
    }
}
