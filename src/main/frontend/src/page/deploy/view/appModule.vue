<template>
    <div>
        <!-- 工具栏按钮 -->
        <div class="col-md-12" style="margin-top:5px;">
            <ul class="btn-edit fr">
                <li>
                    <win-button-group>
                        <win-button type="info" icon="el-icon-plus" round @click="uploadAppModule">上传应用</win-button>
                        <win-button type="info" icon="el-icon-delete" round :disabled="selected.length == 0" @click="delGroupBatch">删除应用</win-button>
                    </win-button-group>
                </li>
            </ul>
        </div>
        <div style="margin-top:9px;">
            <vxe-table ref="xTable" border stripe show-overflow class="app-module-table"
                     :show-selection="false" :show-index="false" 
                     :loading="tableLoading" :expand-config="{expandRowKeys: ['1']}" 
                     :data="pageDataList" @toggle-expand-change="toggleExpandChangeEvent">
                <vxe-table-column type="selection" width="60"/>
                <vxe-table-column type="expand" width="60">
                    <template v-slot="{ row, rowIndex }">
                        <vxe-table border class="device-table" size="mini" :loading="devicesTableLoading"
                            :data="row.devices" >
                            <vxe-table-column field="deviceName" title="名称"></vxe-table-column>
                            <vxe-table-column field="ipAddress" title="ip地址"></vxe-table-column>
                            <vxe-table-column field="status" title="状态" :formatter="formatDeviceStatus"></vxe-table-column>
                            <vxe-table-column title="操作" min-width="100">
                                <template v-slot="{ row }">
                                    <el-button  size="mini" type="text" icon="el-icon-switch-button" :loading="startBtnLoading" @click="startApp(row,rowIndex)">启动</el-button>
                                    <el-button  size="mini" type="text" icon="el-icon-delete" :loading="stopBtnLoading" @click="stopApp(row,rowIndex)">停止</el-button>
                                </template>
                            </vxe-table-column>
                            <template v-slot:empty>
                                <span style="color: red;">当前应用还未部署到任何机器，无法查看服务实例</span>
                            </template>
                        </vxe-table>
                        <!-- <span style="color: red;">当前应用未部署，无法查看实例</span> -->
                    </template>
                </vxe-table-column>
                <vxe-table-column field="name" title="应用模块名(机器)" min-width="130"></vxe-table-column>
                <vxe-table-column field="path" title="脚本路径" min-width="100"></vxe-table-column>
                <vxe-table-column field="packDir" title="包目录" min-width="130"></vxe-table-column>
                <vxe-table-column field="packVer" title="包版本" min-width="80"></vxe-table-column>
                <vxe-table-column field="packFile" title="包文件名" min-width="80"></vxe-table-column>
                <vxe-table-column field="desc" title="描述信息" min-width="60"></vxe-table-column>
                <vxe-table-column field="createTime" title="创建时间" min-width="100" :formatter="formatGroupTable"></vxe-table-column>
                <vxe-table-column field="status" title="状态" min-width="80" :formatter="formatGroupTable"></vxe-table-column>
                <vxe-table-column title="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button size="mini" type="text" icon="el-icon-delete" @click="delGroupOne(row)">删除</el-button>
                    </template>
                </vxe-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </vxe-table>
            <!--分页组件-->
            <div class="page-contanier">
                <win-pagination v-bind:pageInfo="pageVO" @pageInfoChange="pageQuery"></win-pagination>
            </div>
            <!--上传应用弹框 -->
            <UploadDialog v-if="isUploadDialog" :toChildMsg="toUploadDialogMsg" @bindSend="bindUploadSend" ></UploadDialog>
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
    // 应用模块表格样式
    .app-module-table /deep/ {
        // 表头高度统一样式
        .vxe-header--column {
            height: 32px;
            padding: 2px 0 !important;
        }
        .vxe-body--column{
            height: 28px;
            padding: 2px 0 !important;
        }
        // 表格展开箭头样式
        .vxe-table--expanded .vxe-table--expand-icon{
            border-color: #fff
        }
    }
    // 机器实例表格样式
    .device-table /deep/ {
        background-color: #565661;
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
    }
   
</style>

