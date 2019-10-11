import {WinResponseData} from "../../common/vo/BaseVO";
import AxiosFun from "../../../api/AxiosFun";
import {DeviceReqVO, GroupQueryVO} from "../vo/GroupVO";

export default class DeployService {
    userList() {
        return AxiosFun.get("http://localhost:8888/deploy/sys/user/list", null);
    }
    groupTreeList(vo: GroupQueryVO): Promise<WinResponseData>{
        return AxiosFun.post("http://localhost:8888/deploy/group/pageList", vo);
    }

    deviceList(): Promise<WinResponseData> {
        return AxiosFun.get("http://localhost:8888/deploy/device/list", null);
    }

    deviceConnectTest(vo: DeviceReqVO): Promise<WinResponseData> {
        return AxiosFun.post("http://localhost:8888/deploy/device/connectTest", null);
    }
}
