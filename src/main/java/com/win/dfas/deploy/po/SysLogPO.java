package com.win.dfas.deploy.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @包名 com.win.dfas.deploy.po
 * @类名 SysLogPO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/23 16:57
 */
@Data
@TableName("sys_log")
public class SysLogPO extends BasePO {
    private static final long serialVersionUID = 5134537697706060764L;
    //用户名
    private String username;
    //用户操作
    private String operation;
    //请求方法
    private String method;
    //请求参数
    private String params;
    //执行时长(毫秒)
    private Long time;
    //IP地址
    private String ip;
}
