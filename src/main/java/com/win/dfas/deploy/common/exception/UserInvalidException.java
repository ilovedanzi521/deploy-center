/****************************************************
 * 创建人：  @author zhoujinyin    
 * 创建时间: 2019/6/1/11:53
 * 项目名称：dfas-auth-center
 * 文件名称: UserInvalidException
 * 文件描述: @Description: 用户名密码无效异常
 *
 * All rights Reserved, Designed By 投资交易团队
 * @Copyright:2016-2019
 *
 ********************************************************/
package com.win.dfas.deploy.common.exception;

/**
 * 包名称：com.win.dfas.auth.common.exception.auth
 * 类名称：UserInvalidException
 * 类描述：用户名密码无效异常
 * 创建人：@author zhoujinyin
 * 创建时间：2019/6/1/11:53
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, 500);
    }
}
