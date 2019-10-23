import {WinResponseData} from "../../common/vo/BaseVO";
import AxiosFun from "win-biz";
import {DeviceRepVO, DeviceReqVO, GroupQueryVO, GroupReqVO} from "../vo/GroupVO";
import { QueryReqVO } from "../vo/AppModuleVO";
import { DeployBaseUrl } from "../const/DeployConst";

export default class DeployService {

    public static uploadUrl: string = DeployBaseUrl +"/app/module/upload";

    appModulePageList(vo: QueryReqVO) {
        return AxiosFun.post(DeployBaseUrl +"/app/module/pageList", vo);
    }
    removeGroupBatch(delGroupIds: any[]) {
        return AxiosFun.post(DeployBaseUrl +"/group/safeRemoveBatch",delGroupIds);
    }
    userList() {
        return AxiosFun.get(DeployBaseUrl +"/sys/user/list", null);
    }
    groupTreeList(vo: GroupQueryVO): Promise<WinResponseData>{
        return AxiosFun.post(DeployBaseUrl +"/group/pageList", vo);
    }

    deviceList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/device/list", null);
    }

    deviceConnectTest(vo: DeviceReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/device/connectTest", vo);
    }

    insertDevice(vo: DeviceRepVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/device/save", vo);
    }

    saveGroup(vo: GroupReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/group/safeSave", vo);
    }

    removeGroupById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete(DeployBaseUrl +"/group/safeRemove/"+id);
    }

    removeDeviceById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete(DeployBaseUrl +"/device/remove/"+id);
    }
}
