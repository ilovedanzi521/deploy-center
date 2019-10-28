/****************************************************
 * 创建人：  @author zhoujinyin    
 * 创建时间: 2019/5/31/18:46
 * 项目名称：dfas-auth-center
 * 文件名称: BaseException
 * 文件描述: @Description: 认证基本异常类
 *
 * All rights Reserved, Designed By 投资交易团队
 * @Copyright:2016-2019
 *
 ********************************************************/
package com.win.dfas.deploy.common.exception;

/**
 * 基础异常类
 */
public class BaseException extends RuntimeException {
    private int status = 200;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BaseException() {
    }

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
