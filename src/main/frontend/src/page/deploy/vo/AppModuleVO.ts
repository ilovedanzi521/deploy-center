import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
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
}
