<template>
    <win-fdialog width="50%"  :title="dialogTitle" class="strategyDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false">
        <win-form class="strategyDetailForm" :inline="true" :model="strategyDetailVO"  ref="strategyDetailForm" label-position="top" label-width="60px">
            <div class="hr">
                <win-col :span="6">
                        <win-form-item label="策略名称:">
                            <win-input v-model="strategyDetailVO.name" readonly/>
                        </win-form-item>
                        <win-form-item label="描述:">
                            <win-input v-model="strategyDetailVO.desc" readonly type="textarea" :rows="4" size="mini" style="width:200px"/>
                        </win-form-item>
                        <win-form-item label="创建时间:">
                            <win-input v-model="strategyDetailVO.createTime" readonly/>
                        </win-form-item>
                        <win-form-item label="更新时间:">
                            <win-input v-model="strategyDetailVO.updateTime" readonly/>
                        </win-form-item>
                </win-col>
                <win-col :span="18">
                        <el-card class="box-card" >
                                <div slot="header" class="clearfix">
                                        <span>脚本详情({{strategyDetailVO.path}})</span>
                                </div>
                                <!-- 注意需要给 el-scrollbar 设置高度，判断是否滚动是看它的height判断的 -->
                                <el-scrollbar style="height: 100%;"> <!-- 滚动条 -->
                                    <div v-for="item in strategyDetailVO.shellContent" :key="item" class="logtext">
                                            {{item}}
                                     </div>
                                </el-scrollbar><!-- /滚动条 -->
                        </el-card>
                </win-col>
                    
            </div>
        </win-form>
    </win-fdialog>
</template>
<script lang="ts">
    import Component from "vue-class-component";
    import StrategyDialogController from "../controller/StrategyDialogController";
    @Component
    export default class StrategyDialog extends StrategyDialogController{}
</script>
<style lang="scss" scoped>
.el-textarea__inner {
    width: 100%;
}
</style>