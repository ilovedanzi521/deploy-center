import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import { LogDetailVO, LogQueryReqVO } from "../vo/LogVO";
import DeployService from "../service/DeployService";
import dateUtils from "win-biz/common/util/DateUtils";
import { StatisticsRepVO } from "../vo/DeployVO";
import { Emit } from "vue-property-decorator";
import { OperationTypeEnum, PageVO } from "win-biz";
import { GroupTreeVO } from "../vo/GroupVO";
import { DialogTitleConst, DeployBaseUrl } from "../const/DeployConst";
import { DeployTypeEnum } from "../const/DeployEnum";
import { UploadVO } from "../vo/AppModuleVO";
import GroupDialog from "../view/groupDialog.vue";
import UploadDialog from "../view/uploadDialog.vue";
import TaskDialog from "../view/taskDialog.vue";
import { TaskTableVO } from "../vo/TaskVO";

@Component({ components: {GroupDialog,UploadDialog,TaskDialog}  })

export default class IndexController extends BaseController {
    // 服务接口
    private deployService: DeployService=new DeployService();
    // 日志表格数据
    private logTableData: LogDetailVO[]=[];
    // 查询条件
    private logQueryVO: LogQueryReqVO=new LogQueryReqVO();
    // 表格加载进度
    private logLoading: boolean = false;
    private taskLoading: boolean = false;
    private appLoading: boolean = false;
    private taskStatistics: StatisticsRepVO = new StatisticsRepVO();
    private appStatistics: StatisticsRepVO = new StatisticsRepVO();



    // 初始化
    mounted(){
        this.pageVO.pageSize=25;
        this.initTaskStatistics();
        this.initAppStatistics();
        this.queryLogPageList(this.logQueryVO);
    }
    // 初始化发布任务统计数据
    async initTaskStatistics() {
        this.taskLoading = true;
        await this.deployService.indexTaskStatistics()
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.taskStatistics = winResponseData.data;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.taskLoading = false;
            });
    }
    // 初始化应用服务实例统计数据
    async initAppStatistics() {
        this.appLoading = true;
        await this.deployService.indexAppInstanceStatistics()
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.appStatistics = winResponseData.data;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.appLoading = false;
            });
    }
    // 查分页数据列表
    async queryLogPageList(queryVO: LogQueryReqVO) {
        this.logLoading = true;
        await this.deployService.logPageList(queryVO)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.logTableData = winResponseData.data.list;
                    this.pageVO = winResponseData.data;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.logLoading = false;
            });
    }
    // 表格字段格式化
    formatGroupTable({ cellValue, row, rowIndex, column, columnIndex }) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
    }
    // 分页栏查询
    logPageQuery(pageVO: PageVO) {
        this.logQueryVO.reqPageNum = pageVO.pageNum;
        this.logQueryVO.reqPageSize = pageVO.pageSize;
        this.queryLogPageList(this.logQueryVO);
    }
    // 任务统计栏下拉框指令事件
    handleTaskCommand(command:string){
        if(command == "list"){
            this.toTaskVue();
        }else if(command == "refrash"){
            this.initTaskStatistics();
        }
    }
    // 应用服务统计栏下拉框指令事件
    handleAppCommand(command:string){
        if(command == "list"){
            this.toAppModuleVue();
        }else if(command == "refrash"){
            this.initAppStatistics();
        }
    }
    // 跳到任务列表页
    toTaskVue(){
        window.open("#/deploy/task");
        // this.gotoTabPath(menu);
    }
    // 跳到应用模块列表页
    toAppModuleVue(){
        window.open("#/deploy/appmodule");
        // this.gotoTabPath(menu);
    }
    // 跳到tab页
    @Emit("gotoPath")
    gotoTabPath(menu) {
        return menu;
    }
    // 弹框
    toAddDevice(){
        this.toGroupDialogData = {
            dialogTitle: DialogTitleConst.GROUP_ADD,
            type: OperationTypeEnum.ADD,
            data: null
        };
        this.showGroupDialog = true;
    }
    toUploadApp(){
        this.toUploadDialogData={
            dialogTitle: DialogTitleConst.APP_UPLOAD,
            type : DeployTypeEnum.UPLOAD,
            data : null
        }
        this.showUploadDialog = true;
    }
    toDeployTask(){
        this.toTaskDialogData={
            dialogTitle: DialogTitleConst.TASK_ADD,
            type: OperationTypeEnum.ADD,
            data: null,
        };
        this.showTaskDialog = true;
    }
    
    // 进度条颜色
    progressColor(type:string){
        if(type=="success"){
            return '#55D8FE';
        }else if(type=="info"){
            return '#D094D7';
        }else if(type=="warning"){
            return '#F7BA2A';
        }else if(type=="error"){
            return '#FC5660';
        }
    }

    // #################### 弹框 ###################
    // 添加设备
    // 设备组
    private showGroupDialog: boolean=false;
    /** 设备组弹框信息*/
    private toGroupDialogData: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: GroupTreeVO;
    };
    // 子组件回调函数
    backFromGroupDialog(msg: WinRspType) {
        this.showGroupDialog = false;
        if (msg === WinRspType.SUCC) {
            this.win_message_box_success("是否进入设备管理页查看？","添加成功")
                .then(() => {
                    window.open("#/deploy/group");
                });
        }
    }

    //上传应用
    /** 是否显示设备组弹框*/
    private showUploadDialog: boolean = false;
    /** 上传弹框信息*/
    private toUploadDialogData: {
        dialogTitle: string;
        type: DeployTypeEnum;
        data: UploadVO;
    };
    // 子组件回调函数:如果上传成功关闭弹框并刷新表格
    backFromUploadDialog(msg: WinRspType) {
        this.showUploadDialog = false;
        if (msg === WinRspType.SUCC) {
            this.win_message_box_success("是否进入应用管理页查看？","上传成功")
            .then(() => {
                window.open("#/deploy/appmodule");
            });
        }
    }

    //一键部署
    // 展开弹框
    private showTaskDialog: boolean = false;
    /** 弹框信息*/
    private toTaskDialogData: {
        dialogTitle: string,
        type: OperationTypeEnum,
        data: TaskTableVO,
    };
    // 子组件回调函数
    backFromTaskDialog(msg: WinRspType) {
        this.showTaskDialog = false;
        if (msg === WinRspType.SUCC) {
            this.initTaskStatistics();
            this.initAppStatistics();
            this.win_message_box_success("是否进入任务管理页查看？","添加成功")
            .then(() => {
                window.open("#/deploy/task");
            });
        }
    }

}
