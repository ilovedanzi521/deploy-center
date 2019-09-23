package com.win.dfas.deploy.modules.sys.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @包名 com.win.dfas.deploy.modules.sys.po
 * @类名 SysUserPO
 * @类描述 用户信息
 * @创建人 heshansen
 * @创建时间 2019/09/23 10:03
 */
@Data
@TableName("sys_user")
public class SysUserPO extends BasePO {
    private static final long serialVersionUID = 4713443664130519061L;
    private String name;
    private String password;
    private Integer status;
}
