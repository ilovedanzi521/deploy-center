import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import PageVO from "../../common/vo/PageVO";
import {OperationTypeEnum} from "../../common/enum/OperationTypeEnum";
import {GroupDetailVO, GroupQueryVO, GroupTreeVO} from "../vo/GroupVO";
import {GroupConst} from "../const/DeployConst";
import GroupDialog from "../view/groupDialog.vue";


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

    /** 初始化*/
    private mounted() {
        this.queryGroupTreeList(this.groupQueryVO);
    }

    /** 查询设备组树形列表*/
    public queryGroupTreeList(groupQueryVO: GroupQueryVO) {
        this.groupLoading = true;
        this.deployService.groupTreeList(this.groupQueryVO)
            .then((winResponseData: WinResponseData) =>{
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.groupTreeList = winResponseData.data.list;
                    this.pageVO = winResponseData.data;
                } else {
                    this.errorMessage(winResponseData.msg);
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
                dialogTitle: GroupConst.ADD_GROUP,
                type: OperationTypeEnum.ADD,
                data: new GroupTreeVO()
            };
        }if (type === OperationTypeEnum.UPDATE) {
            this.groupDialogMsg = {
                dialogTitle: GroupConst.EDIT_GROUP,
                type: OperationTypeEnum.UPDATE,
                data: this.copy(row)
            };
        }
        this.isGroupDialog = true;
    }
    public delGroupBatch(){

    }
    public handleEdit(row) {
        console.log(row);
    }
    public handleDelete(row) {
        console.log(row);
    }
    // 子组件回调函数
    private toGroupDialogForm(msg: WinRspType) {
        this.isGroupDialog = false;
        if (msg === WinRspType.SUCC) {
            this.groupQueryVO.reqPageNum = 1;
            this.queryGroupTreeList(this.groupQueryVO);
        }
    }

}

