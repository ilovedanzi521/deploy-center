/* @win_message_box组件
 * @Author: lixiuquan
 * @Date: 2019-06-13 14:01:33
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-08-23 14:48:22
 */

import { MessageBox } from "element-ui";
import "./style/message.scss";

/**
 * 提示框方法提供
 * @param msg 消息内容
 * @param title 标题（可为空）
 * @param htmlAble 是否转义成html（是传true）可不传
 * @param confirmText 确定按钮内容（可为空默认为确定）
 * @param cancelText 取消关闭按钮内容（可为空默认为取消）
 */
let highMessage = type => {
    return (
        msg: any,
        title: string,
        htmlAble: boolean = false,
        confirmText: string = "确定",
        cancelText: string = "取消"
    ) => {
        return MessageBox.confirm(msg, title, {
            confirmButtonText: confirmText,
            cancelButtonText: cancelText,
            dangerouslyUseHTMLString: htmlAble,
            type,
            center: true
        });
    };
};

export let win_message_box_success = highMessage("success");

export let win_message_box_error = highMessage("error");

export let win_message_box_info = highMessage("info");

export let win_message_box_warning = highMessage("warning");
