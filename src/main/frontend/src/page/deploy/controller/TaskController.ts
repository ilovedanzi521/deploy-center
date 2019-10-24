
import Component from "vue-class-component";
import { QueryReqVO } from "../vo/DeployVO";
import TaskTableVO from "../vo/TaskVO";
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
    /** 初始化*/
    private mounted() {
        this.queryPageList(this.queryReqVO);
    }
    // 分页查询
    private pageQuery(pageVO: PageVO) {
        this.queryReqVO.reqPageNum = pageVO.pageNum;
        this.queryReqVO.reqPageSize = pageVO.pageSize;
        this.queryPageList(this.queryReqVO);
    }
    // 分页数据
    private queryPageList(queryReqVO: QueryReqVO){
        this.tableLoading = true;
        this.deployService.taskPageList(queryReqVO)
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.pageVO = winResponseData.data;
                    this.taskTableData = this.pageVO.list;
                } else {
                    this.win_message_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.tableLoading = false;
            });
    }
    // 添加任务按钮
    private addTask(){
        this.toDialogMsg={
            dialogTitle: DialogTitleConst.TASK_ADD,
            type: OperationTypeEnum.ADD,
            data: null,
        };
        this.showDialog = true;
    }
    // 表格字段格式化
    private formatCellValue({row, column, cellValue, index}) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
    }

    
    // 子组件回调函数
    private bindDialogSend(msg: WinRspType) {
        this.showDialog = false;
        if (msg === WinRspType.SUCC) {
            this.queryPageList(this.queryReqVO);
        }
    }
}