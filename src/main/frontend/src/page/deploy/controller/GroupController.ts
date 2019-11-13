import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {GroupQueryVO, GroupTreeVO, DeviceReqVO} from "../vo/GroupVO";
import {GroupConst, DeviceStatusConst, BaseTypeConst, DialogTitleConst} from "../const/DeployConst";
import GroupDialog from "../view/groupDialog.vue";
import {BaseConst} from "../../common/const/BaseConst";
import dateUtils from "../../common/util/DateUtils";
import { DeviceStatus } from "../const/DeployEnum";


@Component({ components: {GroupDialog} })
export default class GroupController extends BaseController {
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    /** 分页对象 */
    public pageVO: PageVO = new PageVO();
    /** 查询对象*/
    private groupQueryVO: GroupQueryVO = new GroupQueryVO();
    /** 树形组列表数据*/
    private groupTreeList: GroupTreeVO[]=[];
    /** 是否显示设备组弹框*/
    private isGroupDialog: boolean = false;
    /*组表格加载中*/
    private groupLoading: boolean = false;
    /*选中项*/
    private selected: any = [];
    /** 设备组弹框信息*/
    private groupDialogMsg: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: GroupTreeVO;
    };

    private deviceReqVO: DeviceReqVO = new DeviceReqVO();
    /** 初始化*/
    private mounted() {
        this.queryGroupTreeList(this.groupQueryVO);
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
    /** 查询设备组树形列表*/
    public queryGroupTreeList(groupQueryVO: GroupQueryVO) {
        this.groupLoading = true;
        this.deployService.groupTreeList(groupQueryVO)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.groupTreeList = winResponseData.data.list;
                    this.pageVO = winResponseData.data;
                } else {
                    this.win_message_box_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.groupLoading = false;
            });
    }

    /**
     * 分页按钮查询
     *
     * @param vo
     */
    public groupPageQuery(pageVO: PageVO) {
        console.log("groupPageQuery");
        console.log(pageVO);
        this.groupQueryVO.reqPageNum = pageVO.pageNum;
        this.groupQueryVO.reqPageSize = pageVO.pageSize;
        this.queryGroupTreeList(this.groupQueryVO);
    }

    /** 新增/修改设备组*/
    public groupOperation(row: GroupTreeVO, type: OperationTypeEnum){
        console.log("groupOperation"+OperationTypeEnum);
        console.log(row);
        if (type === OperationTypeEnum.ADD) {
            this.groupDialogMsg = {
                dialogTitle: DialogTitleConst.GROUP_ADD,
                type: OperationTypeEnum.ADD,
                data: null
            };
        }if (type === OperationTypeEnum.UPDATE) {
            this.groupDialogMsg = {
                dialogTitle: DialogTitleConst.GROUP_EDIT+"-"+row.name,
                type: OperationTypeEnum.UPDATE,
                data: this.copy(row)
            };
        }
        if (type === OperationTypeEnum.VIEW) {
            this.groupDialogMsg = {
                dialogTitle: BaseConst.VIEW +"-" + row.name,
                type: OperationTypeEnum.VIEW,
                data: row
            };
        }
        this.isGroupDialog = true;
    }
    public delGroupBatch(){
        console.log("***********delGroupBatch************");
        console.log(this.selected);
        let delGroupIds=[];
        let groupNames = "";
        this.selected.forEach((row: any) => {
            console.log(row);
            if(!row.ipAddress){
                delGroupIds.push(row.id);
                groupNames+=row.name+";";
            }
        });
       
        if(delGroupIds.length>0){
            const h = this.$createElement;
            let msg =  h('p', null, [h('span', null, '确认删除选中设备：'),h('i', { style: 'color: red' }, groupNames)]);
            this.win_message_box_warning(msg,"提示")
                .then(() => {
                    this.deployService.removeGroupBatch(delGroupIds)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.queryGroupTreeList(this.groupQueryVO);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
                            }
                        });
                        
                });
        }

    }
    // 子组件回调函数
    private toGroupDialogForm(msg: WinRspType) {
        this.isGroupDialog = false;
        if (msg === WinRspType.SUCC) {
            this.groupQueryVO.reqPageNum = 1;
            this.queryGroupTreeList(this.groupQueryVO);
        }
    }

    private delGroupOne(row: GroupTreeVO) {
        this.win_message_box_warning("确认删除选中组-"+row.name,"提示")
            .then(() => {
                this.deployService.removeGroupById(row.id)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.queryGroupTreeList(this.groupQueryVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_box_error(winResponseData.msg);
                        }
                    });
            });

    }

    private delDeviceOne(id: number) {
        this.win_message_box_warning("确认删除选中设备","提示")
            .then(() => {
                this.deployService.removeDeviceById(id)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.queryGroupTreeList(this.groupQueryVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_box_error(winResponseData.msg);
                        }
                    });
            });
    }

    // 双击查看
    private view({ cellValue, row, rowIndex, column, columnIndex }) {
        this.groupOperation(row, OperationTypeEnum.VIEW);
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
    // 状态格式化
    private formatDeviceStatus(status:number){
        if(DeviceStatus.not == status){
            return DeviceStatusConst.NOT;
        }else if(DeviceStatus.failure == status){
            return DeviceStatusConst.FAILURE;
        }else if(DeviceStatus.normal == status){
            return DeviceStatusConst.NORMAL;
        }else{
            return status;
        }
    }
    // 设备状态显示类型
    private deviceStatusType(status:number){
        if(DeviceStatus.not == status){
            return BaseTypeConst.info;
        }else if(DeviceStatus.failure == status){
            return BaseTypeConst.warning;
        }else if(DeviceStatus.normal == status){
            return BaseTypeConst.success;
        }else{
            return BaseTypeConst.primary;
        }
    }
      // 表格字段格式化
      private formatGroupTable({ cellValue, row, rowIndex, column, columnIndex }) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
    }
    // 连接测试并保存状态
    private testConnect(row: GroupTreeVO){
        this.win_message_box_warning("是否测试设备连通性？","提示")
            .then(() => {
                if(row.ipAddress){
                    this.deployService.testConnectByIp(row.ipAddress)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.win_message_success(winResponseData.msg);
                                this.queryGroupTreeList(this.groupQueryVO);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
                            }
                        });
                }
            });
        
    }
}

