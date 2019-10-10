package com.win.dfas.deploy.common.exception;

/**
 * @包名 com.win.dfas.deploy.common.exception
 * @类名 ValidateException
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/08 17:15
 */
public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
