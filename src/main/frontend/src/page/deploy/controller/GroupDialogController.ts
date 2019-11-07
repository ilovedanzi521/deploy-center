
import BaseController from "../../common/controller/BaseController";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {DeviceRepVO, DeviceReqVO, GroupReqVO, GroupTreeVO} from "../vo/GroupVO";
import {Component, Emit, Prop} from "vue-property-decorator";
import {WinRspType} from "../../common/enum/BaseEnum";
import {BaseConst} from "../../common/const/BaseConst";
import {DeviceValidCanst, GroupValidConst} from "../const/ValidateConst";
import DeployService from "../service/DeployService";
import {WinResponseData} from "../../common/vo/BaseVO";
import {GroupConst, DialogTitleConst} from "../const/DeployConst";
import DeviceDialog from "../view/deviceDialog.vue";

@Component({ components: {DeviceDialog} })
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

    // 穿梭框左边选中值
    private leftCheckedKeys: number[]=[];
    /**
     * 按钮是否显示
     */
    private buttonShow: boolean = true;
    /**
     * 所有字段是否置灰
     */
    private allDisabled: boolean = false;

    private groupReqVO: GroupReqVO = new GroupReqVO();

    /*###################################33*/
    private isDeviceDialog: boolean=false;
    private deviceDialogMsg: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: DeviceRepVO;
    };
    // 子组件回调函数
    private toDeviceDialogForm(msg: WinRspType) {
        this.isDeviceDialog = false;
        if (msg === WinRspType.SUCC) {
            this.initDeviceTransferData();
        }else if(msg === WinRspType.WARN){
            this.initDeviceTransferData();
            this.isDeviceDialog = true;
        }
    }
    /*###################################33*/

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

    private mounted(){
        console.log("进入组对话框...........");
        console.log(this.toChildMsg);
        this.initDeviceTransferData();
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.transfChildMsg(this.toChildMsg.data);
        if (this.toChildMsg.type === OperationTypeEnum.ADD) {
            this.dialogSumbitText = BaseConst.CONFIRM;
        }else if(this.toChildMsg.type === OperationTypeEnum.UPDATE){
            this.dialogSumbitText = BaseConst.CONFIRM;
        }
        this.dialogVisibleSon = true;
    }
    // 提交
    private submitDialog(formName: string) {
        this.saveLoading = true;
        console.log("submitDialog...........");
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                console.log(this.groupReqVO);
                if(this.groupReqVO.id){
                    this.deployService.safeUpdateGroup(this.groupReqVO)
                        .then((winResponseData: WinResponseData) =>{
                            this.groupDialogMessage(winResponseData);
                        }).finally(()=>{
                            this.saveLoading = false;
                        })
                        
                }else{
                    this.deployService.saveGroup(this.groupReqVO)
                        .then((winResponseData: WinResponseData) =>{
                            this.groupDialogMessage(winResponseData);
                        }).finally(()=>{
                            this.saveLoading = false;
                        })
                }
            } else {
                this.saveLoading = false;
                this.win_message_box_error("组表单验证未通过");
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
        console.log("*************transfChildMsg***********");
        console.log(treeVO);
        let me = this;
        if(treeVO){
            me.groupReqVO.id = treeVO.id;
            me.groupReqVO.name = treeVO.name;
            me.groupReqVO.desc = treeVO.desc;
            me.groupReqVO.deviceIds=[];
            treeVO.children.forEach(function (item) {
                me.groupReqVO.deviceIds.push(item.id);
            });
        }else{
            me.groupReqVO = new GroupReqVO();
        }
    }

    /*初始化设备穿梭框可选数据*/
    async initDeviceTransferData() {
        let me = this;
        me.deviceTransferData = [];
        await this.deployService.deviceList()
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
                    this.win_message_box_error(winResponseData.msg);
                }
            })
    }

    // 消息
    private groupDialogMessage(winResponseData: WinResponseData) {
        if (WinRspType.SUCC === winResponseData.winRspType) {
            this.win_message_success(winResponseData.msg);
            this.dialogVisibleSon = false;
            this.send(WinRspType.SUCC);
        } else {
            this.win_message_box_error(winResponseData.msg);
        }
    }

    /** 新增/修改设备组*/
    public deviceOperation(row: DeviceRepVO, type: OperationTypeEnum){
        console.log("*******************************deviceOperation");
        console.log(row);
        if (type === OperationTypeEnum.ADD) {
            this.deviceDialogMsg = {
                dialogTitle: DialogTitleConst.GROUP_ADD,
                type: OperationTypeEnum.ADD,
                data: new DeviceRepVO()
            };
        }
        this.isDeviceDialog = true;
    }
    // 批量删除设备
    deleteDevices(){
        if(this.leftCheckedKeys.length>0){
            this.deployService.removeDeviceBatch(this.leftCheckedKeys)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    console.log(winResponseData.data);
                   this.win_message_success(winResponseData.msg);
                   this.initDeviceTransferData();
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
        }else{
            this.win_message_box_warning("请选择设备！");
        }
    }

    // 处理左边选中事件
    leftCheckChangeHandle(checked, changed){
        this.leftCheckedKeys = checked;
    }
}
