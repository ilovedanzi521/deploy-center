import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import DeployService from "../service/DeployService";
import {GroupQueryVO, GroupTreeVO} from "../vo/DeployVO";
import PageVO from "../../common/vo/PageVO";


@Component({ components: {} })

export default class GroupController extends BaseController {
    private deployService: DeployService = new DeployService();
    /** 分页对象 */
    public pageVO: PageVO = new PageVO();
    private groupQueryVO: GroupQueryVO = new GroupQueryVO();
    private groupTreeList: GroupTreeVO[]=[];

    private mounted() {
        this.groupQueryVO.reqPageNum = 1;
        this.queryGroupTreeList(this.groupQueryVO);
    }

    public queryGroupTreeList(groupQueryVO: GroupQueryVO) {
        console.log("##########serachDeviceList###########");
        this.deployService.groupTreeList(this.groupQueryVO).then((winResponseData: WinResponseData) =>{
            console.log(winResponseData);
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.groupTreeList = winResponseData.data.list;
                this.pageVO = winResponseData.data;
            } else {
                this.errorMessage(winResponseData.msg);
            }
        });
    }

    /**
     * 字典项分页查询
     *
     * @param vo
     */
    public groupPageQuery(pageVO: PageVO) {
        this.groupQueryVO.reqPageNum = pageVO.pageNum;
        this.groupQueryVO.reqPageSize = pageVO.pageSize;
        this.queryGroupTreeList(this.groupQueryVO);
    }
}
