import Vue from "vue";
import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import PageVO from "../../common/vo/PageVO";
import DeployService from "../service/DeployService";
import {DeviceRepVO, DeviceReqVO} from "../vo/DeployVO";


@Component({ components: {} })

export default class DeviceController extends BaseController {
    private deployService: DeployService = new DeployService();
    private deviceReqVO: DeviceReqVO = new DeviceReqVO();
    private deviceList: DeviceRepVO[]=[];

    private mounted() {
        this.serachDeviceList(this.deviceReqVO);
    }

    public serachDeviceList(deviceReqVO: DeviceReqVO) {
        console.log("##########serachDeviceList###########");
        this.deployService.deviceList(deviceReqVO).then((winResponseData: WinResponseData) =>{
            console.log("##########winResponseData###########");
            console.log(winResponseData);
            if (WinRspType.SUCC === winResponseData.winRspType) {
                this.deviceList = winResponseData.data;
            } else {
                this.errorMessage(winResponseData.msg);
            }
        });
    }
}
