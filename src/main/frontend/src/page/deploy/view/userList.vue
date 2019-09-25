<template>
    <div class="container">
        <h1 style="color: red"> 用户管理</h1>

        <div class="content-fl">
            <div class="content-fl-warp">
                <win-table :data="userList" :highlight-current-row="true" :showSelection="false" height="600" :index-width="20" :style="{'width': '100%'}">
                    <win-table-column prop="name" :min-width="80" label="用户名" sortable></win-table-column>
                    <win-table-column prop="password" :min-width="100" label="密码"></win-table-column>
                </win-table>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import UserListController from "../controller/UserListController";

    @Component({})
    export default class User extends UserListController {}
</script>

<style lang="scss" scoped>
    .container {
        padding: 16px 18px;
        overflow: hidden;
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
    }
</style>

