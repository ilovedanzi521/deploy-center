
import {Component, Emit, Prop} from "vue-property-decorator";
import BaseController from "../../common/controller/BaseController";
import { OperationTypeEnum } from "../../common/enum/OperationTypeEnum";
import { UploadVO } from "../vo/AppModuleVO";
import { WinRspType } from "../../common/enum/BaseEnum";
import { DeployTypeEnum } from "../const/DeployEnum";

@Component
export default class UploadDialogController extends BaseController{
     // 接受父组件的值
     @Prop({
        type: Object,
        required: false,
        default: () => ({})
    })
    private toChildMsg!: {
        dialogTitle: string;
        type: DeployTypeEnum;
        data: UploadVO;
    };
    //传回父组件的值
    @Emit("bindSend")
    // tslint:disable-next-line: no-empty
    private send(msg: WinRspType) {}
    // 弹窗标题
    private dialogTitle: string = "";
    // 控制dialog显隐
    private dialogVisibleSon: boolean = false;

    
    /*关闭组对话框*/
    private closeDialog(){
        this.send(WinRspType.CANCEL);
    }

    private mounted(){
        console.log("进入上传弹框...........");
        console.log(this.toChildMsg);
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.dialogVisibleSon = true;
    }

}