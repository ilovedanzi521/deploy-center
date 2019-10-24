package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.win.dfas.common.util.LongJsonDeserializer;
import com.win.dfas.common.util.LongJsonSerializer;
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
    private static final long serialVersionUID = -8173822571749959422L;
    /**
     * 数据库唯一id
     */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @NotNull(message = "主键ID不能为空",groups = UpdateGroup.class)
    @TableId(type = IdType.AUTO)
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
