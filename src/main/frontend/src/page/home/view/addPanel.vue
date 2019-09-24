/* @panel组件
 * @Author: mikey.zhz
 * @Date: 2019-05-21 13:42:17 
 * @Last Modified time: 2019-05-21 13:10:04
 */

<template>
    <section class="addPanel">
    </section>
</template>
<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";

@Component({})
export default class Panel extends Vue {}
</script>
<style lang="scss" scoped>
@import "../../../assets/style/element.scss";
.addPanel {
    float: left;
    position: relative;

    width: 456px;
    height: 370px;
    margin: 0 40px 44px 0;
    margin-left: 14px;
    border-radius: 11px;
    overflow: hidden;
    background: #1f2640;
    border: 1px dashed #68718e;
    cursor: pointer;
    &::after {
        content: "";
        position: absolute;
        width: 20px;
        height: 150px;
        background: #68718e;
        left: 50%;
        margin-left: -10px;
        top: 50%;
        margin-top: -75px;
    }
    &::before {
        content: "";
        position: absolute;
        width: 150px;
        height: 20px;
        background: #68718e;
        left: 50%;
        margin-left: -75px;
        top: 50%;
        margin-top: -10px;
    }
}
</style>
