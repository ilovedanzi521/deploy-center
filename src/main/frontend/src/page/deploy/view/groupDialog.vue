<template>
    <win-fdialog :title="dialogTitle" class="GroupDialog" :before-close="closeGroupDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false" width="1000px">
        <win-form class="form" :inline="true" :rules="rules" :model="groupReqVO" ref="groupForm" :disabled="allDisabled" label-position="left" label-width="80px" v-testName="{'TEST_NAME':'GroupDialog_wz'}">
            <div class="hr">
                <win-row>
                    <win-col :span="8">
                        <win-form-item label="组名称" prop="name">
                            <win-input v-model="groupReqVO.name" class="form-input" :rules="['number','word','chinese']" maxlength=50></win-input>
                        </win-form-item>
                        <win-form-item label="组描述" prop="desc">
                            <win-input type="textarea" v-model="groupReqVO.desc" class="form-textarea" maxlength="100" show-word-limit clearable></win-input>
                        </win-form-item>
                    </win-col>
                    <win-col :span="16">
                        <win-form-item>
                            <el-transfer :titles="['可选设备', '已选设备']" v-model="groupReqVO.deviceIds" :data="deviceTransferData">
                                <div slot="left-footer">
                                        <el-button-group>
                                            <win-button type="info" icon="el-icon-plus" size="small" @click="deviceOperation('','ADD')">新增设备</win-button>
                                            <win-button v-popover:popover1 type="info" icon="el-icon-delete" size="small">删除设备</win-button>
                                        </el-button-group>
                                </div>
                            </el-transfer>
                        </win-form-item>
                    </win-col>
                </win-row>
            </div>
        </win-form>
        <div slot="footer" class="dialog-footer" v-show="buttonShow">
            <win-button @click="closeGroupDialog">取消</win-button>
            <win-button type="primary" :loading="saveLoading" @click="submitDialog('groupForm')">{{dialogSumbitText}}</win-button>
        </div>
        <DeviceDialog v-if="isDeviceDialog" :toDeviceChildMsg="deviceDialogMsg" @bindDeviceSend="toDeviceDialogForm" ></DeviceDialog>
    </win-fdialog>

</template>
<script lang="ts">
    import Component from "vue-class-component";
    import GroupDialogController from "../controller/GroupDialogController";
    @Component
    export default class GroupDialog extends GroupDialogController{}
</script>
<style lang="scss" scoped>
    .form-input /deep/ .el-input{
        .el-input__inner{
            background: #1f2640;
        }
    }
    .form-textarea /deep/ .el-textarea {
        .el-textarea__inner {
            width: 200px;
            height: 200px;
            background: #1f2640;
        }
        .el-input__inner{
            width: 100px;
        }
        .el-input__count {
            color: #d6d7d7;
        }
    }
    .device-textarea /deep/ .el-textarea {
        .el-textarea__inner {
            width: 200px;
        }
        .el-input__count {
             color: #d6d7d7;
         }
    }
</style>
