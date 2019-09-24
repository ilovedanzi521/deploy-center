
<template>
    <!-- <win-fdialog :title="getLock?'解屏':'锁屏'" :visible.sync="layoutReqVO.lockisOpen" :close-on-click-modal="false" width="420px" :show-close="!getLock" custom-class="lockScree">
        <win-form :inline="true">
            <div class="form_content">
                <win-form-item label="登录密码">
                    <win-input :placeholder="getLock?'请输入登录密码进行解屏':'请输入登录密码进行锁屏'" autocomplete="off" type="password" v-model="loginPassword"></win-input>
                </win-form-item>
            </div>
        </win-form>
        <div slot="footer" class="dialog-footer">
            <el-button v-if="!getLock" @click="close">取 消</el-button>
            <el-button type="warning" v-if="getLock" @click="handleSolutionLock">确 认 解 屏</el-button>
            <el-button type="warning" @click="handleSubmitLock" v-else>确 认 锁 屏</el-button>
        </div>
    </win-fdialog> -->
    <win-fdialog title="解屏" :visible.sync="layoutReqVO.lockisOpen" :close-on-click-modal="false" width="420px" custom-class="lockScree" :close-on-press-escape="false">
        <div class="form_content">
            <label class="label">登录密码</label>
            <win-input placeholder="输入登录密码进行解屏" type="password" v-model="loginPassword" test_name="lock-01" :autofocus="true"></win-input>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="warning" @click="handleSolutionLock">确 认 解 屏</el-button>
        </div>
    </win-fdialog>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
import { gettLocalStore } from "../assets/js/localStore";
import BaseController from "../page/common/controller/BaseController";
@Component({})
export default class LLock extends BaseController {
    loginPassword: string = "";
    @Prop()
    layoutReqVO: LayoutReqVO;
    @Prop()
    inFullCreeen;
    /***锁屏幕 */
    handleSubmitLock() {
        this.$emit("handleSubmitLock", this.loginPassword);
        this.loginPassword = "";
    }
    /***解屏幕 */
    handleSolutionLock(loginPassword) {
        this.$emit("handleSolutionLock", this.loginPassword);
        this.loginPassword = "";
    }

    get getLock(): boolean {
        if (this.layoutReqVO.nameLock) {
            return true;
        } else {
            return false;
        }
    }

    close() {
        this.$emit("closeLock");
    }
    mounted() {
        if (localStorage.getItem("lockName")) {
            this.handleSubmitLock();
        }
    }
}
</script>
<style lang="scss" scoped>
.lockScree {
    .form_content {
        margin-bottom: 0px;
    }
    .v-modal {
        background-color: red;
    }
    input[type="password"] {
        display: inline-block;
        height: 30px;
        width: 200px;
        line-height: 30px;
        border: 1px solid #ff900d;
        background: #1b2441;
        color: #fff;
        padding-left: 10px;
        margin-left: 10px;
        border-radius: 4px;
    }
    .label {
        margin-left: 30px;
    }
}
</style>