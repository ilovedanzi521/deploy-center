<template>
    <div>
        <h1 style="color: red">设备管理</h1>

        <div class="content-fl">
            <div class="content-fl-warp">
                <win-table :data="groupTreeList" style="width: 100%;margin-bottom: 20px;" row-key="id" border default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                    <win-table-column prop="name" :min-width="80" label="组（设备)名称" sortable></win-table-column>
                    <win-table-column prop="ipAddress" :min-width="100" label="ip地址"></win-table-column>
                    <win-table-column prop="desc" :min-width="100" label="描述"></win-table-column>
                    <win-table-column prop="osType" :min-width="100" label="系统类型"></win-table-column>
                    <win-table-column prop="userName" :min-width="100" label="用户名"></win-table-column>
                    <win-table-column prop="port" :min-width="100" label="端口号"></win-table-column>
                    <win-table-column prop="createTime" :min-width="150" label="创建时间"></win-table-column>
                    <win-table-column prop="status" :min-width="150" label="连接状态"></win-table-column>
                </win-table>
                <div class="paginatio-contanier">
                    <win-pagination name="subDic" v-bind:childMsg="pageVO" @callFather="groupPageQuery"></win-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import GroupController from "../controller/GroupController";

    @Component({})
    export default class group extends GroupController {}
</script>

<style lang="scss" scoped>
    .container {
        padding: 16px 18px;
        overflow: hidden;
        .content-fr {
            position: relative;
            box-sizing: border-box;
            float: left;
            width: 27%;
            box-sizing: border-box;
            .content-fr-warp {
                background: #0b1431ff;
                height: 720px;
                overflow-x: hidden;
            }
        }
        .content-fl {
            position: relative;
            width: 72%;
            box-sizing: border-box;
            top: 28px;
            float: right;
            .content-fl-warp {
                border: 1px solid #f58b0eff;
                padding: 2px;
                background: #38415bff;
                box-sizing: border-box;
            }
        }
        .page-contanier {
            position: absolute;
            width: 100%;
            right: 10px;
        }

        .search-contanier {
            margin-bottom: 14px;
            overflow: hidden;
            .add-botton {
                display: inline-block;
                width: 132px;
                height: 36px;
                font-size: 14px;
                line-height: 36px;
                border-radius: 20px;
                background: #2b3551ff;
                color: #fff;
                text-align: center;
            }
        }
        .paginatio-contanier {
            margin-top: 5px;
        }
    }
</style>

