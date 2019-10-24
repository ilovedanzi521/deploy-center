import { Component, Prop, Emit } from "vue-property-decorator";
import BaseController from "../../common/controller/BaseController";
import { WinRspType, OperationTypeEnum } from "win-biz";
import TaskTableVO from "../vo/TaskVO";

@Component
export default class TaskDialogController extends BaseController{
     // 接受父组件的值
     @Prop({
        type: Object,
        required: false,
        default: () => ({})
    })
    private toChildMsg!: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: TaskTableVO;
    };
    //传回父组件的值
    @Emit("bindSend")
    // tslint:disable-next-line: no-empty
    private send(msg: WinRspType) {}

    private dialogTitle: string= "";
    private dialogVisibleSon: boolean = false;

    private mounted(){
        console.log("进入弹框");
        console.log(this.toChildMsg);
        this.dialogTitle = this.toChildMsg.dialogTitle;
        if(this.toChildMsg.type === OperationTypeEnum.ADD){
            this.dialogVisibleSon = true;
        }
    }

    /*关闭组对话框*/
    private closeDialog(){
        this.send(WinRspType.CANCEL);
    }
}