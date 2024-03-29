import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
import { DeviceRepVO } from "./GroupVO";
/**********************request VO***********************/
//查询VO
export class QueryReqVO extends BaseReqVO{

}
export class UploadVO {
    
}

/*********************response VO****************/
//响应设备组树形VO
export class AppModuleTreeVO extends BaseRepVO{
    name: string;
    path: string;
    packDir: string;
    packVer: string;
    packFile: string;
    desc: string;
    help: string;
    allowDelete: number;
    hasChildren: boolean;
    devices: DeviceModuleRefVO[]=[];
}
export class AppModuleVO extends BaseRepVO{
    name: string;
    path: string;
    packDir: string;
    packVer: string;
    packFile: string;
    desc: string;
    help: string;
    allowDelete: number;
}

export class DeviceModuleRefVO{
    moduleName:string;
    deviceName:string;
    ipAddress:String;
    status:number=0;
    deviceId:number;
    moduleId:number;
}
