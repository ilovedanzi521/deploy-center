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
        <el-dialog width="30%" :title="deviceDialogTitle" :visible.sync="deviceDialogVisible" append-to-body :close-on-click-modal="false" :close-on-press-escape="false" >
            <win-form class="form" :inline="true" :rules="deviceRules" :model="deviceRepVO" ref="deviceForm" :disabled="false" label-position="left" label-width="80px" v-testName="{'TEST_NAME':'DeviceDialog_wz'}">
                <div class="hr">
                    <win-row>
                        <win-col :span="12">
                            <win-form-item label="设备名称" prop="name">
                                <win-input v-model="deviceRepVO.name" maxlength=50 readonly></win-input>
                            </win-form-item>
                            <win-form-item label="ip地址" prop="ipAddress">
                                <win-input v-model="deviceRepVO.ipAddress" clearable></win-input>
                            </win-form-item>
                            <win-form-item label="根用户" prop="userName">
                                <win-input v-model="deviceRepVO.userName" maxlength="50" clearable></win-input>
                            </win-form-item>
                        </win-col>
                        <win-col :span="12">
                            <win-form-item label="设备别名" prop="alias">
                                <win-input v-model="deviceRepVO.alias" maxlength="50" clearable></win-input>
                            </win-form-item>
                            <win-form-item label="操作系统" prop="osType">
                                <win-input v-model="deviceRepVO.osType" maxlength="50" clearable></win-input>
                            </win-form-item>
                            <win-form-item label="端口号" prop="port">
                                <win-input v-model="deviceRepVO.port" maxlength="50" clearable></win-input>
                            </win-form-item>
                        </win-col>
                    </win-row>
                    <win-row>
                        <win-col :span="12">
                            <win-form-item label="设备描述" prop="desc">
                                <win-input type="textarea" v-model="deviceRepVO.desc" class="device-textarea" rows="3" maxlength="100" show-word-limit clearable></win-input>
                            </win-form-item>
                            <win-form-item label="连接状态：" prop="status">
                                <span v-model="deviceRepVO.status">{{deviceStatusText}}</span>
                            </win-form-item>
                        </win-col>
                    </win-row>
                </div>
            </win-form>
            <div slot="footer" class="dialog-footer" >
                <win-button type="info" @click="connectTest('deviceForm')">连接测试</win-button>
                <win-button @click="deviceDialogVisible = false">取消</win-button>
                <win-button type="primary"  @click="submitDeviceDialog('deviceForm')">{{deviceSubmitText}}</win-button>
            </div>
        </el-dialog>
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
