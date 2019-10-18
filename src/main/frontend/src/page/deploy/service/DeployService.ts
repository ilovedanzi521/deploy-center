import {WinResponseData} from "../../common/vo/BaseVO";
import AxiosFun from "../../../api/AxiosFun";
import {DeviceRepVO, DeviceReqVO, GroupQueryVO, GroupReqVO} from "../vo/GroupVO";
import { QueryReqVO } from "../vo/AppModuleVO";

export default class DeployService {
    appModulePageList(vo: QueryReqVO) {
        return AxiosFun.post("http://localhost:8888/deploy/app/module/pageList", vo);
    }
    removeGroupBatch(delGroupIds: any[]) {
        return AxiosFun.post("http://localhost:8888/deploy/group/safeRemoveBatch",delGroupIds);
    }
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
        return AxiosFun.post("http://localhost:8888/deploy/device/connectTest", vo);
    }

    insertDevice(vo: DeviceRepVO): Promise<WinResponseData> {
        return AxiosFun.post("http://localhost:8888/deploy/device/save", vo);
    }

    saveGroup(vo: GroupReqVO): Promise<WinResponseData> {
        return AxiosFun.post("http://localhost:8888/deploy/group/safeSave", vo);
    }

    removeGroupById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete("http://localhost:8888/deploy/group/safeRemove/"+id);
    }

    removeDeviceById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete("http://localhost:8888/deploy/device/remove/"+id);
    }
}
