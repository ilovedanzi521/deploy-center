<template>
    <section class="container">
        <div class="mask"></div>
        <div class="content">
            <Logo></Logo>
            <Login @loginClick="login" @rememberChange="changeRemember" @creatCode="createCode" @passwordTip="passwordTip" @passusenameTip="passusenameTip" @yzmTip="yzmTip" :loginVO="loginVO"></Login>
        </div>
    </section>
</template>
<script lang="ts">
import LoginController from "../controller/LoginController";
export default class Login extends LoginController {}
</script>
<style lang="scss" scoped>
.container {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background: url("../../../assets/image/lognbg.png") no-repeat;
    background-size: 100%;
    .mask {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.6);
    }
    .content {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        display: flex;
        justify-content: center;
        align-items: center;
    }
}
</style>