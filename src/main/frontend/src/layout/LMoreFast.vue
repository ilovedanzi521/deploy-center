<template>
    <div class="moreFast-contanier">
        <ul>
            <li v-for="fItem in getMenu " :key="fItem.menuName" :title="fItem.menuName" @click="handleClick(fItem)" @mouseenter="handleFastItem(fItem.id)" v-dragging="{ item: fItem, list: menu, group: 'fItem' }">
                <p :class="['icon','win',fItem.menuIcon] "></p>
                <p class="fast-menu-name">{{fItem.menuName}}</p>
            </li>
            <span class="close" @click="close">✖</span>
        </ul>

    </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop, Emit, Watch } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
import { Menu } from "element-ui";

@Component({})
export default class LMoreFast extends Vue {
    fastIndex: string = "";
    menu = [];
    @Prop()
    layoutReqVO: LayoutReqVO;

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
    close() {
        this.layoutReqVO.moreFastPanelisOpen = false;
    }
    mounted() {
        this.menu = this.layoutReqVO.fastMenus;
    }

    get getMenu() {
        this.menu = this.layoutReqVO.fastMenus;
        return this.menu;
    }
}
</script>

<style lang="scss" scoped>
@import "../assets/style/element.scss";
.moreFast-contanier {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 999;
    .fast-menu-name{
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    ul {
        position: relative;
        overflow: hidden;
        padding: 10px;
        border-radius: 6px;
        border: 1px solid #ff900d;
        background: #1b2441;
        max-width: 500px;
        li {
            width: 80px;
            float: left;
            margin: 10px;
            color: #fff;
            text-align: center;
            cursor: pointer;
            &:hover {
                color: #ff900d;
            }
        }
    }
    .close {
        position: absolute;
        color: #fff;
        right: 10px;
        top: 8px;
        cursor: pointer;
        &:hover {
            color: #ff900d;
        }
    }
}
</style>