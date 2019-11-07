<template>
    <div>
        <win-row :gutter="32">
            <!-- 最外层布局：左16，右8 -->
            <win-col :span="16">
                <!-- 左侧布上下两行布局 行间距20-->
                <win-row :gutter="20">
                    <!-- 左侧上面行布局：左右同等表单 -->
                    <win-col :span="12">
                        <win-form class="statisticsForm" label-position="right"  label-width="120px" v-loading="taskLoading" :model="taskStatistics">
                            <!-- 第一行 操作栏 -->
                            <win-row>
                                <div style="float: right;">
                                    <el-dropdown @command="handleTaskCommand">
                                        <win-button icon="el-icon-menu" style="background:rgba(37,44,87,1);" ></win-button>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item icon="el-icon-s-grid" command="list">任务列表</el-dropdown-item>
                                            <el-dropdown-item icon="el-icon-refresh" command="refrash">刷新</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </win-row>
                            <!-- 仪表行 -->
                            <win-row>
                                <win-col :span="12">
                                    <div class="title-h2">
                                        <span>已发布任务：{{taskStatistics.success}}</span>
                                    </div>
                                </win-col>
                                <win-col :span="12">
                                    <!-- <el-progress type="dashboard" :percentage="60" :color="'rgb(19, 206, 102)'"></el-progress> -->
                                    <el-progress type="dashboard" :percentage="taskStatistics.successPercent" :color="progressColor('success')"></el-progress>
                                </win-col>
                            </win-row>
                            <!-- 明细行 -->
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="任务总数:">
                                        <div style="color: #fff;">
                                            <span>{{taskStatistics.total}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <!-- <el-progress :percentage="100" :color="'#409eff'"></el-progress> -->
                                        <el-progress :percentage="100" :color="progressColor('info')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="警告:">
                                        <div style="color: #fff;">
                                            <span>{{taskStatistics.warning}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <!-- <el-progress :percentage="20" :color="'orange'"></el-progress> -->
                                        <el-progress :percentage="taskStatistics.warningPercent" :color="progressColor('warning')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="异常:">
                                        <div style="color: #fff;">
                                            <span>{{taskStatistics.error}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <!-- <el-progress :percentage="20" :color="'orange'"></el-progress> -->
                                        <el-progress :percentage="taskStatistics.errorPercent" :color="progressColor('error')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                        </win-form>
                    </win-col>
                    <win-col :span="12">
                        <win-form class="statisticsForm" label-position="right" label-width="120px" v-loading="appLoading" :model="appStatistics">
                            <win-row>
                                <div style="float: right;">
                                    <el-dropdown @command="handleAppCommand">
                                        <win-button icon="el-icon-menu" style="background:rgba(37,44,87,1);" ></win-button>
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item icon="el-icon-s-grid" command="list">应用列表</el-dropdown-item>
                                            <el-dropdown-item icon="el-icon-refresh" command="refrash">刷新</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                            </win-row>
                            <!-- 仪表行 -->
                            <win-row>
                                <win-col :span="12">
                                    <div class="title-h2">
                                        <span>已启动服务：{{appStatistics.success}}</span>
                                    </div>
                                </win-col>
                                <win-col :span="12">
                                    <!-- <el-progress type="dashboard" :percentage="60" :color="'rgb(19, 206, 102)'"></el-progress> -->
                                    <el-progress type="dashboard" :percentage="appStatistics.successPercent" :color="progressColor('success')"></el-progress>
                                </win-col>
                            </win-row>
                            <!-- 明细行 -->
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="服务总数:">
                                        <div style="color: #fff;">
                                            <span>{{appStatistics.total}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="100" :color="progressColor('info')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="警告:">
                                        <div style="color: #fff;">
                                            <span>{{appStatistics.warning}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="appStatistics.warningPercent" :color="progressColor('warning')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="异常:">
                                        <div style="color: #fff;">
                                            <span>{{appStatistics.error}}</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="appStatistics.errorPercent" :color="progressColor('error')"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                        </win-form>
                    </win-col>
                </win-row>
                <win-row>
                    <!-- 左侧下面行布局：表单卡片 -->
                    <win-form class="quick-form" label-width="30px">
                        <win-form-item>
                            <div class="title-h1">
                                <span>快速指引</span>
                            </div>
                        </win-form-item>
                        <win-form-item >
                            <el-card class="quick-card" shadow="always">
                                <win-col :span="2">
                                    <div class="quick-image">
                                        <img src="../../../assets/image/deploy/device.svg"/>
                                    </div>
                                </win-col>
                                <win-col :span="22">
                                    <ul>
                                        <li><el-link type="primary" style="font-size:20px;" @click="toAddDevice">添加设备</el-link></li>
                                        <li><div>新增设备组，将应用快速部署到一批设备上。</div></li>
                                    </ul>
                                </win-col>
                            </el-card>
                        </win-form-item>
                        <win-form-item >
                            <el-card class="quick-card" shadow="always">
                                <win-col :span="2">
                                    <div class="quick-image">
                                        <img src="../../../assets/image/deploy/upload.svg" />
                                    </div>
                                </win-col>
                                <win-col :span="22">
                                    <ul>
                                        <li><el-link type="primary" style="font-size:20px;" @click="toUploadApp">上传应用包</el-link></li>
                                        <li><div>上传应用部署包，实现自动升级、解压、安装、部署。</div></li>
                                    </ul>
                                </win-col>
                            </el-card>
                        </win-form-item>
                        <win-form-item >
                            <el-card class="quick-card" shadow="always">
                                <win-col :span="2">
                                    <div class="quick-image">
                                        <img src="../../../assets/image/deploy/deploy.svg" />
                                    </div>
                                </win-col>
                                <win-col :span="22">
                                    <ul>
                                        <li><el-link type="primary" style="font-size:20px;"  @click="toDeployTask">一键部署</el-link></li>
                                        <li><div>创建发布任务，选择一种策略和设备组，一键部署多个应用到目标机器上。</div></li>
                                    </ul>
                                </win-col>
                            </el-card>
                        </win-form-item>
                    </win-form>
                </win-row>
            </win-col>
            <win-col :span="8">
                <div class="title-h1 log">
                    <span>操作日志</span>
                </div>
                <!-- 右侧布局：表格 -->
                <win-table border stripe class="log-table" :loading="logLoading"
                        :show-selection="false" :show-index="false" 
                        :data="logTableData" >
                    <win-table-column title="用户" field="username" ></win-table-column>
                    <win-table-column title="操作" field="operation"></win-table-column>
                    <win-table-column title="时间" field="createTime"  :formatter="formatGroupTable"></win-table-column>
                </win-table>
                <!--分页组件-->
                <div class="page-contanier">
                    <win-pagination v-bind:pageInfo="pageVO" @pageInfoChange="logPageQuery"></win-pagination>
                </div>
            </win-col>
        </win-row>
        <!-- 弹框 -->
        <GroupDialog v-if="showGroupDialog" :toChildMsg="toGroupDialogData" @bindSend="backFromGroupDialog" ></GroupDialog>
        <!--上传应用弹框 -->
        <UploadDialog v-if="showUploadDialog" :toChildMsg="toUploadDialogData" @bindSend="backFromUploadDialog" ></UploadDialog>
        <!-- 添加任务弹框 -->
        <TaskDialog v-if="showTaskDialog" :toChildMsg="toTaskDialogData" @bindSend="backFromTaskDialog"></TaskDialog>
    </div>
</template>

<script lang="ts">

    import IndexController from "../controller/IndexController";
    import Component from "vue-class-component";

    @Component({})
    export default class Index extends IndexController {}

</script>

<style lang="scss" scoped>
// 统计表单样式
.statisticsForm /deep/ {
    margin: 5px;
    height:284px;
    background:rgba(12,22,63,1);
    opacity:1;
    .el-form-item{
        margin-bottom: 5px !important;
    }
}
.text-statistics {
    color: #fff;
    // float: right;
}
.progress {
    margin-top: 10px;
    margin-right: 30px;
}
// 快速指引表单样式
.quick-form /deep/ {
    margin: 5px;
    height:652px;
    background:rgba(12,22,63,1);
    box-shadow:0px 2px 6px rgba(0,0,0,0.04);
    opacity:1;
    .quick-card {
        margin-right: 10px;
        height:135px;
        background:rgba(37,44,87,1);
        opacity:1;
        .quick-image {

            margin-top: 15px;
        }
    }
}
.title-h1 {
    font-size: 20px;
    color: #fff;
    background-color:#0C163F;
}
.title-h2 {
    font-size: 20px;
    color:rgba(224,225,4,1) ;
    padding: 30px;
    margin-left: 20px;
}
.title-h1.log {
    margin: 8px;
}
.log-table {
    min-height: 100%;
    padding-bottom:80px;
}

.page-contanier {
    position:fixed;
    bottom: 30px;
    width: 100%;
    right: 10px;
}

</style>
