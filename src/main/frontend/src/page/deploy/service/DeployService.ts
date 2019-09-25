import { WinResponseData } from "../../common/vo/BaseVO";
import {DeviceReqVO} from "../vo/DeployVO";
import AxiosFun from "../../../api/AxiosFun";

export default class DeployService {
    userList() {
        return AxiosFun.get("http://localhost:8080/deploy/sys/user/list", null);
    }

    deviceList(vo: DeviceReqVO): Promise<WinResponseData> {
        console.log("##########url=/deploy/sys/user/list###########");
        return AxiosFun.get("http://localhost:8080/deploy/sys/user/list", vo);
    }
}
