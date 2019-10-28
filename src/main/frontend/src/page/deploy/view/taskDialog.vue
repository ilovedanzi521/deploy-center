<template>
    <win-fdialog width="30%"  :title="dialogTitle" class="taskDialog" :before-close="closeDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false">
        <win-form class="taskform" :inline="true" :rules="formRules" :model="taskReqVO" ref="taskForm" label-position="left" label-width="80px">
            <div class="hr">
                
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
        <div slot="footer" class="dialog-footer">
            <win-button @click="closeDialog">取消</win-button>
            <win-button type="primary" :loading="submitLoading" @click="submitDialog('taskForm')">确认</win-button>
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
   

</style>