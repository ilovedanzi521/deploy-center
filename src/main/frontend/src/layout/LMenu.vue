/* @二级导航组件
 * @Author: mikey.zhz
 * @Date: 2019-05-21 13:42:17 
 * @Last Modified time: 2019-05-21 13:10:04
 */

<template>
    <section class="menu-container">
        <ul class="menu-first">
            <li :class="{'active':layoutReqVO.firstMenuIndex===item.id}" v-for="( item,index ) in layoutReqVO.firstMents" :key="item.id" @mouseenter="handleChangeTab(item.id,index)">
                <span>{{item.menuName}}</span>
                <i class="icon-14 win win-next"></i>
            </li>
        </ul>
        <!-- <span class="icon_panel"></span> -->
    </section>
</template>
<script lang="ts">
import Vue from "vue";
import { Component, Prop, Emit } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
import { setTimeout, clearTimeout } from "timers";

@Component({})
export default class LMenu extends Vue {
    time;
    isSecondOpen: Boolean = false;
    @Prop()
    layoutReqVO: LayoutReqVO;
    /**
     *切换菜单
     * * */
    @Emit("changeSecondMenu")
    handleChangeTab(id) {
        if (this.time) {
            clearTimeout(this.time);
        }
        this.layoutReqVO.firstMenuIndex = id;
        this.layoutReqVO.stwichController.switchsScondMeunIsDetailed = true;
        //重置搜索
        this.layoutReqVO.menuSize = 0;
        this.layoutReqVO.currentMenu = 0;
        this.layoutReqVO.isNoSearch = false;
        return id;
    }
    secondIsClose() {
        if (this.isSecondOpen) {
            return;
        } else {
            clearTimeout(this.time);
            this.isSecondOpen = false;
        }
    }
}
</script>

<style lang="scss" scoped>
@import "../assets/style/element.scss";
.menu-container {
    position: absolute;
    left: 70px;
    top: 47px;
    bottom: 22px;
    width: 130px;
    z-index: 5;
    .icon_panel {
        position: absolute;
        width: 15px;
        height: 25px;
        line-height: 25px;
        background: #57607d;
        right: 0px;
        top: 50%;
        margin-top: -18px;
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
    }
    .menu-first {
        position: absolute;
        left: 0;
        top: 0;
        bottom: -36px;
        width: 130px;
        // background: #0a0f0e; //换色
        li {
            display: flex;
            height: 52px;
            padding-right: 18px;
            justify-content: space-between;
            align-items: center;
            text-align: left;
            // color: #adb5bb; //换色
            font-size: 14px;
            box-sizing: border-box;
            cursor: pointer;
            .icon-14::before {
                color: transparent;
                font-size: 14px;
            }
            &.active {
                // background: #1f2640;//换色
                color: $color-orange;
                .icon-14::before {
                    color: $color-orange;
                }
            }
        }
    }

    .close {
        position: absolute;
        top: 24px;
        right: 18px;
        color: #1f2640;
        cursor: pointer;
        &:hover {
            color: $color-orange;
        }
    }
}
</style>
