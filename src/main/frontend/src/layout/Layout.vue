<template>
    <div :class="['container',layoutReqVO.changeTheme =='light'?'light':'dark']">
        <!--------@头部------------->
        <LHeader :layoutReqVO="layoutReqVO" :inFullCreeen="inFullCreeen" ref="lheader"></LHeader>
        <!--Tab区域---->
        <LTab :layoutReqVO="layoutReqVO"></LTab>
        <!--Tab区域---->
        <!---快速区域---->
        <LFast :layoutReqVO="layoutReqVO" @deleteFastItem="deleteFastItem" @gotoPath="gotoPath" @hasFastId="hasFastId"></LFast>
        <!---快速区域---->
        <!---更多快速区域---->
        <LMoreFast :layoutReqVO="layoutReqVO" @deleteFastItem="deleteFastItem" @gotoPath="gotoPath" @hasFastId="hasFastId" v-if="layoutReqVO.moreFastPanelisOpen"></LMoreFast>
        <!---更多快速区域---->
        <div class="changeBoutton">
            <span @click="changeLight">亮色</span>
            <span @click="changeDark">暗色</span> 
        </div>
        <!--修改密码--->
        <LPassword :layoutReqVO="layoutReqVO" @modifyPassWord="modifyPassWord"></LPassword>
        <!--修改密码--->
        <!--锁频--->
        <LLock :layoutReqVO="layoutReqVO" @handleSubmitLock="handleLockScreen" @handleSolutionLock="handleSolutionScreen" @closeLock="closeLock" :inFullCreeen="inFullCreeen"></LLock>
        <!--锁频- -->
        <!-------@头部------------------->

        <!----------------@左侧边栏--------->
        <!--遮罩层--->
        <transition name="hide">
            <LMark @closeAll="closeAllPanle" v-if="layoutReqVO.secondMeunIsOpen"></LMark>
        </transition>
        <transition name="fade">
            <LAside :layoutReqVO="layoutReqVO" v-if="layoutReqVO.secondMeunIsOpen"></LAside>
        </transition>
        <!--一级菜单-@changeSecondMenu点击切换面板事件--->
        <transition name="fade">
            <LMenu :layoutReqVO="layoutReqVO" @changeSecondMenu="changeSecondMenu" v-if="layoutReqVO.secondMeunIsOpen"></LMenu>
        </transition>
        <!--二级菜单-@gotoPath跳转到选中的路由事件-@toggleFastItem切换添加删除快速键的事件----->
        <transition name="fade">
            <div v-if="layoutReqVO.stwichController.switchsScondMeunIsDetailed" class="secondMeun">
                <LSecondMenu :layoutReqVO="layoutReqVO" @gotoPath="gotoPath" @toggleFastItem="toggleFastItem"></LSecondMenu>
            </div>
        </transition>
        <!--二级菜单-->
        <!----------------@左侧边栏--------->

        <!--右侧主边内容-@closeMenusPanle点击body关闭菜单面板-->
        <!----------------@右侧主要显示区域--------->
        <LMain>
            <!--路由页面切换--->
            <transition name="fade">
                <router-view @closeAll="closeAllPanle"></router-view>
            </transition>
            <!--路由页面切换--->
            <!-------@底部-------->
            <LFooter></LFooter>
            <!-------@底部-------->
        </LMain>
        <!----------------@右侧主要显示区域--------->

    </div>
</template>
<script lang="ts">
import LayoutController from "./controller/layoutControllers";
import "./style/style.scss";
import "./style/theme.scss";
export default class Layout extends LayoutController {}
</script>
<style lang="scss" scoped>
.changeBoutton {
    position: absolute;
    left: 84%;
    top: 14px;
    span {
     display: inline-block;
    padding: 2px 4px;
    background:transition;
    color:#fff;
    cursor: pointer;
    }
}
</style>