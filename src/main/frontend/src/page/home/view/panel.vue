<template>
    <div class="pannel-container">
        <!---tabs组件--->
        <win-tabs v-model="activeName">
            <win-tab-pane label="消息提示组件" name="first">
                <div class="message">
                    <win-button :plain="true" @click="open2">成功</win-button>
                    <win-button :plain="true" @click="open3">警告</win-button>
                    <win-button :plain="true" @click="open4">错误</win-button>
                    <win-button :plain="true" @click="open1">正常</win-button>
                </div>
            </win-tab-pane>
            <win-tab-pane label="消息弹出框组件" name="second">
                <div class="message">
                    <win-button :plain="true" @click="open5">成功</win-button>
                    <win-button :plain="true" @click="open6">警告</win-button>
                    <win-button :plain="true" @click="open7">错误</win-button>
                    <win-button :plain="true" @click="open8">正常</win-button>
                </div>
            </win-tab-pane>
        </win-tabs>

        <!---formdialog组件--->
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import BaseController from "../../common/controller/BaseController";

@Component
export default class Panel extends BaseController {
    activeName: string = "first";
    centerDialogVisible: boolean = true;
    dialogFormVisible: boolean = true;
    checkList: string[] = ["选中且禁用", "复选框 A"];
    data: any[] = [
        {
            id: 1,
            label: "A-赢和基金管理公司",
            children: [
                {
                    id: 4,
                    label: "A1-固收投资部",
                    children: [
                        {
                            id: 9,
                            label: "投资总监"
                        },
                        {
                            id: 10,
                            label: "投资总监"
                        }
                    ]
                }
            ]
        },
        {
            id: 2,
            label: "一级 2",
            children: [
                {
                    id: 5,
                    label: "二级 2-1"
                },
                {
                    id: 6,
                    label: "二级 2-2"
                }
            ]
        },
        {
            id: 3,
            label: "一级 3",
            children: [
                {
                    id: 7,
                    label: "二级 3-1"
                },
                {
                    id: 8,
                    label: "二级 3-2"
                }
            ]
        }
    ];

    /**消息提示框方法声明开始 */
    win_message_success: any = win_message_success;
    win_message_warning: any = win_message_warning;
    win_message_error: any = win_message_error;
    win_message_info: any = win_message_info;
    //弹出框
    win_message_box_success: any = win_message_box_success;
    win_message_box_error: any = win_message_box_error;
    win_message_box_info: any = win_message_box_info;
    win_message_box_warning: any = win_message_box_warning;
    /**消息提示框方法声明结束 */

    tableData: any[] = [
        {
            date: "2016-05-02",
            name: "王小虎",
            address: "上海市普陀区金沙江路 1518 弄"
        },
        {
            date: "2016-05-04",
            name: "王小虎",
            address: "上海市普陀区金沙江路 1517 弄"
        }
    ];
    defaultProps = {
        children: "children",
        label: "label"
    };

    open1() {
        this.win_message_info("这是一条正常消息");
    }

    open2() {
        this.win_message_success("这是一条成功消息");
    }

    open3() {
        this.win_message_warning("这是一条警告消息");
    }

    open4() {
        this.win_message_error("这是一条错误消息");
    }
    open5() {
        this.win_message_box_success("这是一条成功弹框");
    }

    open6() {
        this.win_message_box_warning("这是一条警告弹框", "警告");
    }

    open7() {
        const ret =
            "<a href=\"javascript:void(0);\" onclick=\"approve('aaaa', 'APPROVED'" +
            ')" >' +
            "同意" +
            "</a>";
        this.win_message_box_error(ret, "错误(传html字符串，强转为html)", true);
    }
    open8() {
        const h = this.$createElement;
        const messageHData = [];
        messageHData.push(h("p", null, "AAAAAAAAA?确认"));
        messageHData.push(h("p", null, "BBBBBBBBB?确认"));
        messageHData.push(h("p", null, "AAAAAAAAA?确认"));
        this.win_message_box_info(
            h("div", null, messageHData),
            "自定义消息内容(无需强转html)"
        );
    }
}
</script>
<style lang="scss" scoped>
</style>