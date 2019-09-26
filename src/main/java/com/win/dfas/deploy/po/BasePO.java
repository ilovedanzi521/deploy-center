package com.win.dfas.deploy.po;

import lombok.Data;

import javax.annotation.PropertyKey;
import java.io.Serializable;
import java.util.Date;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 BasePO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/23 10:06
 */
@Data
public class BasePO implements Serializable {
    private Long id;
    private Date createTime;
    private Date updateTime;
}
