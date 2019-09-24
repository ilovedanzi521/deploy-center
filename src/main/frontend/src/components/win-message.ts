/* @win_message组件
 * @Author: lixiuquan
 * @Date: 2019-06-13 14:01:33
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-09-06 09:38:31
 */

import { Message } from "element-ui";
import "./style/message.scss";

/**
 * 成功消息提示方法
 * @param msg 提示消息
 */
export function win_message_success(msg: any) {
    return Message.success({
        showClose: true,
        message: msg
    });
}

/**
 * 警告消息提示方法
 * @param msg 提示消息
 */
export function win_message_warning(msg: any) {
    return Message.warning({
        showClose: true,
        message: msg
    });
}

/**
 * 错误消息提示方法
 * @param msg 提示消息
 */
export function win_message_error(msg: any) {
    return Message.error({
        showClose: true,
        message: msg
    });
}

/**
 * 正常消息提示方法
 * @param msg 提示消息
 */
export function win_message_info(msg: any) {
    return Message.info({
        showClose: true,
        message: msg
    });
}
