<template>
    <div>
        <div style="margin-top:9px;">
            <!--树形表格-->
            <win-table border resizable 
                        class="strategyTable" 
                        :show-selection="false" :show-index="false"
                       @cell-dblclick="view" 
                       @select-all="handleSelectAll" @select-change="handleSelectChange"
                        :loading="tableLoading" :data="strategyList">
                <win-table-column type="selection" width="60" ></win-table-column>
                <win-table-column field="name" min-width="130" title="策略名称"></win-table-column>
                <win-table-column field="path" title="脚本路径" min-width="100"></win-table-column>
                <win-table-column field="desc" title="描述" min-width="130"></win-table-column>
                <win-table-column field="createTime" title="创建时间" min-width="100" :formatter="formatGroupTable"></win-table-column>
                <win-table-column title="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button  size="mini" type="text" icon="el-icon-edit"  @click="showStrategyDetail(row)">查看详情</el-button>
                    </template>
                </win-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </win-table>
            <!--分页组件-->
            <div class="page-contanier">
                    <win-pagination v-bind:pageInfo="pageVO" @pageInfoChange="strategyPageQuery"></win-pagination>
            </div>
            <!-- 添加弹框 -->
            <StrategyDialog v-if="showDialog" :toChildMsg="toDialogMsg" @bindSend="bindDialogSend"></StrategyDialog>
        </div>
    </div>


</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import StrategyManagementController from "../controller/StrategyManagementController";

    @Component({})
    export default class strategyManagement extends StrategyManagementController {
    }

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
    .strategyTable /deep/ {
        width: 100%;
        margin-bottom: 20px;
        .vxe-body--row.row--level-0.row-orange {
            background-color: #4a5373;
            color: #fff;
        }
        .vxe-body--column.col-checkbox-none .vxe-checkbox{
            display: none;
        }
    }
</style>

