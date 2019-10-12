import {BaseRepVO, BaseReqVO} from "../../common/vo/BaseVO";
/**********************request VO***********************/
//组查询VO
export class GroupQueryVO extends BaseReqVO{

}
export class GroupReqVO{
    id: number;
    name: string;
    desc: string;
    deviceIds: number[];
}
export class DeviceReqVO extends BaseReqVO{
    ipAddress:String;
    userName:string;
    port:number;
}

/*********************response VO****************/
//响应设备组树形VO
export class GroupTreeVO extends BaseRepVO{
    name: string;
    ipAddress: string;
    osType: string;
    userName: string;
    port: number;
    status: number;
    desc: string;
    children: GroupTreeVO[] = [];
}

export class GroupDetailVO extends BaseRepVO {

}

export class DeviceRepVO extends BaseRepVO{
    name:string;
    alias:string;
    ipAddress:String;
    userName:string;
    port:number;
    osType:string;
    status:number=0;
    desc:string;
}

