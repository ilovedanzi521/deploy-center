<template>
    <div class="logo-container">
        <p class="welcome">Welcome</p>
        <p class="hr"></p>
        <p class="logo-content">
            <img src="../../../assets/image/logo.png" width="33" height="35">
            <span class="logo-text">赢时胜信息技术</span>
        </p>
        <p class="copy">赢时胜信息技术股份有限公司</p>
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component({})
export default class Logo extends Vue {}
</script>
<style lang="scss" scoped>
@import "../../../assets/style/element.scss";
.logo-container {
    position: relative;
    width: 322px;
    height: 373px;
    padding: 46px 0 12px 40px;
    background: $color-bule;
    border-radius: 4px;
    box-sizing: border-box;
    .welcome {
        font-size: 30px;
        color: $color-white;
        font-weight: bold;
        line-height: 40px;
        margin-bottom: 6px;
    }
    .hr {
        height: 1px;
        width: 108px;
        background: $color-white;
    }
    .logo-content {
        margin-top: 32px;
        height: 26px;
        img,
        span {
            display: inline-block;
            vertical-align: middle;
            &.logo-text {
                color: $color-white;
                font-size: 24px;
                margin-left: 20px;
                font-weight: bold;
            }
        }
    }
    .copy {
        position: absolute;
        left: 40px;
        bottom: 12px;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.4);
        font-weight: 400;
    }
}
</style>