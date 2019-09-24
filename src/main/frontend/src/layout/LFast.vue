/* @ 全局快速区组件
 * @Author: mikey.zhaopeng 
 * @Date: 2019-05-29 10:30:31 
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-08-17 14:15:09
 */

<template>
    <span>
        <span class="fast-contanier">
            <i :class="['icon','win',fastItem.menuIcon] " v-for="(fastItem,index) in layoutReqVO.fastMenus " @contextmenu.prevent="handleOpenDelPanel(fastItem.id)" :key="fastItem.menuName" :title="fastItem.menuName"
                @click="handleClick(fastItem)" @mouseenter="handleFastItem(fastItem.id)" v-dragging="{ item: fastItem, list: layoutReqVO.fastMenus, group: 'fastItem' }" v-if="index<maxFast">
                <!-- <span :class="['delte',{'active':fastItem.id===fastIndex}]">
                    <span @click="handleCloseDelPanel" class="close">关闭</span>
                    <i class="icon-2" @click="handleDeleteFastItem(fastItem.id)">✖</i>
                </span> -->
                <b class="right-hr"></b>
            </i>
            <em v-if="layoutReqVO.fastMenus.length>=maxFast" class="more" @click="openMorePanel"> 更多</em>
        </span>

    </span>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop, Emit } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
import { Menu } from "element-ui";

@Component({})
export default class LFast extends Vue {
    fastIndex: string = "";
    maxFast = 3;
    @Prop()
    layoutReqVO: LayoutReqVO;

    // /**显示元素删除窗口 */
    handleOpenDelPanel(id) {
        this.fastIndex = id;
    }
    /*删除一个元素动作 */
    handleDeleteFastItem(id) {
        this.$emit("deleteFastItem", id);
    }
    /**鼠标移到某个快捷元素获取id号 */
    handleFastItem(id) {
        this.$emit("hasFastId", id);
    }
    /*关闭删除元素窗口动作 */
    handleCloseDelPanel() {
        this.fastIndex = "";
    }

    @Emit("gotoPath")
    handleClick(menu) {
        var returnMenu = Object.assign({}, menu);
        returnMenu.id = returnMenu.menuId;
        return returnMenu;
    }
    openMorePanel() {
        this.layoutReqVO.moreFastPanelisOpen = true;
    }
}
</script>

<style lang="scss" scoped>
@import "../assets/style/element.scss";
.fast-contanier {
    padding-left: 48px;
    position: absolute;
    left: 152px;
    top: 14px;
    .icon {
        position: relative;
        display: inline-block;
        vertical-align: middle;
        font-size: 16px;
        &:last-of-type {
            .right-hr {
                background: transparent;
            }
        }
        &::before {
            // color: #adb5bb;//换色
        }
        cursor: pointer;
        &:hover {
            &::before {
                // color: $color-orange;//换色
            }
        }
        .right-hr {
            display: inline-block;
            vertical-align: middle;
            height: 12px;
            width: 1px;
            margin: 0 16px;
            // background: #adb5bb;//换色
        }
    }
    .delte {
        position: absolute;
        left: -39px;
        top: 28px;
        width: 104px;
        height: 30px;
        line-height: 30px;
        padding: 0 14px 0 4px;
        color: #fff;
        text-align: left;
        border: 2px solid #cc791b;
        font-size: 10px;
        border-radius: 6px;
        box-sizing: border-box;
        z-index: 99;
        cursor: pointer;
        display: none;
        background: #444e69;
        &::after {
            content: "";
            position: absolute;
            top: -16px;
            left: 43px;
            height: 0;
            width: 0;
            border: 8px solid transparent;
            border-bottom-color: #444e69;
        }
        &::before {
            content: "";
            position: absolute;
            top: -24px;
            left: 39px;
            height: 0;
            width: 0;
            border: 12px solid transparent;
            border-bottom-color: #cc791b;
        }
        .close {
            position: relative;
            display: inline-block;
            top: -2px;
            &:hover {
                color: #cc791b;
            }
        }
        .icon-2 {
            position: relative;
            display: inline-block;
            top: -2px;
            left: 45px;
            &:hover::before {
                color: #cc791b;
            }
            &::before {
                font-size: 12px;
            }
        }
        &.active {
            display: inline;
        }
    }
}
.more {
    display: inline-block;
    position: relative;
    left: -11px;
    padding: 2px 6px;
    color: #fff;
    cursor: pointer;
}
</style>