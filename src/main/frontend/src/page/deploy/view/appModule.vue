<template>
    <div>
        <!-- 设备组弹框end：新增/删除 -->
        <div class="col-md-12" style="margin-top:5px;">
            <ul class="btn-edit fr">
                <li>
                    <el-button-group>
                        <win-button type="info" icon="el-icon-plus" round @click="groupOperation('','ADD')">上传应用</win-button>
                        <win-button v-popover:popover1 type="info" icon="el-icon-delete" round :disabled="selected.length == 0" @click="delGroupBatch">删除应用</win-button>
                    </el-button-group>
                </li>
            </ul>
        </div>
        <div style="margin-top:9px;">
            <el-table border resizable class="tree-table-class"  row-key="id"
                        :row-class-name="tableRowClassName"
                       :data="pageDataList" :loading="tableLoading"
                       :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                       lazy :load="lazyLoad"
                       >
                <el-table-column type="selection" width="60" ></el-table-column>
                <el-table-column prop="name" label="应用模块名(机器)" min-width="130"></el-table-column>
                <el-table-column prop="ipAddress" label="机器地址" min-width="100"></el-table-column>
                <el-table-column prop="path" label="脚本路径" min-width="100"></el-table-column>
                <el-table-column prop="packDir" label="包目录" min-width="130"></el-table-column>
                <el-table-column prop="packVer" label="包版本" min-width="80"></el-table-column>
                <el-table-column prop="packFile" label="包文件名" min-width="80"></el-table-column>
                <el-table-column prop="desc" label="描述信息" min-width="60"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" min-width="100" :formatter="formatGroupTable"></el-table-column>
                <el-table-column prop="status" label="服务状态" min-width="80">
                    <template v-slot="{ row }" >
                        <el-tag v-if="row.ipAddress==null ? false : true" :type="deviceStatusType(row.status)" >{{formatDeviceStatus(row.status)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button v-if="row.ipAddress==null ? false : true" size="mini" @click="groupOperation(row,'UPDATE')">编辑</el-button>
                        <el-button v-if="row.ipAddress==null ? false : true" size="mini" type="danger" @click="delGroupOne(row)">删除</el-button>
                    </template>
                </el-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </el-table>
            <!--分页组件-->
            <div class="page-contanier">
                <win-pagination name="groupPage" v-bind:childMsg="pageVO" @callFather="groupPageQuery"></win-pagination>
            </div>
            <!-- 设备组弹框begin：新增/删除 -->
            <GroupDialog v-if="isGroupDialog" :toChildMsg="groupDialogMsg" @bindSend="toGroupDialogForm" ></GroupDialog>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import AppModuleController from "../controller/AppModuleController";

    @Component({})
    export default class group extends AppModuleController {}
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
    // el-table 树形表格样式
    .el-table.tree-table-class /deep/ {
        width: 100%;
        margin-bottom: 20px;
        .row-node {
            background-color: #1b2441;
            color: #fff;
        }
        .row-children {
            background-color:#0b1431;
            color: #fff;
            //隐藏子节点复选框
            .vxe-checkbox{
                display: none;
            }
        }
        // 展开按钮样式
        .el-table__expand-icon .el-icon-arrow-right{
            color: #fff;
        }
    }
</style>

