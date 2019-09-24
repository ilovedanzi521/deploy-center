<template>
    <div class="contanier-user" @mouseleave="close">
        <div @click="handleEditPassWord">修改密码</div>
        <div @click="logout">退出</div>
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
@Component({})
export default class LUserInfo extends Vue {
    @Prop()
    layoutReqVO: LayoutReqVO;
    //退出
    logout() {
        // window.location.reload(); 重新刷新页面
        if (localStorage.getItem("Authorization")) {
            localStorage.removeItem("Authorization");
        }
        this.$router.push({
            path: "/login"
        });
    }
    //修改密码
    handleEditPassWord() {
        this.$emit("modifyPassWord");
    }
    close() {
        this.$emit("closeUserInfo");
    }
}
</script>
<style lang="scss" scoped>
.contanier-user {
    position: relative;
    z-index: 9999;
    width: 120px;
    text-align: center;
    // color: #fff; //换色
    // background: #444e69; //换色
    border-radius: 6px;
    div {
        height: 36px;
        line-height: 36px;
        cursor: pointer;
        &:hover {
            // color: #ff900d; //换色
        }
    }
    &::after {
        content: "";
        position: absolute;
        height: 0;
        width: 0;
        top: -23px;
        // border: 12px solid transparent;换色
        // border-bottom-color: #444e69; //换色
    }
}
</style>