import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import {StrategyRepVO, StrategyReqVO} from "../vo/StrategyVO";
import dateUtils from "../../common/util/DateUtils";
import { QueryReqVO } from "../vo/DeployVO";
import StrategyDialog from "../view/strategyDialog.vue"
import { BaseConst, OperationTypeEnum } from "win-biz";


@Component({components: {StrategyDialog}})
export default class StrategyManagementController extends BaseController {
    /** 部署服务*/
    private deployService: DeployService = new DeployService();

    /** 查询对象 策略列表*/
    private strategyList: StrategyRepVO[]=[];
    /*组表格加载中*/
    private tableLoading: boolean = false;
    /*选中项*/
    private selected: any = [];
    private toDialogMsg: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: any;
    };
    private queryReqVO: QueryReqVO=new QueryReqVO();

    private showDialog:boolean=false;

    /** 初始化*/
    private mounted() {
        this.strategyPageList(this.queryReqVO);
    }

    /**
     * 策略列表查询
     *
     * @param vo
     */
    public strategyPageList(queryReqVO: QueryReqVO) {
        this.tableLoading = true;
        this.deployService.strategyPageList(queryReqVO)
        .then((winResponseData: WinResponseData) =>{
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.strategyList = winResponseData.data.list;
                this.pageVO = winResponseData.data;
            } else {
                this.win_message_error(winResponseData.msg);
            }
        })
        .finally(() =>{
            this.tableLoading = false;
        });
    }
/**
     * 分页按钮查询
     *
     * @param vo
     */
    public strategyPageQuery(pageVO: PageVO) {
        this.queryReqVO.reqPageNum = pageVO.pageNum;
        this.queryReqVO.reqPageSize = pageVO.pageSize;
        this.strategyPageList(this.queryReqVO);
    }

       /** 查看策略详情*/
    showStrategyDetail(row: StrategyRepVO){
        console.log(row);
        this.toDialogMsg = {
            dialogTitle: "详情-" + row.name,
            type: OperationTypeEnum.VIEW,
            data: row
        };
        this.showDialog=true;
    }
    // 子组件回调函数
    private bindDialogSend(msg: WinRspType) {
        this.showDialog = false;
        if (msg === WinRspType.SUCC) {
            this.strategyPageList(this.queryReqVO);
        }
    }
    /**删除策略 */
    public delStrategy(row: StrategyRepVO){
        console.log("***********delStrategy************");
        console.log(this.selected);
    }

  

    // 双击查看
    private view({ cellValue, row, rowIndex, column, columnIndex }) {
       console.log("双击查看");
    }

    // 选中行事件
    private handleSelectChange({ selection }) {
        this.selected=selection;
    }

    /**
     * 全选操作
     */
    private handleSelectAll({ selection, checked }) {
        this.selected=selection;
    }
    
      // 表格字段格式化
      private formatGroupTable({ cellValue, row, rowIndex, column, columnIndex }) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
    }
}

