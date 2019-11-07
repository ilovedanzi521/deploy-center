import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {GroupTreeVO, DeviceReqVO} from "../vo/GroupVO";
import {GroupConst, DeviceStatusConst, BaseTypeConst, DialogTitleConst} from "../const/DeployConst";
import {BaseConst} from "../../common/const/BaseConst";
import dateUtils from "../../common/util/DateUtils";
import { DeviceStatus, DeployTypeEnum } from "../const/DeployEnum";
import { QueryReqVO, AppModuleTreeVO, UploadVO, DeviceModuleRefVO } from "../vo/AppModuleVO";
import UploadDialog from "../view/uploadDialog.vue";
import { async } from "q";


@Component({ components: {UploadDialog} })
export default class AppModuleController extends BaseController {
    /** 部署服务*/
    private deployService: DeployService = new DeployService();
    /** 分页对象 */
    public pageVO: PageVO = new PageVO();
    /** 查询对象*/
    private queryReqVO: QueryReqVO = new QueryReqVO();
    /** 树形组列表数据*/
    private pageDataList: AppModuleTreeVO[]=[];
    /** 是否显示设备组弹框*/
    private isUploadDialog: boolean = false;
    /*组表格加载中*/
    private tableLoading: boolean = false;
    private deviceLoading :boolean=false;
    // 机器服务列表加载条
    private devicesTableLoading:boolean=false;
    // 启停按钮加载进度
    private startBtnLoading:boolean=false;
    private stopBtnLoading:boolean=false;
    /*选中项*/
    private selected: any = [];
    /** 上传弹框信息*/
    private toUploadDialogMsg: {
        dialogTitle: string;
        type: DeployTypeEnum;
        data: UploadVO;
    };

    private deviceReqVO: DeviceReqVO = new DeviceReqVO();

    /** 初始化*/
    private mounted() {
        this.queryPageList(this.queryReqVO);
    }
    
    /** 查询设备组树形列表*/
    public queryPageList(queryReqVO: QueryReqVO) {
        console.log("queryPageList");
        this.tableLoading = true;
        this.deployService.appModulePageList(queryReqVO)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.pageDataList = winResponseData.data.list;
                    console.log(this.pageDataList);
                    this.pageVO = winResponseData.data;
                } else {
                    this.win_message_error(winResponseData.msg);
                }
            })
            .finally(() =>{
                this.tableLoading = false;
            });
    }

    /**
     * 分页按钮查询
     *
     * @param vo
     */
    public groupPageQuery(pageVO: PageVO) {
        this.queryReqVO.reqPageNum = pageVO.pageNum;
        this.queryReqVO.reqPageSize = pageVO.pageSize;
        this.queryPageList(this.queryReqVO);
    }

    /** 新增/修改设备组*/
    public uploadAppModule(){
        this.toUploadDialogMsg={
            dialogTitle: DialogTitleConst.APP_UPLOAD,
            type : DeployTypeEnum.UPLOAD,
            data : null
        }
        this.isUploadDialog = true;
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
            let msg =  h('p', null, [h('span', null, '确认删除设置组：'),h('i', { style: 'color: red' }, groupNames)]);
            this.win_message_box_success(msg,"提示")
                .then(() => {
                    this.deployService.removeGroupBatch(delGroupIds)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.queryPageList(this.queryReqVO);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_error(winResponseData.msg);
                            }
                        });
                        
                });
        }

    }
    // 子组件回调函数:如果上传成功关闭弹框并刷新表格
    private bindUploadSend(msg: WinRspType) {
        this.isUploadDialog = false;
        if (msg === WinRspType.SUCC) {
            this.queryPageList(this.queryReqVO);
        }
    }

    private delGroupOne(row: GroupTreeVO) {
        this.win_message_box_warning("确认删除选中组-"+row.name,"提示")
            .then(() => {
                this.deployService.removeGroupById(row.id)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.queryPageList(this.queryReqVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_error(winResponseData.msg);
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
                            this.queryPageList(this.queryReqVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_error(winResponseData.msg);
                        }
                    });
            });
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
    formatDeviceStatus({row, column, cellValue, index}){
        if(column.property === "status"){
            if(cellValue === 0){
                return "服务未运行";
            }else if(cellValue < 0){
                return "服务运行异常";
            }else if(cellValue>0){
                return "服务正运行";
            }else{
                return "未知码:"+cellValue;
            }
        }
    }
      // 表格字段格式化
      private formatGroupTable({row, column, cellValue, index}) {
        // console.log("formatGroupTable************8");
        if (column.property === "createTime"  && cellValue) {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
        if(column.property === "status"){
            return "未部署";
        }
    }
    // 行展开、收起事件
    toggleExpandChangeEvent ({row,rowIndex},event) {
        // 判断是展开还是折叠操作
        let expandClass:string = event.target.className;
        if(expandClass.indexOf("expand--active")<0){
            console.log("展开行："+rowIndex);
            this.queryDevices(row,rowIndex);
        }
      }
    queryDevices(row,rowIndex){
        this.devicesTableLoading = true;
        this.deployService.appModuleInstantList(row.id)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    console.log(winResponseData.data);
                    this.pageDataList[rowIndex].devices=winResponseData.data;
                    //  if(row.id==1){
                    //     this.pageDataList[rowIndex].devices=[{"id":1,"createTime":"2019-10-30T13:20:15.934+0000","updateTime":"2019-10-30T13:20:15.934+0000","name":"dev73","alias":"dev73","ipAddress":"192.168.0.73","userName":"root","port":22,"osType":"Centos7","status":1,"desc":null}];
                    // }else{
                    //     this.pageDataList[rowIndex].devices=[{"id":"1","createTime":"2019-10-30T13:20:15.934+0000","updateTime":"2019-10-30T13:20:15.934+0000","name":"test1111","alias":"test1111","ipAddress":"0.0.0.0","userName":"root","port":22,"osType":"Centos7","status":0,"desc":null}];
                    // }
                    // 必须刷新表格数据，否则展开表格devices数据无法更新。
                    this.$refs["xTable"].refreshData();
                } else {
                    this.win_message_error(row.name+"机器服务查询异常："+winResponseData.msg);
                }
            })
            .finally(()=>{
                this.devicesTableLoading=false;
            })
    }
    // 启动应用服务
    startApp(row:DeviceModuleRefVO,rowIndex:number){
        console.log("startApp-"+rowIndex);
        console.log(row);
        if(row.status>0){
            this.win_message_box_warning("服务已经启动");
            return;
        }
        if(row.ipAddress&&row.moduleName){
            this.startBtnLoading = true;
            let params={
                "ipAddress":row.ipAddress,
                "moduleName":row.moduleName
            }
            this.deployService.startAppModule(params)
                .then((winResponseData: WinResponseData) =>{
                    if (WinRspType.SUCC === winResponseData.winRspType) {
                        console.log(winResponseData.data);
                        if(winResponseData.data){
                            console.log(this.pageDataList[rowIndex]);
                            this.queryDevices(this.pageDataList[rowIndex],rowIndex);
                        }
                    } else {
                        this.win_message_error(row.ipAddress+"机器服务查询异常："+winResponseData.msg);
                    }
                })
                .finally(() =>{
                    this.startBtnLoading = false;
                })
        }

    }
    // 停止应用
    stopApp(row:DeviceModuleRefVO,rowIndex:number){
        console.log("stopApp-"+rowIndex);
        console.log(row);
        if(row.status===0){
            this.win_message_box_warning("服务已经停止");
            return;
        }
        if(row.ipAddress&&row.moduleName){ 
            this.stopBtnLoading = true;
            let params={
                "ipAddress":row.ipAddress,
                "moduleName":row.moduleName
            }
            this.deployService.stopAppModule(params)
                .then((winResponseData: WinResponseData) =>{
                    if (WinRspType.SUCC === winResponseData.winRspType) {
                        console.log(winResponseData.data);
                        if(winResponseData.data){
                            this.queryDevices(this.pageDataList[rowIndex],rowIndex);
                        }
                    } else {
                        this.win_message_error(row.ipAddress+"机器服务查询异常："+winResponseData.msg);
                    }
                })
                .finally(() =>{
                    this.stopBtnLoading = false;
                })
        }
    }
}

