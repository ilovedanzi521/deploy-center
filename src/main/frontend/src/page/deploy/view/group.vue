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
            <win-table style="width: 100%;margin-bottom: 20px;" row-id="id" border resizable element-loading-text="拼命加载中..." indexFixed selectionFixed
                       :data="groupTreeList"
                       :tree-config="{children: 'children', expandAll: true}"
                       :loading="groupLoading">
                <win-table-column field="name" title="组（设备)名称" min-width="130" tree-node></win-table-column>
                <win-table-column field="ipAddress" title="ip地址" min-width="100"></win-table-column>
                <win-table-column field="desc" title="描述" min-width="130"></win-table-column>
                <win-table-column field="osType" title="系统类型" min-width="80"></win-table-column>
                <win-table-column field="userName" title="用户名" min-width="80"></win-table-column>
                <win-table-column field="port" title="端口号" min-width="60"></win-table-column>
                <win-table-column field="createTime" title="创建时间" min-width="100"></win-table-column>
                <win-table-column field="status" title="连接状态" min-width="80"></win-table-column>
                <vxe-table-column title="操作" min-width="100">
                    <template v-slot="{ row }">
                        <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
                    </template>
                </vxe-table-column>
                <template v-slot:empty>
                    <span style="color: red;">没有更多数据了！</span>
                </template>
            </win-table>
            <!--分页组件-->
            <div class="page-contanier">
<!--                <vxe-pager-->
<!--                    :loading="groupLoading"-->
<!--                    :current-page="pageVO.pageNum"-->
<!--                    :page-size="pageVO.pageSize"-->
<!--                    :total="pageVO.total"-->
<!--                    :layouts="['PrevPage', 'JumpNumber', 'NextPage', 'FullJump', 'Sizes', 'Total']"-->
<!--                    @page-change="groupPageQuery">-->
<!--                </vxe-pager>-->
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
</style>

