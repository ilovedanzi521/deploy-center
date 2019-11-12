
import Component from "vue-class-component";
import { QueryReqVO } from "../vo/DeployVO";
import TaskTableVO, { TaskTableVO } from "../vo/TaskVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import { PageVO, WinRspType, WinResponseData, OperationTypeEnum } from "win-biz";
import dateUtils from "win-biz/common/util/DateUtils";
import TaskDialog from "../view/taskDialog.vue";
import { DialogTitleConst } from "../const/DeployConst";

@Component({ components: {TaskDialog} })
export default class TaskController extends BaseController{
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    /** 分页对象 */
    public pageVO: PageVO = new PageVO();
    private queryReqVO: QueryReqVO=new QueryReqVO();
    // 列表数据
    private taskTableData: TaskTableVO[]=[];
    // 列表进度
    private tableLoading: boolean = false;
    // 展开弹框
    private showDialog: boolean = false;

    /** 弹框信息*/
    private toDialogMsg: {
        dialogTitle: string,
        type: OperationTypeEnum,
        data: TaskTableVO,
    };

    // 方法区
    /** 初始化*/
    mounted() {
        this.queryPageList(this.queryReqVO);
    }
    // 分页查询
    pageQuery(pageVO: PageVO) {
        this.queryReqVO.reqPageNum = pageVO.pageNum;
        this.queryReqVO.reqPageSize = pageVO.pageSize;
        this.queryPageList(this.queryReqVO);
    }
    // 分页数据
    queryPageList(queryReqVO: QueryReqVO){
        this.tableLoading = true;
        this.deployService.taskPageList(queryReqVO)
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.pageVO = winResponseData.data;
                    this.taskTableData = this.pageVO.list;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.tableLoading = false;
            });
    }
    // 添加任务按钮
    addTask(){
        this.toDialogMsg={
            dialogTitle: DialogTitleConst.TASK_ADD,
            type: OperationTypeEnum.ADD,
            data: null,
        };
        this.showDialog = true;
    }
    refresh(){
        this.queryPageList(this.queryReqVO);
    }
    // 表格字段格式化
    formatCellValue({row, column, cellValue, index}) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }else if(column.property === "status"){
            if(cellValue === 0){
                return "未部署";
            }else if(cellValue === 1){
                return "部署中...";
            }else if(cellValue === 2){
                return "部署成功";
            }else if(cellValue === 3){
                return "部署失败";
            }else if(cellValue === 4){
                return "卸载中...";
            }else if(cellValue === 5){
                return "卸载成功";
            }else if(cellValue === 6){
                return "卸载失败";
            }
        }
    }

    
    // 子组件回调函数
    bindDialogSend(msg: WinRspType) {
        this.showDialog = false;
        if (msg === WinRspType.SUCC) {
            this.queryPageList(this.queryReqVO);
        }
    }
    // 部署按钮
    deploy(row: TaskTableVO){
        console.log("*************deploy");
        console.log(row);
        // const h = this.$createElement;
        if(this.isDeployTask(row)){
            this.win_message_box_warning('确认部署任务['+row.id+']?')
                .then(() => {
                    this.deployService.taskDeploy(row.id)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.win_message_success(winResponseData.msg);
                                this.queryPageList(this.queryReqVO);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
                            }
                        });
                    
                });
        }else{
            this.win_message_box_error("当前状态任务无法部署！");
        }
    }
    // 卸载按钮
    unDeploy(row: TaskTableVO){
        if(this.isUnDeployTask(row)){
            this.win_message_box_info('确认卸载任务['+row.id+']?')
                .then(() => {
                    this.deployService.taskUnDeploy(row.id)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.win_message_success(winResponseData.msg);
                                this.queryPageList(this.queryReqVO);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
                            }
                        });
                        
                });
        }else{
            this.win_message_box_error("当前状态任务无法卸载！");
        }
    }
    
    remove(row: any){
        if(this.isRemoveTask(row)){
            this.win_message_box_info('确认删除任务['+row.id+']?')
                .then(() => {
                    this.deployService.removeTaskById(row.id)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.win_message_success(winResponseData.msg);
                                this.queryPageList(this.queryReqVO);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
                            }
                        });
                        
                });
        }else{
            this.win_message_box_error("当前状态任务无法删除！");
        }
    }
    // 是否可部署
    isDeployTask(row: any) {
        // 未部署、部署失败、卸载成功的任务可部署
        if(row.status===0 ||row.status===3 ||row.status ===5){
            return true;
        }
        return false;
    }
    // 是否可卸载
    isUnDeployTask(row: any) {
        // 部署成功、卸载失败任务可卸载
        if(row.status===2 ||row.status===6){
            return true;
        }
        return false;
    }
    // 是否可删除
    isRemoveTask(row:any){
        // 未部署、部署失败、卸载成功的任务可删除
        if(row.status===0 || row.status===3||row.status===5){
            return true;
        }
        return false;
    }


    // 查看日志按钮
    viewLog(row: TaskTableVO){
        this.toDialogMsg={
            dialogTitle: DialogTitleConst.TASK_DETAIL,
            type: OperationTypeEnum.VIEW,
            data: row,
        };
        this.showDialog = true;
    }

    percentage(row: TaskTableVO){
        if(row.status === 1 || row.status === 4){
            return 50;
        }
    }
}