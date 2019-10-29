import {WinResponseData} from "../../common/vo/BaseVO";
import AxiosFun from "win-biz";
import {DeviceRepVO, DeviceReqVO, GroupQueryVO, GroupReqVO} from "../vo/GroupVO";
import { QueryReqVO } from "../vo/AppModuleVO";
import { DeployBaseUrl } from "../const/DeployConst";
import { TaskReqVO } from "../vo/TaskVO";

export default class DeployService {
    taskUnDeploy(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/undeploy?id="+id, null);
    }
    taskDeploy(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/deploy?id="+id, null);
    }
    insertTask(vo: TaskReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/task/save", vo);
    }
    strategyList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/strategy/list", null);
    }
    groupList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/group/list", null);
    }
    public static uploadUrl: string = DeployBaseUrl +"/app/module/upload";

    taskPageList(vo: import("../vo/DeployVO").QueryReqVO) {
        return AxiosFun.post(DeployBaseUrl +"/task/pageList", vo);
    }

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
