package com.win.dfas.deploy.po;

import com.win.dfas.deploy.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 BasePO
 * @类描述 持久对象基础PO
 * @创建人 heshansen
 * @创建时间 2019/09/23 10:06
 */
@Data
public class BasePO implements Serializable {
    /**
     * 数据库唯一id
     */
    @NotNull(message = "主键ID不能为空",groups = UpdateGroup.class)
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
