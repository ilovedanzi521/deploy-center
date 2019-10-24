
import Component from "vue-class-component";
import { QueryReqVO } from "../vo/DeployVO";
import TaskTableVO from "../vo/TaskVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import { PageVO, WinRspType, WinResponseData } from "win-biz";
import dateUtils from "win-biz/common/util/DateUtils";

@Component({})
export default class TaskController extends BaseController{
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    /** 分页对象 */
    public pageVO: PageVO = new PageVO();
    private queryReqVO: QueryReqVO=new QueryReqVO();
    private taskTableData: TaskTableVO[]=[];
    private tableLoading: boolean = false;
    /** 初始化*/
    private mounted() {
        this.queryPageList(this.queryReqVO);
    }

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
    private addTask(){
        this.win_message_box_info("添加任务");
    }
    // 表格字段格式化
    private formatCellValue({row, column, cellValue, index}) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
    }
}