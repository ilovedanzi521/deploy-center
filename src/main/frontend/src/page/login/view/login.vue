<template>
    <div class="login-container">

        <span class="login-title">账户密码登录</span>
        <div class="login-info">

            <label class="icon icon-29" for="user"></label>
            <span class="input-content">
                <input type="text" placeholder="用户名/用户编号" id="user" v-focus :change="handleChange()" v-model="username" @blur="handleUsernameForm" @keyup.enter="hangleLogin" maxlength="50" autocomplete="off">
            </span>
            <label for="remember" :class="['remember' ,{'rememberOk':rememberisOk}]" @click="handleRemberUserName"></label>
        </div>
        <div class="login-info">
            <label class="icon icon-5" for="password"></label>
            <span class="input-content">
                <input type="password" placeholder="密码" id="password" v-model="password" @blur="handlePasswordForm" @keyup.enter="hangleLogin" maxlength="50" autocomplete="off">
            </span>
        </div>
        <!-- <div class="login-info">
            <label class="icon icon-4" for="code"></label>
            <span class="input-content code">
                <input type="text" placeholder="验证码" maxlength="4" id="code" v-model="loginVO.code" @input="handleYzmForm" @keyup.enter="hangleLogin" autocomplete="off">
            </span>
            <span class="code-img">
                <input type="button" ref="checkCode" @click="handleCreateCode" class="yzm" v-model="loginVO.yzm">
            </span>
        </div> -->
        <div :class="['login-info','notip',{'tip-message':ShowTip}]">
            <span class="icon-8 icon-tip"></span>
            <span class="tip">{{loginVO.tipMessage}}</span>
        </div>
        <div class="login-content">
            <button class="login" @click="hangleLogin">登录</button>
        </div>

    </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop, Emit } from "vue-property-decorator";
import { LoginVO } from "../vo/loginVO";

@Component({
    directives: {
        focus: {
            // 指令的定义
            inserted: function(el) {
                el.focus();
            }
        }
    }
})
export default class Login extends Vue {
    rememberisOk: boolean = localStorage.getItem("userName") ? true : false;
    username: string = localStorage.getItem("userName") || "";
    password: string = "";
    // code: string = "";
    @Prop()
    loginVO: LoginVO;

    get ShowTip(): boolean {
        if (this.loginVO.tipMessage) {
            return true;
        }
        if (this.loginVO.rememberOk) {
            return false;
        }
    }

    handleChange() {
        this.username = this.username.replace(/[^\a-\z\A-\Z0-9]/g, "");
    }

    @Emit("loginClick")
    hangleLogin() {
        return {
            loginName: this.username,
            password: this.password
        };
    }
    @Emit("this.username")
    handleUsernameForm() {
        return this.username;
    }
    @Emit("passwordTip")
    handlePasswordForm() {
        return this.password;
    }
    @Emit("yzmTip")
    handleYzmForm() {
        return this.loginVO.code;
    }

    @Emit("rememberChange")
    handleRemberUserName() {
        this.rememberisOk = !this.rememberisOk;
        return this.rememberisOk;
    }
    @Emit("creatCode")
    handleCreateCode() {}

    mounted() {
        let that = this;
        document.body.addEventListener("keydown", function(e) {
            let key = e.keyCode;
            if (key == 13) {
                that.$emit("loginClick", {
                    loginName: that.username,
                    password: that.password
                });
            }
        });
    }
}
</script>
<style lang="scss" scoped>
@import "../../../assets/style/element.scss";

.login-container {
    position: relative;
    width: 398px;
    height: 442px;
    padding: 44px 0 54px 0;
    background: $color-white;
    border-radius: 4px;
    box-sizing: border-box;
    left: -6px;
    .login-title {
        display: inline-block;
        padding: 0 0 4px 0;
        margin-left: 40px;
        margin-bottom: 56px;
        font-size: 18px;
        font-weight: 700;
        color: $color-orange;
        border-bottom: 1px solid $color-orange;
    }
    .yzm {
        display: inline-block;
        width: 100px;
        height: 38px;
        vertical-align: middle;
        border: none;
        font-size: 20px;
        font-style: italic;
        font-weight: bolder;
        letter-spacing: 4px;
        background-image: linear-gradient(160deg, #00ffd5 20%, #008cff 80%);
        color: #fff;
    }
    .login-info {
        margin: 0 0 20px 28px;
        &.notip {
            opacity: 0;
            &.tip-message {
                opacity: 1;
            }
        }

        .icon {
            display: inline-block;
            vertical-align: -5px;
            margin-right: 12px;
            font-size: 20px;
        }
        .input-content {
            width: 296px;
            display: inline-block;
            height: 31px;
            border-bottom: 1px solid #a8a8a8;
            &.code {
                width: 154px;
            }
            input {
                border: none;
                background: $color-white;
                margin-bottom: 6px;
                font-size: 16px;
                &::-webkit-input-placeholder {
                    color: #a8a8a8;
                    font-size: 14px;
                }
            }
        }
        .icon-tip {
            display: inline-block;
            vertical-align: middle;
            margin-right: 14px;
            margin-left: 6px;
            &::before {
                color: $color-orange;
            }
            & + .tip {
                display: inline-block;
                vertical-align: middle;
                color: #a8a8a8;
            }
        }
    }
    .login-content {
        margin-top: 32px;
        text-align: center;
        .login {
            width: 152px;
            height: 40px;
            border: none;
            background: $color-orange;
            text-align: center;
            color: $color-white;
            font-size: 14px;
            font-weight: 400;
            border-radius: 4px;
            cursor: pointer;
        }
    }
    .code-img {
        position: absolute;
        margin-left: 20px;
        margin-top: -6px;
    }
    .checkbox {
        border: none;
    }
    .remember {
        position: absolute;
        width: 16px;
        height: 16px;
        margin-left: -23px;
        margin-top: 4px;
        border: 1px solid #b7b7b7;
        cursor: pointer;
        &.rememberOk {
            &::after {
                content: "";
                position: absolute;
                top: 50%;
                left: 50%;
                height: 4px;
                width: 8px;
                margin-left: -6px;
                margin-top: -7px;
                border: 2px solid transparent;
                border-left-color: $color-orange;
                border-bottom-color: $color-orange;
                transform: rotate(-40deg);
            }
        }
    }
}
</style>