package com.win.dfas.deploy.common.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.common.exception.UserInvalidException;
import com.win.dfas.deploy.common.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @包名 com.win.dfas.deploy.common.handler
 * @类名 GlobalExceptionHandler
 * @类描述 全局异常统一处理类
 * @创建人 heshansen
 * @创建时间 2019/09/23 11:26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 顶级异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public WinResponseData exceptionHandle(Exception e){
        log.error("未知系统异常："+e.getMessage(),e);
        return WinResponseData.handleError("未知系统异常："+e.getMessage());
    }

    //************************自定义业务异常**************************

    @ExceptionHandler(BaseException.class)
    public WinResponseData baseExceptionHandle(BaseException e){
        log.error("后台异常："+e.getMessage(),e);
        return WinResponseData.handleError("后台异常："+e.getMessage());
    }
    /**
     * 用户验证异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(UserInvalidException.class)
    public WinResponseData userInvalidExceptionHandler(HttpServletRequest request, UserInvalidException ex) {

        log.error("url = {}, errMsg ={}", request.getRequestURL(), ExceptionUtil.stacktraceToString(ex));

        return WinResponseData.handleError(ex.getMessage());
    }
    @ExceptionHandler(ValidateException.class)
    public WinResponseData ValidateExceptionHandler(ValidateException e){

        return WinResponseData.handleError("验证异常："+e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public WinResponseData duplicateKeyExceptionHandler(DuplicateKeyException e){
        log.error(e.getMessage(),e);
        return WinResponseData.handleError("数据已经存在："+e.getMessage());
    }

}
