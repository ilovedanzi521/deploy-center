import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import {StrategyRepVO} from "../vo/StrategyVO";
import dateUtils from "../../common/util/DateUtils";


@Component({})
export default class StrategyManagementController extends BaseController {
    /** 部署服务*/
    private deployService: DeployService = new DeployService();

    /** 查询对象 策略列表*/
    private strategyList: StrategyRepVO[]=[];
    /*组表格加载中*/
    private groupLoading: boolean = false;
    /*选中项*/
    private selected: any = [];

    /** 初始化*/
    private mounted() {
        this.strategyQuery();
    }

    public rowClassName({ row, rowIndex }){
        if (!row.ipAddress) {
            return 'row-orange';
        }
    }
    public cellClassName({ row, rowIndex, column, columnIndex }){
        if(row.ipAddress && columnIndex === 0){
            return 'col-checkbox-none';
        }
    }

    /**
     * 策略列表查询
     *
     * @param vo
     */
    public strategyQuery() {
        this.deployService.strategyList()
        .then((winResponseData: WinResponseData) =>{
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.strategyList = winResponseData.data;
            } else {
                this.win_message_error(winResponseData.msg);
            }
        })
        .finally(() =>{
            this.groupLoading = false;
        });
    }


       /** 查看策略详情*/
       public showStrategyDetail(row: StrategyRepVO){
        console.log(row);
       
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

