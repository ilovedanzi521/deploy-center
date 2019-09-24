/* @首页Home路口文件
 * @Author: mikey.zhaopeng 
 * @Date: 2019-05-31 01:44:49 
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-07-17 10:35:15
 */


<template>
    <div class="home-content">
        <addPanel></addPanel>
    </div>
</template>

<script lang="ts">
import Vue from "vue";

import Panels from "./addPanel.vue";
import { Component, Prop, Emit } from "vue-property-decorator";

import { WinButton } from "win-plus";
import { WinForm, WinFormItem } from "win-plus";
import { WinTabs, WinTabpane } from "win-plus";
import { WinSelect, WinOption } from "win-plus";
import { WinInput } from "win-plus";
import { WinTable, WinTableColumn } from "win-plus";
import addPanel from "./addPanel.vue";

@Component({
    components: {
        addPanel,
        WinButton,
        WinTabs,
        WinSelect,
        WinInput,
        WinTable
    }
})
export default class Index extends Vue {
    tabValue: number = 0;

    handleChangeTab(index) {
        this.tabValue = index;
    }
}
</script>
<style lang="scss" scoped>
.contanierTop8px {
    margin-top: 8px;
}
.input_group_wrap {
    margin-right: 18px;
    .input_group_wrap_label {
        padding-left: 4px;
        color: #909399;
    }
}
.input_group_more {
    display: inline-block;
    font-size: 14px;
    color: #ff900d;
    font-size: 0;
    cursor: pointer;
    span {
        display: inline-block;
        font-size: 14px;
        margin-right: 10px;
    }
    em {
        display: inline-block;
        font-size: 14px;
        margin-right: 11px;
    }
}
.el-input__inner {
    height: 28px;
    line-height: 28px;
}

.el-button--info {
    color: white;
    background-color: #2b3551;
    border-color: #2b3551;
}
</style>