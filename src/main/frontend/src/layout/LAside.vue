/* @全局左侧边框组件
 * @Author: mikey.zhz
 * @Date: 2019-05-21 13:42:17 
 * @Last Modified time: 2019-05-21 13:10:04
 */

<template>
    <section class="aside-container">
        <div>
            <p :class="['icon','win' ,item.menuIcon, {'active':item.id===layoutReqVO.firstMenuIndex}]" v-for="(item,index) in layoutReqVO.firstMents" :key="index" @mouseover="handleSecondMeunOpen(item.id)"></p>
        </div>
    </section>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
// import { BaseConst } from "@win-frond-frameworks/trade-biz";
import _ from "lodash";
import { setTimeout, clearTimeout } from "timers";

@Component({})
export default class LAside extends Vue {
    // s = BaseConst.Add;
    time;
    isFirstOpen: Boolean = false;
    @Prop()
    layoutReqVO: LayoutReqVO;
    handleSecondMeunOpen(id) {
        try {
            //重置搜索
            this.layoutReqVO.menuSize = 0;
            this.layoutReqVO.currentMenu = 0;
            this.layoutReqVO.isNoSearch = false;

            this.layoutReqVO.firstMenuIndex = id;
            this.layoutReqVO.stwichController.switchsScondMeunIsDetailed = true;
            this.layoutReqVO.secondMeunIsOpen = true;
            this.layoutReqVO.activeMenuId = this.layoutReqVO.secondMenus[
                this.layoutReqVO.firstMenuIndex
            ][0].children[0].id;
        } catch (e) {
            console.log(e);
        }
    }
    firstMenuIsOpen() {
        if (this.layoutReqVO.secondMeunIsOpen) {
            this.isFirstOpen = false;
            return;
        }
        if (this.isFirstOpen) {
            return;
        } else {
            clearTimeout(this.time);
            this.isFirstOpen = false;
        }
    }
}

// };
</script>

<style lang="scss" scoped>
@import "../assets/style/element.scss";
.aside-container {
    position: absolute;
    left: 0;
    top: 47px;
    bottom: 0;
    width: 70px;
    // background: $color-assist; //换色
    text-align: center;
    font-size: 20px;
    z-index: 5;
    .icon {
        height: 52px;
        line-height: 52px;
        cursor: pointer;
        //hover 后加
        // &:hover{
        //   background: #1F2640;
        //   &::before{
        //     color:$color-orange;
        //   }
        // }
        &::before {
            // color: #adb5bb; //换色
        }
        &.active {
            // background: #1f2640;//换色
            &::before {
                color: $color-orange;
            }
        }
    }
}
</style>
