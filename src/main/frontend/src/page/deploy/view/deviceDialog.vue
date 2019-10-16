<template>
    <el-dialog width="30%" class="DeviceDialog" :title="deviceDialogTitle" :visible.sync="deviceDialogVisible" append-to-body :close-on-click-modal="false" :close-on-press-escape="false" >
        <win-form class="form" :inline="true" :rules="deviceRules" :model="deviceRepVO" ref="deviceForm" :disabled="false" label-position="left" label-width="80px" v-testName="{'TEST_NAME':'DeviceDialog_wz'}">
                <div class="hr">
                    <win-row>
                        <win-col :span="12">
                            <win-form-item label="设备名称" prop="name">
                                <win-input v-model="deviceRepVO.name" maxlength=50 clearable></win-input>
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
                                <win-input v-model="deviceRepVO.alias" maxlength="50" readonly></win-input>
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
                                <span v-bind="deviceRepVO.status">{{deviceStatusText}}</span>
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
</template>

<script lang="ts">
    import Component from "vue-class-component";
    import DeviceDialogController from "../controller/DeviceDialogController";
    @Component({})
    export default class DeviceDialog extends DeviceDialogController{}
</script>

<style lang="scss" scoped>

</style>
