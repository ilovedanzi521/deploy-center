<template>
    <win-fdialog width="50%"  :title="dialogTitle" class="taskDialog" :before-close="closeDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false">
            <win-form v-if="toChildMsg.type==='ADD'" class="taskform" :inline="true" :rules="formRules" :model="taskReqVO"  ref="taskForm" label-position="left" label-width="80px">
                <div class="hr" >
                    <win-col :span="12">
                        <win-form-item label="部署策略:" prop="strategyId">
                            <win-select v-model="taskReqVO.strategyId" placeholder="请选择">
                                    <win-option
                                    v-for="item in strategyData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                                    </win-option>
                            </win-select>
                        </win-form-item>
                    </win-col>
                    <win-col :span="12">
                        <win-form-item label="设备组:" prop="groupId">
                                <win-select v-model="taskReqVO.groupId" placeholder="请选择">
                                    <win-option
                                    v-for="item in groupData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                                    </win-option>
                                </win-select>
                        </win-form-item>
                    </win-col>
                </div>
            </win-form>
            <win-form v-if="toChildMsg.type==='VIEW'" class="taskDetailForm" :inline="true" :model="taskDetailVO"  ref="taskDetailForm" label-position="right" label-width="60px">
                <div class="hr" v-if="toChildMsg.type==='VIEW'">
                    <win-col :span="8">
                        <win-form-item label="任务ID:" style="width:100%">
                            <win-input v-model="taskDetailVO.id" readonly />
                        </win-form-item>
                    </win-col>
                    <win-col :span="8">
                        <win-form-item label="策略名称:">
                            <win-input v-model="taskDetailVO.strategyName" readonly/>
                        </win-form-item>
                    </win-col>
                    <win-col :span="8">
                        <win-form-item label="设备组名:">
                            <win-input v-model="taskDetailVO.groupName" readonly/>
                        </win-form-item>
                    </win-col>
                    <win-col :span="8">
                        <win-form-item label="状态:">
                            <win-input v-model="taskDetailVO.status" readonly/>
                        </win-form-item>
                    </win-col>
                    <win-col :span="8">
                        <win-form-item label="创建时间:">
                            <win-input v-model="taskDetailVO.createTime" readonly/>
                        </win-form-item>
                    </win-col>
                    
                    <win-col :span="24">
                        <el-divider></el-divider>
                    </win-col>
                    <win-col :span="24">
                        <win-col :span="8">
                            <win-col :span="21">
                                <el-card class="box-card" >
                                    <div slot="header" class="clearfix">
                                            <span>设备列表</span>
                                    </div>
                                    <div v-for="item in taskDetailVO.devices" :key="item.id" class="text item">
                                        {{item.ipAddress}}
                                    </div>
                                    </el-card>
                            </win-col>
                            <win-col :span="21">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <span>应用列表</span>
                                    </div>
                                    <div v-for="item in taskDetailVO.appModules" :key="item.id" class="text item">
                                            {{item.name}}
                                    </div>
                                </el-card>
                            </win-col>
                        </win-col>
                        
                        <win-col :span="16">
                            <el-card class="box-card max-height">
                                    <div slot="header" class="clearfix">
                                        <span>日志详情({{taskDetailVO.logPath}})</span>
                                    </div>
                                    <div v-for="item in taskDetailVO.logInfo" :key="item" class="logtext">
                                            {{item}}
                                    </div>
                                    <!-- <win-input v-model="taskDetailVO.logInfo" type="textarea" rows="13" readonly/> -->
                            </el-card>
                        </win-col>
                    </win-col>
                </div>
            </win-form>
            <div slot="footer" class="dialog-footer">
                <win-button @click="closeDialog">取消</win-button>
                <win-button v-if="toChildMsg.type==='ADD'" type="primary" :loading="submitLoading" @click="submitDialog('taskForm')">确认</win-button>
            </div>
    </win-fdialog>

</template> 

<script lang="ts">
    import Component from "vue-class-component";
    import TaskDialogController from "../controller/TaskDialogController";

    @Component
    export default class TaskDialog extends TaskDialogController{}
</script>

<style lang="scss" scoped>
.clearfix:after {
    clear: both
  }
.box-card {
    margin-bottom: 18px;
    height: auto;
}
.box-card.max-height{
    height: 380px;
}
.text {
    font-size: 14px;
}
.logtext {
    font-size: 8px;
}
</style>