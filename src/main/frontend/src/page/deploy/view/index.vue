<template>
    <div>
        <win-row :gutter="32">
            <!-- 最外层布局：左16，右8 -->
            <win-col :span="16">
                <!-- 左侧布上下两行布局 行间距20-->
                <win-row :gutter="20">
                    <!-- 左侧上面行布局：左右同等表单 -->
                    <win-col :span="12">
                        <win-form class="statisticsForm" label-position="right"  label-width="120px">
                            <!-- 第一行 操作栏 -->
                            <win-row>
                                <div style="float: right;">
                                    <win-button icon="el-icon-s-grid" style="background:rgba(37,44,87,1);" @click="toTaskVue" ></win-button>
                                </div>
                            </win-row>
                            <!-- 仪表行 -->
                            <win-row>
                                <win-col :span="12">
                                    <div class="title-h2">
                                        <span>已发布任务：6</span>
                                    </div>
                                </win-col>
                                <win-col :span="12">
                                    <el-progress type="dashboard" :percentage="60" :color="'rgb(19, 206, 102)'"></el-progress>
                                </win-col>
                            </win-row>
                            <!-- 明细行 -->
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="任务总数:">
                                        <div style="color: #fff;">
                                            <span>10</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="100" :color="'#409eff'"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="未发布:">
                                        <div style="color: #fff;">
                                            <span> 2</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="20" :color="'orange'"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="发布异常:">
                                        <div style="color: #fff;">
                                            <span>2</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="20" :color="'red'"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                        </win-form>
                    </win-col>
                    <win-col :span="12">
                        <win-form class="statisticsForm">
                            <win-row>
                                <div style="float: right;">
                                    <win-button icon="el-icon-s-grid" style="background:rgba(37,44,87,1);" @click="toAppModuleVue" ></win-button>
                                </div>
                            </win-row>
                            <!-- 仪表行 -->
                            <win-row>
                                <win-col :span="12">
                                    <div class="title-h2">
                                        <span>已启动服务：6</span>
                                    </div>
                                </win-col>
                                <win-col :span="12">
                                    <el-progress type="dashboard" :percentage="60" :color="'rgb(19, 206, 102)'"></el-progress>
                                </win-col>
                            </win-row>
                            <!-- 明细行 -->
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="服务总数:">
                                        <div style="color: #fff;">
                                            <span>10</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="100" :color="'#409eff'"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="未启动:">
                                        <div style="color: #fff;">
                                            <span> 2</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="20" :color="'orange'"></el-progress>
                                    </div>
                                </win-col>
                            </win-row>
                            <win-row type="flex">
                                <win-col :span="7">
                                    <win-form-item label="启动异常:">
                                        <div style="color: #fff;">
                                            <span>2</span>
                                        </div>
                                    </win-form-item>
                                </win-col>
                                <win-col :span="17">
                                    <div class="progress">
                                        <el-progress :percentage="20" :color="'red'"></el-progress>
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
                                <div class="quick-image">
                                    <i icon="el-icon-s-platform"></i>
                                </div>
                                <div>
                                    添加设备
                                </div>
                            </el-card>
                        </win-form-item>
                        <win-form-item >
                            <el-card class="quick-card" shadow="always">
                                <div>
                                    上传应用包
                                </div>
                            </el-card>
                        </win-form-item>
                        <win-form-item >
                            <el-card class="quick-card" shadow="always">
                                <div>
                                    一键部署
                                </div>
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
                <win-table border stripe class="log-table"
                        :show-selection="false" :show-index="false" 
                        :data="logTableData" >
                    <win-table-column title="用户" field="name" ></win-table-column>
                    <win-table-column title="操作" field="operate"></win-table-column>
                    <win-table-column title="时间" field="createTime" ></win-table-column>
                    
                </win-table>
                <!--分页组件-->
                <div class="page-contanier">
                    <win-pagination name="logPage" v-bind:childMsg="logPageVO" @callFather="logPageQuery"></win-pagination>
                </div>
            </win-col>
        </win-row>
        

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
            font-size: 30px;
            color: #E0E104;
            background-color: #fff
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
