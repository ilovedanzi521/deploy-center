import { Component, Prop, Emit } from "vue-property-decorator";
import BaseController from "../../common/controller/BaseController";
import { WinRspType, OperationTypeEnum, WinResponseData } from "win-biz";
import { TaskReqVO, TaskTableVO } from "../vo/TaskVO";
import { StrategyRepVO } from "../vo/StrategyVO";
import { GroupDetailVO } from "../vo/GroupVO";
import DeployService from "../service/DeployService";

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
    // 提交按钮加载
    private submitLoading: boolean = false;
    //任务新增修改VO
    private taskReqVO: TaskReqVO = new TaskReqVO();

    private deployService: DeployService = new DeployService();
    private strategyData: StrategyRepVO[] = [];
    private selectedStrategy: number[]=[];
    private groupData: GroupDetailVO[]=[];
    private selectedGroup: number[]=[];

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
        if(this.toChildMsg.type === OperationTypeEnum.ADD){
            this.dialogVisibleSon = true;
        }
        if(this.dialogVisibleSon){
            this.initStrategyData();
            this.initGroupData();
        }
    }
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
                    this.win_message_error(winResponseData.msg);
                }
            });

    }
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
                    this.win_message_error(winResponseData.msg);
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
                console.log(this.taskReqVO);
                this.deployService.insertTask(this.taskReqVO)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            console.log(winResponseData.data);
                            this.dialogVisibleSon = false;
                            //传参到GroupDialog组件更新穿梭框设备可选列表
                            this.send(WinRspType.SUCC);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_error(winResponseData.msg);
                        }
                    })
            } else {
                this.win_message_error("表单验证未通过");
                return false;
            }
        });
    }
}