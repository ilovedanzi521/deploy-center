import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import PageVO from "../../common/vo/PageVO";
import DeployService from "../service/DeployService";
import {UserRepVO} from "../vo/DeployVO";


@Component({ components: {} })

export default class UserListController extends BaseController {
    private deployService: DeployService = new DeployService();
    private userList: UserRepVO[]=[];

    private mounted() {
        this.serachDeviceList();
    }

    public serachDeviceList() {
        this.deployService.userList().then((winResponseData: WinResponseData) =>{
            console.log(winResponseData);
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.userList = winResponseData.data;
            } else {
                this.errorMessage(winResponseData.msg);
            }
        });
    }
}
