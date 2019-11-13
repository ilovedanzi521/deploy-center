<template>
    <div>
        <div class="col-md-12" style="margin-top:5px;">
            <ul class="btn-edit fr">
                <li>
                    <el-button-group>
                        <win-button type="info" icon="el-icon-plus" round @click="deviceOperation('','ADD')">新增设备</win-button>
                        <win-button v-popover:popover1 type="info" icon="el-icon-delete" round :disabled="selected.length == 0" @click="delDeviceBatch">删除设备</win-button>
                    </el-button-group>
                </li>
            </ul>
        </div>
        <div style="margin-top:9px;">
            <!--设备表格-->
            <win-table border resizable 
                        class="deviceTable"  
                        :show-selection="false" :show-index="false"
                       @cell-dblclick="view" 
                       @select-all="handleSelectAll" @select-change="handleSelectChange"
                       :data="deviceList" :loading="deviceLoading"
                       >
                <win-table-column type="selection" width="60" ></win-table-column>
                <win-table-column type="index" width="60" title="序号"></win-table-column>
                <win-table-column field="name" title="设备名称" min-width="130"></win-table-column>
                <win-table-column field="alias" title="设备别名" min-width="130"></win-table-column>
                <win-table-column field="ipAddress" title="ip地址" min-width="100"></win-table-column>
                <win-table-column field="desc" title="描述" min-width="130"></win-table-column>
                <win-table-column field="osType" title="系统类型" min-width="80"></win-table-column>
                <win-table-column field="userName" title="用户名" min-width="80"></win-table-column>
                <win-table-column field="port" title="端口号" min-width="60"></win-table-column>
                <win-table-column field="createTime" title="创建时间" min-width="100" :formatter="formatDeviceTable"></win-table-column>
                <win-table-column field="status" title="连接状态" min-width="80">
                    <template v-slot="{ row }" >
                        <el-tag :type="deviceStatusType(row.status)" >{{formatDeviceStatus(row.status)}}</el-tag>
                    </template>
                </win-table-column>
                <win-table-column title="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button size="mini" type="text" icon="el-icon-edit" @click="deviceOperation(row,'UPDATE')">编辑</el-button>
                        <el-button size="mini" type="text" icon="el-icon-delete" @click="delDeviceOne(row)">删除</el-button>
                    </template>
                </win-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </win-table>
            <!--分页组件-->
            <div class="page-contanier">
                <win-pagination name="devicePage" v-bind:pageInfo="pageVO" @pageInfoChange="devicePageQuery"></win-pagination>
            </div>
            <!-- 设备弹框begin：新增/删除 -->
            <DeviceDialog v-if="isDeviceDialog" :toDeviceChildMsg="deviceDialogMsg" @bindDeviceSend="toDeviceDialogForm" ></DeviceDialog>
            <!-- 设备弹框end：新增/删除 -->
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import DeviceController from "../controller/DeviceController";

    @Component({})
    export default class device extends DeviceController {}
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
    .deviceTable /deep/ {
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

