/* @panel组件
 * @Author: mikey.zhz
 * @Date: 2019-05-21 13:42:17 
 * @Last Modified time: 2019-05-21 13:10:04
 */

<template>
    <section class="panel">
        <div class="panel-header">
            <span>资产净值走势图</span>
            <i class="icon-2"></i>
        </div>
        <div class="panel-content">
            <p class="panel-content-inline">
                <span class="green">华夏成长基金</span>
            </p>
            <p class="panel-content-inline">
                <span class="yellow">华夏成长基金</span>
            </p>
            <p class="panel-content-inline">
                <span class="red">华夏成长基金</span>
            </p>
        </div>
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
.panel {
    float: left;
    width: 456px;
    height: 390px;
    margin: 0 40px 44px 0;
    border-radius: 11px;
    overflow: hidden;
    background: #1f2640;
    .panel-header {
        display: flex;
        height: 40px;
        padding: 0 16px;
        flex-direction: row;
        justify-content: space-between;
        color: $color-white;
        background: #68718e;
        align-items: center;
        font-size: 16px;
    }
    .panel-content {
        padding: 26px 42px;
        .panel-content-inline {
            display: inline-block;
            margin: 0 80px 12px 0;
            font-size: 14px;
            color: #9290b1;
            span {
                position: relative;
                &.green {
                    &::before {
                        background: $color-green;
                    }
                }
                &.yellow {
                    &::before {
                        background: $color-yellow;
                    }
                }
                &.red {
                    &::before {
                        background: $color-red;
                    }
                }
                &::before {
                    content: "";
                    position: absolute;
                    left: -22px;
                    top: 50%;
                    width: 10px;
                    height: 10px;
                    margin-top: -5px;
                    border-radius: 50%;
                }
            }
        }
    }
}
</style>
