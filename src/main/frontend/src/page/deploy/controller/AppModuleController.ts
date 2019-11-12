import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import { DeviceReqVO} from "../vo/GroupVO";
import {DialogTitleConst} from "../const/DeployConst";
import dateUtils from "../../common/util/DateUtils";
import { DeployTypeEnum } from "../const/DeployEnum";
import { QueryReqVO, AppModuleTreeVO, UploadVO, DeviceModuleRefVO } from "../vo/AppModuleVO";
import UploadDialog from "../view/uploadDialog.vue";


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
                    this.win_message_box_error(winResponseData.msg);
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
    public pageQuery(pageVO: PageVO) {
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
    public delAppModuleBatch(){
        console.log("***********delAppModuleBatch************");
        console.log(this.selected);
        let delIds=[];
        let names = "";
        this.selected.forEach((row: any) => {
            console.log(row);
                delIds.push(row.id);
                names+=row.name+";";
        });
       
        if(delIds.length>0){
            const h = this.$createElement;
            let msg =  h('p', null, [h('span', null, '确认批量删除应用模块：'),h('i', { style: 'color: red' }, names)]);
            this.win_message_box_success(msg,"提示")
                .then(() => {
                    this.deployService.removeAppModuleBatch(delIds)
                        .then((winResponseData: WinResponseData) =>{
                            if (WinRspType.SUCC === winResponseData.winRspType) {
                                this.queryPageList(this.queryReqVO);
                                this.win_message_success(winResponseData.msg);
                            } else {
                                this.win_message_box_error(winResponseData.msg);
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

    delOne(row: AppModuleTreeVO) {
        if(row.devices && row.devices.length>0){
            this.win_message_box_error("应用已经被部署，请先卸载再删除！");
            return;
        }
        this.win_message_box_warning("确认删除选中应用-"+row.name,"提示")
            .then(() => {
                this.deployService.removeAppModuleById(row.id)
                    .then((winResponseData: WinResponseData) =>{
                        if (WinRspType.SUCC === winResponseData.winRspType) {
                            this.queryPageList(this.queryReqVO);
                            this.win_message_success(winResponseData.msg);
                        } else {
                            this.win_message_box_error(winResponseData.msg);
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
      private formatAppModuleTable({row, column, cellValue, index}) {
        if (column.property === "createTime"  && cellValue) {
            return dateUtils.dateFtt("yyyy-MM-dd hh:mm:ss", new Date(cellValue));
        }
        if(column.property === "status"){
            // 应用无状态
            return "";
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
                    // 必须刷新表格数据，否则展开表格devices数据无法更新。
                    this.$refs["xTable"].refreshData();
                } else {
                    this.win_message_box_error(row.name+"机器服务查询异常："+winResponseData.msg);
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
                        this.win_message_box_error(row.ipAddress+"机器服务查询异常："+winResponseData.msg);
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
                "ipAddress": row.ipAddress,
                "moduleName": row.moduleName
            }
            this.deployService.stopAppModule(params)
                .then((winResponseData: WinResponseData) =>{
                    if (WinRspType.SUCC === winResponseData.winRspType) {
                        console.log(winResponseData.data);
                        if(winResponseData.data){
                            this.queryDevices(this.pageDataList[rowIndex],rowIndex);
                        }
                    } else {
                        this.win_message_box_error(row.ipAddress+"机器服务查询异常："+winResponseData.msg);
                    }
                })
                .finally(() =>{
                    this.stopBtnLoading = false;
                })
        }
    }
}

