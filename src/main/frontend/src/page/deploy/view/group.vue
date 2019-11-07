<template>
    <div>
        <!-- 设备组弹框end：新增/删除 -->
        <div class="col-md-12" style="margin-top:5px;">
            <ul class="btn-edit fr">
                <li>
                    <el-button-group>
                        <win-button type="info" icon="el-icon-plus" round @click="groupOperation('','ADD')">新增设备组</win-button>
                        <win-button v-popover:popover1 type="info" icon="el-icon-delete" round :disabled="selected.length == 0" @click="delGroupBatch">删除设备组</win-button>
                    </el-button-group>
                </li>
            </ul>
        </div>
        <div style="margin-top:9px;">
            <!--树形表格-->
            <win-table border resizable 
                        class="groupTable"  :cell-class-name="cellClassName"
                        :show-selection="false" :show-index="false"
                       @cell-dblclick="view" 
                       @select-all="handleSelectAll" @select-change="handleSelectChange"
                       :data="groupTreeList" :loading="groupLoading"
                       :tree-config="{children: 'children', expandAll: true}"
                       >
                <win-table-column type="selection" width="60" ></win-table-column>
                <win-table-column type="index" width="60" title="序号"></win-table-column>
                <win-table-column field="name" title="组（设备)名称" min-width="130" tree-node></win-table-column>
                <win-table-column field="ipAddress" title="ip地址" min-width="100"></win-table-column>
                <win-table-column field="desc" title="描述" min-width="130"></win-table-column>
                <win-table-column field="osType" title="系统类型" min-width="80"></win-table-column>
                <win-table-column field="userName" title="用户名" min-width="80"></win-table-column>
                <win-table-column field="port" title="端口号" min-width="60"></win-table-column>
                <win-table-column field="createTime" title="创建时间" min-width="100" :formatter="formatGroupTable"></win-table-column>
                <win-table-column field="status" title="连接状态" min-width="80">
                    <template v-slot="{ row }" >
                        <el-tag v-if="row.ipAddress==null ? false : true" :type="deviceStatusType(row.status)" @click="testConnect(row)">{{formatDeviceStatus(row.status)}}</el-tag>
                    </template>
                </win-table-column>
                <win-table-column title="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button v-if="row.ipAddress==null ? true : false" size="mini" type="text" icon="el-icon-edit" @click="groupOperation(row,'UPDATE')">编辑</el-button>
                        <el-button v-if="row.ipAddress==null ? true : false" size="mini" type="text" icon="el-icon-delete" @click="delGroupOne(row)">删除</el-button>
                    </template>
                </win-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </win-table>
            <!--分页组件-->
            <div class="page-contanier">
                <win-pagination v-bind:pageInfo="pageVO" @pageInfoChange="groupPageQuery"></win-pagination>
            </div>
            <!-- 设备组弹框begin：新增/删除 -->
            <GroupDialog v-if="isGroupDialog" :toChildMsg="groupDialogMsg" @bindSend="toGroupDialogForm" ></GroupDialog>
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
        .page-contanier {
            position: absolute;
            width: 100%;
            right: 10px;
        }
    }
    .groupTable /deep/ {
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

