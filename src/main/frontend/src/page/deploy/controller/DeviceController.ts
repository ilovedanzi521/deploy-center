import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {BaseConst} from "../../common/const/BaseConst";
import dateUtils from "../../common/util/DateUtils";
import { DeviceStatus } from "../const/DeployEnum";
import DeviceDialog from "../view/deviceDialog.vue";
import {DeviceRepVO, DeviceReqVO} from "../vo/GroupVO";
import {DeviceDetailVO, DeviceQueryVO} from "../vo/DeviceVO";
import {DeviceStatusConst, BaseTypeConst, DialogTitleConst} from "../const/DeployConst";

@Component({ components: {DeviceDialog} })
export default class DeviceController extends BaseController {

    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    /** 是否显示设备组弹框*/
    private isDeviceDialog: boolean = false;
    /*组表格加载中*/
    private deviceLoading: boolean = false;
    /*选中项*/
    private selected: any = [];
    /** 查询对象*/
    private deviceQueryVO: DeviceQueryVO = new DeviceQueryVO();
    /** 设备列表*/
    private deviceList: DeviceRepVO[]=[];
    /** 设备弹框信息*/
    private deviceDialogMsg: {
        dialogTitle: string;
        type: OperationTypeEnum;
        data: DeviceQueryVO;
    };

    private deviceTransferData: any =[];
    private devices: DeviceRepVO[]=[];

    /** 初始化*/
    private mounted() {
        this.devicePageQuery(this.deviceQueryVO);
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

    // 表格字段格式化
    private formatDeviceTable({ cellValue, row, rowIndex, column, columnIndex }) {
        if (column.property === "createTime") {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
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

    /**
     * 分页按钮查询
     *
     * @param vo
     */
    public devicePageQuery(deviceQueryVO: DeviceQueryVO) {
        this.deviceLoading = true;
        this.deployService.devicePageList(deviceQueryVO)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.deviceList = winResponseData.data.list;
                    this.pageVO = winResponseData.data;
                } else {
                    this.win_message_error(winResponseData.msg);
                }
            }).finally(() =>{
                this.deviceLoading = false;
            });
    }

    /** 新增/修改设备*/
    public deviceOperation(row: DeviceQueryVO, type: OperationTypeEnum){
        console.log("deviceOperation"+OperationTypeEnum);
        console.log(row);
        if (type === OperationTypeEnum.ADD) {
            this.deviceDialogMsg = {
                dialogTitle: DialogTitleConst.DEVICE_ADD,
                type: OperationTypeEnum.ADD,
                data: null
            };
        }
        if (type === OperationTypeEnum.UPDATE) {
            this.deviceDialogMsg = {
                dialogTitle: DialogTitleConst.DEVICE_EDIT+"-"+row.name,
                type: OperationTypeEnum.UPDATE,
                data: this.copy(row)
            };
        }
        if (type === OperationTypeEnum.VIEW) {
            this.deviceDialogMsg = {
                dialogTitle: BaseConst.VIEW +"-" + row.name,
                type: OperationTypeEnum.VIEW,
                data: row
            };
        }
        this.isDeviceDialog = true;
    }

    // 双击查看
    private view({ cellValue, row, rowIndex, column, columnIndex }) {
        this.deviceOperation(row, OperationTypeEnum.VIEW);
    }


    private delDeviceOne(row: DeviceQueryVO) {
        this.win_message_box_warning("确认删除选中设备-"+row.name)
            .then(() => {
                this.deployService.removeDeviceById(row.id)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.devicePageQuery(this.deviceQueryVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_error(winResponseData.msg);
                        }
                    });
            });

    }

    public delDeviceBatch(){
        console.log("***********delDeviceBatch************");
        console.log(this.selected);
        let delDeviceIds=[];
        let deviceNames = "";
        this.selected.forEach((row: any) => {
            console.log(row);
            delDeviceIds.push(row.id);
            deviceNames+=row.name+";";
        });
       
        if(delDeviceIds.length>0){
            const h = this.$createElement;
            let msg =  h('p', null, [h('span', null, '确认删除设备：'),h('i', { style: 'color: red' }, deviceNames)]);
            this.win_message_box_warning(msg,"提示")
                .then(() => {
                    this.deployService.removeDeviceBatch(delDeviceIds)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.devicePageQuery(this.deviceQueryVO);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_error(winResponseData.msg);
                            }
                        });
                        
                });
        }

    }

    // 子组件回调函数
    private toDeviceDialogForm(msg: WinRspType) {
        this.isDeviceDialog = false;
        if (msg === WinRspType.SUCC) {
            this.initDeviceTransferData(this.deviceQueryVO);
        }else if(msg === WinRspType.WARN){
            this.initDeviceTransferData(this.deviceQueryVO);
            this.isDeviceDialog = true;
        }
    }

    /*初始化设备穿梭框可选数据*/
    async initDeviceTransferData(deviceQueryVO: DeviceQueryVO) {
        await this.deployService.devicePageList(deviceQueryVO)
        .then((winResponseData: WinResponseData) =>{
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.deviceList = winResponseData.data.list;
                this.pageVO = winResponseData.data;
            } else {
                this.win_message_error(winResponseData.msg);
            }
        })
    }
}