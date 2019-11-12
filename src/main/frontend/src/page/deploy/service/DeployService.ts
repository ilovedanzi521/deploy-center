import {WinResponseData} from "../../common/vo/BaseVO";
import AxiosFun from "win-biz";
import {DeviceRepVO, DeviceReqVO, GroupQueryVO, GroupReqVO} from "../vo/GroupVO";
import { DeployBaseUrl } from "../const/DeployConst";
import { TaskReqVO } from "../vo/TaskVO";
import { QueryReqVO } from "../vo/DeployVO";
import { LogQueryReqVO } from "../vo/LogVO";

export default class DeployService {
    indexAppInstanceStatistics(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/index/statistics/appInstance", null);
    }
    indexTaskStatistics(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/index/statistics/task", null);
    }
    logPageList(vo: LogQueryReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/sys/log/pageList", vo);
    }
    viewDeviceLog(params: { "ipAddress": string; "strategyName": string; }): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/log/device", params);
    }
    taskInfo(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/detail/"+id, null);
    }
    taskUnDeploy(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/undeploy?id="+id, null);
    }
    taskDeploy(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/task/deploy?id="+id, null);
    }
    insertTask(vo: TaskReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/task/safeSave", vo);
    }
    oneKeyDeploy(vo: TaskReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/task/oneKeyDeploy", vo);
    }
    taskPageList(vo: QueryReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/task/pageList", vo);
    }
    appModuleInstantList(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/app/module/instanceList/"+id, null);
    }
    startAppModule(params: { "ipAddress": string; "moduleName": string; }): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/app/module/moduleStart", params);
    }
    stopAppModule(params: { "ipAddress": string; "moduleName": string; }): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/app/module/moduleStop", params);
    }
    public static uploadUrl: string = DeployBaseUrl +"/app/module/upload";

    appModulePageList(vo: QueryReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/app/module/pageList", vo);
    }
    removeAppModuleById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete(DeployBaseUrl +"/app/module/safeRemove/"+id);
    }
    removeAppModuleBatch(ids: number[]): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/app/module/safeRemoveBatch",ids);
    }
    strategyPageList(vo:QueryReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/strategy/pageList", vo);
    }
    strategyDetail(id: number): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/strategy/detail/"+id, null);
    }
    strategyList() : Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/strategy/list", null);
    }
    groupList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/group/list", null);
    }
    removeGroupBatch(delGroupIds: any[]): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/group/safeRemoveBatch",delGroupIds);
    }
    userList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/sys/user/list", null);
    }
    groupTreeList(vo: GroupQueryVO): Promise<WinResponseData>{
        return AxiosFun.post(DeployBaseUrl +"/group/pageList", vo);
    }

    deviceList(): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/device/list", null);
    }

    deviceConnectTest(vo: DeviceRepVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/device/connectTest", vo);
    }
    testConnectByIp(ipAddress: string): Promise<WinResponseData> {
        return AxiosFun.get(DeployBaseUrl +"/device/connectDev?ipAddr="+ipAddress);
    }

    insertDevice(vo: DeviceRepVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/device/safeSave", vo);
    }

    saveGroup(vo: GroupReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/group/safeSave", vo);
    }
    safeUpdateGroup(vo: GroupReqVO): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/group/safeUpdate", vo);
    }

    removeGroupById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete(DeployBaseUrl +"/group/safeRemove/"+id);
    }

    removeDeviceById(id: number): Promise<WinResponseData> {
        return AxiosFun.winDelete(DeployBaseUrl +"/device/safeRemove/"+id);
    }
    removeDeviceBatch(ids: number[]): Promise<WinResponseData> {
        return AxiosFun.post(DeployBaseUrl +"/device/safeRemoveBatch",ids);
    }
}
