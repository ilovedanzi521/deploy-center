<template>
    <div>
        <!-- 工具栏按钮-->
        <div class="col-md-12" style="margin-top:5px;">
            <ul class="btn-edit fr">
                <li>
                    <win-button-group>
                        <win-button type="info" icon="el-icon-plus" round @click="addTask">添加任务</win-button>
                    </win-button-group>
                </li>
            </ul>
        </div>
        <!-- 表格 -->
        <div style="margin-top:5px;">
            <win-table border resizable :data="taskTableData" :show-selection="false" :show-index="false">
                <win-table-column type="selection" width="60" ></win-table-column>
                <win-table-column  title="序号" type="index" width="60"></win-table-column>
                <win-table-column  title="任务ID" field="id" width="160"></win-table-column>
                <win-table-column title="策略名称">
                    <template v-slot="{ row }">
                        <span>{{row.strategy.name}}</span>
                    </template>
                </win-table-column>
                <win-table-column title="设备组名">
                    <template v-slot="{ row }">
                        <span>{{row.group.name}}</span>
                    </template>
                </win-table-column>
                <win-table-column title="创建时间" field="createTime" :formatter="formatCellValue"></win-table-column>
                <win-table-column title="状态" field="status" :formatter="formatCellValue"></win-table-column>
                <win-table-column title="操作" >
                    <template v-slot="{ row }">
                        <win-button size="mini" type="text" icon="el-icon-orange">部署</win-button>
                        <win-button size="mini" type="text" icon="el-icon-delete">卸载</win-button>
                        <win-button size="mini" type="text" icon="el-icon-view">查看日志</win-button>
                    </template>
                </win-table-column>
            </win-table>
            <!--分页组件-->
            <div class="page-contanier">
                <win-pagination name="groupPage" v-bind:childMsg="pageVO" @callFather="pageQuery"></win-pagination>
            </div>
        </div>
        <!-- 添加任务弹框 -->
        <TaskDialog v-if="showDialog" :toChildMsg="toDialogMsg" @bindSend="bindDialogSend"></TaskDialog>
    </div>
</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import TaskController from "../controller/TaskController"

    @Component({})
    export default class task extends TaskController {}
</script>

<style lang="scss" scoped>
    .container {
        padding: 16px 18px;
        overflow: hidden;
        .page-contanier {
            position: absolute;
            width: 100%;
            right: 10px;
        }
    }
</style>

