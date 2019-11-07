import { Component, Prop, Emit } from "vue-property-decorator";
import BaseController from "../../common/controller/BaseController";
import { WinRspType, OperationTypeEnum, WinResponseData } from "win-biz";
import { TaskReqVO, TaskTableVO, TaskDetailVO } from "../vo/TaskVO";
import { StrategyRepVO } from "../vo/StrategyVO";
import { GroupDetailVO } from "../vo/GroupVO";
import DeployService from "../service/DeployService";
import dateUtils from "win-biz/common/util/DateUtils";

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
    // 弹框标题
    private dialogTitle: string= "";
    // 是否隐藏弹框
    private dialogVisibleSon: boolean = false;
    private allDisabled:boolean = false;
    // 提交按钮加载
    private submitLoading: boolean = false;
    private submitAndDeployLoading: boolean =false;
    //任务新增修改VO
    private taskReqVO: TaskReqVO = new TaskReqVO();
    private taskDetailVO: TaskDetailVO= new TaskDetailVO();
    private logInfo: string = "";

    private deployService: DeployService = new DeployService();
    private strategyData: StrategyRepVO[] = [];
    private selectedStrategy: number[]=[];
    private groupData: GroupDetailVO[]=[];
    private selectedGroup: number[]=[];

    // 机器日志弹框
    private remoteDialogTitle:string="日志详情";
    private remoteDialogVisibleSon: boolean= false;
    private deviceLogInfo: string[]=[];

    /*定义校验函数*/
    public $refs: {
        validate: HTMLFormElement;
    };

    private formRules={
        strategyId: [
            {
                required: true,
                message: '部署策略不能为空！',
                trigger: 'change'
            }
        ],
        groupId: [
            {
                required: true,
                message: '设备组不能为空！',
                trigger: 'change'
            }
        ]
    };

    mounted(){
        console.log("进入弹框");
        console.log(this.toChildMsg);
        this.dialogTitle = this.toChildMsg.dialogTitle;
        this.dialogVisibleSon = true;
        if(this.toChildMsg.type === OperationTypeEnum.ADD){
            this.allDisabled = false;
            this.initStrategyData();
            this.initGroupData();
        }else if(this.toChildMsg.type === OperationTypeEnum.VIEW){
            this.allDisabled = true;
            this.getInfo(this.toChildMsg.data.id);
        }
    }
    getInfo(id:number) {
        console.log("***********getLogInfo");
        let me=this;
        if(id){
            this.deployService.taskInfo(id)
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData.data);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.taskDetailVO = winResponseData.data;
                    this.taskDetailVO.status= this.formmatStatus(this.taskDetailVO.status);
                    this.taskDetailVO.createTime = this.formatDate(this.taskDetailVO.createTime);
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            });
        }
    }
    formatDate(time:string){
        return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(time));
    }
    formmatStatus(status: number): any {
        if(status === 0){
            return "未部署";
        }else if(status === 1){
            return "部署中...";
        }else if(status === 2){
            return "部署成功";
        }else if(status === 3){
            return "部署失败";
        }else if(status === 4){
            return "卸载中...";
        }else if(status === 5){
            return "卸载成功";
        }else if(status === 6){
            return "卸载失败";
        }
    }
    // 初始化组下拉框数据
    initGroupData() {
        console.log("初始化可选策略");
        let me = this;
        this.deployService.strategyList()
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData.data);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    if (winResponseData.data && winResponseData.data.length>0){
                        me.strategyData = winResponseData.data;
                    }
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            });

    }
    // 初始化策略下拉框数据
    initStrategyData() {
        console.log("初始化可选设备组");
        let me = this;
        this.deployService.groupList()
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData.data);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    if (winResponseData.data && winResponseData.data.length>0){
                        me.groupData = winResponseData.data;
                    }
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            });
    }

    /*关闭组对话框*/
    closeDialog(){
        this.send(WinRspType.CANCEL);
    }

    submitDialog(formName: string){
        console.log("********submitDialog**********");
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                this.deployService.insertTask(this.taskReqVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            console.log(winResponseData.data);
                            this.dialogVisibleSon = false;
                            //传参到GroupDialog组件更新穿梭框设备可选列表
                            this.send(WinRspType.SUCC);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_box_error(winResponseData.msg);
                        }
                    })
            } else {
                this.win_message_box_error("表单验证未通过");
                return false;
            }
        });
    }
    // 一键部署
    submitAndDeployDialog(formName:string){
        this.$refs[formName].validate((valid: boolean) => {
            if (valid) {
                this.deployService.oneKeyDeploy(this.taskReqVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            console.log(winResponseData.data);
                            this.dialogVisibleSon = false;
                            //传参到GroupDialog组件更新穿梭框设备可选列表
                            this.send(WinRspType.SUCC);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_box_error(winResponseData.msg);
                        }
                    })
            } else {
                this.win_message_box_error("表单验证未通过");
                return false;
            }
        });
    }
    viewLog(ipAddress:string){
        if(ipAddress && this.taskDetailVO.strategyName){
            this.remoteDialogTitle +="["+ipAddress+"]";
            this.remoteDialogVisibleSon = true;
            // this.deviceLogInfo=['asdfasfsa','asdfasf ','sfasfa'];
            let params={
                "ipAddress":ipAddress,
                "strategyName":this.taskDetailVO.strategyName
            }
            this.deployService.viewDeviceLog(params)
            .then((winResponseData: WinResponseData) =>{
                console.log(winResponseData.data);
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.deviceLogInfo=winResponseData.data;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            });
        }
    }
}