/* @首页Home路口文件
 * @Author: mikey.zhaopeng 
 * @Date: 2019-05-31 01:44:49 
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-07-03 14:49:58
 */


<template>
    <div class="no-fond">
        404
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import Panels from "./panels.vue";
</script>
<style lang="scss" scoped>
.no-fond {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background: #0b1431;
    display: inline-block;
    color: #1565c0;
    text-transform: uppercase;
    font-weight: bold;
    text-align: center;
    font-size: 120px;
    letter-spacing: -4px;
    padding-top: 10%;
    text-shadow: 4px 4px 0px #441369, 8px 8px 0px rgba(255, 255, 255, 0.1);
}
</style>