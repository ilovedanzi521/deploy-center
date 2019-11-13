import BaseController from "../../common/controller/BaseController";
import {Component, Emit, Prop} from "vue-property-decorator";
import DeployService from "../service/DeployService";
import {DeviceRepVO, DeviceReqVO} from "../vo/GroupVO";
import {DeviceValidCanst} from "../const/ValidateConst";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {WinResponseData} from "../../common/vo/BaseVO";
import {WinRspType} from "../../common/enum/BaseEnum";
import {BaseConst} from "../../common/const/BaseConst";
import {DeviceStatusConst} from "../const/DeployConst";
import { DeviceStatus } from "../const/DeployEnum";

@Component
export default class DeviceDialogController extends BaseController{
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    // 接受父组件的值
    @Prop({
        type: Object,
        required: false,
        default: () => ({})
    })
    private toDeviceChildMsg!: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: DeviceRepVO;
    };
    /*定义校验函数*/
    public $refs: {
        validate: HTMLFormElement;
    };
    /*关闭组对话框*/
    private closeDeviceDialog(){
        this.send(WinRspType.CANCEL);
    }

    @Emit("bindDeviceSend")
    // tslint:disable-next-line: no-empty
    private send(msg: WinRspType) {}
    /*是否展示设备对话框*/
    private deviceDialogVisible: boolean = false;
    /*设备对话框标题*/
    private deviceDialogTitle: string = "";
    /*设备对话框-提交按钮*/
    private deviceSubmitText :string = "";
    private deviceStatusText : string = "未连接";
    private showSubmitBtn :boolean = true;

    private deviceRepVO: DeviceRepVO=new DeviceRepVO();
    private deviceReqVO: DeviceReqVO = new DeviceReqVO();

    // 校验规则
    private deviceRules = {
        ipAddress: [
            {
                required: true,
                message: DeviceValidCanst.ipAddress_notnull,
                trigger: "blur"
            }
        ],
        osType: [
            {
                required: true,
                message: DeviceValidCanst.osType_notnull,
                trigger: "blur"
            }
        ],
        userName: [
            {
                required: true,
                message: DeviceValidCanst.userName_notnull,
                trigger: "blur"
            }
        ],
        port: [
            {
                required: true,
                message: DeviceValidCanst.port_notnull,
                trigger: "blur"
            }
        ],
    };

    private mounted(){
        console.log("进入组对话框...........");
        console.log(this.toDeviceChildMsg);
        this.deviceDialogTitle = this.toDeviceChildMsg.dialogTitle;
        this.transfChildMsg(this.toDeviceChildMsg.data);

        if (this.toDeviceChildMsg.type === OperationTypeEnum.ADD) {
            this.deviceSubmitText = BaseConst.CONFIRM;
            this.showSubmitBtn = true;
        } else if (this.toDeviceChildMsg.type === OperationTypeEnum.UPDATE) {
            this.deviceSubmitText = BaseConst.CONFIRM;
            this.showSubmitBtn = true;
        } else if (this.toDeviceChildMsg.type === OperationTypeEnum.VIEW) {
            this.showSubmitBtn = false;
        } 
        this.deviceDialogVisible = true;
    }

    /*初始化设备对话框数据*/
    private transfChildMsg(device: DeviceRepVO) {
        console.log("*************transfChildMsg***********");
        console.log(device);
        let me = this;
        if(device){
            me.deviceRepVO.id = device.id;
            me.deviceRepVO.name = device.name;
            me.deviceRepVO.alias = device.alias;
            me.deviceRepVO.ipAddress = device.ipAddress;
            me.deviceRepVO.osType = device.osType;
            me.deviceRepVO.port=device.port;
            me.deviceRepVO.desc = device.desc;
            me.deviceRepVO.userName = device.userName;
            me.deviceRepVO.status = device.status;
            if(DeviceStatus.not == device.status){
                this.deviceStatusText = DeviceStatusConst.NOT;
            }else if(DeviceStatus.failure == device.status){
                this.deviceStatusText = DeviceStatusConst.FAILURE;
            }else if(DeviceStatus.normal == device.status){
                this.deviceStatusText = DeviceStatusConst.NORMAL;
            }else{
                this.deviceStatusText = "未连接";
            }
        }else{
            me.deviceRepVO = new DeviceRepVO();
        }
    }

    public submitDeviceDialog(formName: string){
        console.log("********submitDeviceDialog**********");
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                if (this.toDeviceChildMsg.type === OperationTypeEnum.ADD) {
                    this.deployService.insertDevice(this.deviceRepVO)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                console.log(winResponseData.data);
                                this.deviceDialogVisible = false;
                                //传参到GroupDialog组件更新穿梭框设备可选列表
                                this.send(WinRspType.SUCC);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_error(winResponseData.msg);
                            }
                        })
                } else if (this.toDeviceChildMsg.type === OperationTypeEnum.UPDATE) {
                    this.deployService.updateDevice(this.deviceRepVO)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                console.log(winResponseData.data);
                                this.deviceDialogVisible = false;
                                //传参到GroupDialog组件更新穿梭框设备可选列表
                                this.send(WinRspType.SUCC);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_error(winResponseData.msg);
                            }
                        })
                }
            } else {
                this.win_message_error("设备表单验证未通过");
                return false;
            }
        });
    }

    public connectTest(formName: string){
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                this.deployService.deviceConnectTest(this.deviceRepVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.deviceRepVO = winResponseData.data;
                            if (this.deviceRepVO.status == -1){
                                this.deviceStatusText= "连接失败";
                            }else if (this.deviceRepVO.status == 1){
                                this.deviceStatusText= "连接正常";
                            }else {
                                this.deviceStatusText= "未连接";
                            }
                            this.showSubmitBtn = false;
                            this.send(WinRspType.WARN);
                        } else {
                            this.win_message_error(winResponseData.msg);
                        }
                    })
            } else {
                this.win_message_error("设备表单验证未通过");
                return false;
            }
        });
    }
}
