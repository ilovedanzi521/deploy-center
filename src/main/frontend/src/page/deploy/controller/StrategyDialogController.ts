import BaseController from "../../common/controller/BaseController";
import { Prop, Component, Emit } from "vue-property-decorator";
import { WinRspType, OperationTypeEnum, WinResponseData, DateUtils } from "win-biz";
import DeployService from "../service/DeployService";
import { StrategyDetailVO } from "../vo/StrategyVO";
import dateUtils from "win-biz/common/util/DateUtils";

@Component
export default class StrategyDialogController extends BaseController{
     // 接受父组件的值
     @Prop({
        type: Object,
        required: false,
        default: () => ({})
    })
    private toChildMsg!: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: any;
    };
    //传回父组件的值
    @Emit("bindSend")
    // tslint:disable-next-line: no-empty
    private send(msg: WinRspType) {}
    // 弹框标题
    private dialogTitle: string= "";
    // 是否隐藏弹框
    private dialogVisibleSon: boolean = false;
    private deployService: DeployService=new DeployService();

    private strategyDetailVO: StrategyDetailVO= new StrategyDetailVO();

    mounted(){
        console.log("进入弹框");
        console.log(this.toChildMsg);
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.dialogVisibleSon = true;
        if(this.toChildMsg.data){
            this.queryDetail(this.toChildMsg.data.id);
        }
    }

    queryDetail(id: number) {
        if(id){
            this.deployService.strategyDetail(id)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.strategyDetailVO = winResponseData.data;
                    this.strategyDetailVO.createTime = dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(this.strategyDetailVO.createTime));
                    this.strategyDetailVO.updateTime = dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(this.strategyDetailVO.updateTime));
                } else {
                    this.win_message_error(winResponseData.msg);
                }
            })
        }
    }
}