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
        <div style="margin-top:9px;">
            <win-table border resizable :data="taskTableData" :show-selection="false" :show-index="false">
                <win-table-column title="策略名称" field="strategy.name" ></win-table-column>
                <win-table-column title="设置组名" field="group.name" ></win-table-column>
                <win-table-column title="创建时间" field="createTime" :formatter="formatCellValue"></win-table-column>
                <win-table-column title="操作" >
                    <template v-slot="{ row }">
                        <el-button size="mini" type="success">部署</el-button>
                        <el-button size="mini" type="danger">卸载</el-button>
                    </template>
                </win-table-column>
            </win-table>
            
        </div>
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

</style>

