
import {Component, Emit, Prop} from "vue-property-decorator";
import BaseController from "../../common/controller/BaseController";
import { UploadVO } from "../vo/AppModuleVO";
import { WinRspType } from "../../common/enum/BaseEnum";
import { DeployTypeEnum } from "../const/DeployEnum";
import { MimeTypeConst } from "../const/DeployConst";
import DeployService from "../service/DeployService";
import { WinResponseData } from "win-biz";

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
    // 允许上传文件类型
    private acceptFileType: string = MimeTypeConst.ZIP;
    private uploadUrl: string = DeployService.uploadUrl;
    
    /*关闭组对话框*/
    private closeDialog(){
        this.send(WinRspType.CANCEL);
    }

    // 页面初始化
    private mounted(){
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.dialogVisibleSon = true;
    }
    // 上传前处理
    private beforeUpload(file){
        const isXZIP = file.type === MimeTypeConst.XZIP;
        const isLt1G = file.size / 1024 / 1024 / 1024 < 1;
        if (!isXZIP) {
            this.win_message_box_error('上传文件只能是 zip 格式!');
          }
          if (!isLt1G) {
            this.win_message_box_error('上传文件大小不能超过 1GB!');
          }
        return isXZIP && isLt1G;
    }
    // 提交上传按钮
    private submitUpload(event){
        this.$refs.upload.submit();
    }
    // 上传成功提示
    private successUpload(winResponseData:WinResponseData, file){
        if (WinRspType.SUCC === winResponseData.winRspType) {
            this.win_message_success(winResponseData.msg);
            this.send(WinRspType.SUCC);
        } else {
            this.win_message_error(winResponseData.msg);
        }
    }
    // 上传失败提示
    private errorUpload(err, file){
        console.log("errorUpload");
        console.log(err);
        this.win_message_error(err.message);
    }
}