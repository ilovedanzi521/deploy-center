
import BaseController from "../../common/controller/BaseController";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {DeviceRepVO, DeviceReqVO, GroupReqVO, GroupTreeVO} from "../vo/GroupVO";
import {Component, Emit, Prop} from "vue-property-decorator";
import {WinRspType} from "../../common/enum/BaseEnum";
import {BaseConst} from "../../common/const/BaseConst";
import {DeviceValidCanst, GroupValidConst} from "../const/ValidateConst";
import DeployService from "../service/DeployService";
import {WinResponseData} from "../../common/vo/BaseVO";
import {GroupConst} from "../const/DeployConst";

@Component
export default class GroupDialogController extends BaseController{
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    // 接受父组件的值
    @Prop({
        type: Object,
        required: false,
        default: () => ({})
    })
    private toChildMsg!: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: GroupTreeVO;
    };
    /*定义校验函数*/
    public $refs: {
        validate: HTMLFormElement;
    };
    // 弹窗标题
    private dialogTitle: string = "";
    // 控制dialog显隐
    private dialogVisibleSon: boolean = false;
    // 确定按钮或删除按钮标志
    private dialogSumbitText: string = "";
    /*保存加载中*/
    private saveLoading: boolean = false;
    private deviceIds: number[]=[];
    private devices: DeviceRepVO[]=[];
    private deviceTransferData: any =[];
    /**
     * 按钮是否显示
     */
    private buttonShow: boolean = true;
    /**
     * 所有字段是否置灰
     */
    private allDisabled: boolean = false;

    private groupReqVO: GroupReqVO = new GroupReqVO();

    /*是否展示设备对话框*/
    private deviceDialogVisible: boolean = false;
    /*设备对话框标题*/
    private deviceDialogTitle: string = "";
    /*设备对话框-提交按钮*/
    private deviceSubmitText :string = "";
    private deviceStatusText : string = "未连接";

    private deviceRepVO: DeviceRepVO=new DeviceRepVO();
    private deviceReqVO: DeviceReqVO = new DeviceReqVO();

    // 校验规则
    private rules = {
        name: [
            {
                required: true,
                message: GroupValidConst.NAME_NOTNULL,
                trigger: "blur"
            }
        ],
        deviceIds: [
            {
                required: true,
                message: GroupValidConst.DEVICE_NOTNULL,
                trigger: "change"
            }
        ]
    };
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
        console.log(this.toChildMsg);
        this.initDeviceTransferData();
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.transfChildMsg(this.toChildMsg.data);
        if (this.toChildMsg.type === OperationTypeEnum.ADD) {
            this.dialogSumbitText = BaseConst.CONFIRM;
        }
        this.dialogVisibleSon = true;
    }
    // 提交
    private submitDialog(formName: string) {
        console.log("submitDialog...........");
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                console.log(this.groupReqVO);
                this.deployService.saveGroup(this.groupReqVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            console.log(winResponseData.data);
                            this.dialogVisibleSon = false;
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.errorMessage(winResponseData.msg);
                        }
                });
            } else {
                this.win_message_error("组表单验证未通过");
                return false;
            }
        });
    }
    /*关闭组对话框*/
    private closeGroupDialog(){
        this.send(WinRspType.CANCEL);
    }

    @Emit("bindSend")
    // tslint:disable-next-line: no-empty
    private send(msg: WinRspType) {}

    /*初始化设备穿梭框已选数据*/
    private transfChildMsg(treeVO: GroupTreeVO) {
        let me = this;
        this.groupReqVO.id = treeVO.id;
        this.groupReqVO.name = treeVO.name;
        this.groupReqVO.desc = treeVO.desc;
        treeVO.children.forEach(function (item) {
            me.groupReqVO.deviceIds.push(item.id);
        });
    }

    /*初始化设备穿梭框可选数据*/
    private initDeviceTransferData() {
        let me = this;
        me.deviceTransferData = [];
        this.deployService.deviceList()
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    console.log(winResponseData.data);
                    this.devices = winResponseData.data;
                    if (this.devices && this.devices.length>0){
                        this.devices.forEach(function (item) {
                            me.deviceTransferData.push({
                                key: item.id,
                                label: `${item.ipAddress}(${item.name})`,
                            });
                        })
                    }
                } else {
                    this.errorMessage(winResponseData.msg);
                }
            })
    }

    public deviceOperation(row: DeviceRepVO, type: OperationTypeEnum){
        console.log("deviceOperation");
        console.log(OperationTypeEnum);
        console.log(row);
        if (type === OperationTypeEnum.ADD) {
            this.deviceDialogTitle = GroupConst.ADD_DEVICE;
            this.deviceSubmitText = GroupConst.SAVE;
        }
        this.deviceDialogVisible = true;
    }
    public submitDeviceDialog(formName: string){
        console.log("********submitDeviceDialog**********");
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                this.deployService.insertDevice(this.deviceRepVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            console.log(winResponseData.data);
                            this.initDeviceTransferData();
                            this.deviceDialogVisible = false;
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.errorMessage(winResponseData.msg);
                        }
                    })
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
                        } else {
                            this.errorMessage(winResponseData.msg);
                        }
                })
            } else {
                this.win_message_error("设备表单验证未通过");
                return false;
            }
        });
    }
}
