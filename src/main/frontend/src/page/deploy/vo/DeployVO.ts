import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
export class DeployReqVO extends BaseReqVO {
    commonParam: string;
    dicCode: string;
    dicExplain: string;
    parentDicCode: string;
    parentDicCodeList: Array<string>;
    param1: string;
    param2: string;
    param3: string;
    paramExplain: string;
}

export class DeployRepVO extends BaseRepVO {
    dicCode: string;
    dicExplain: string;
    parentDicCode: string;
    param1: string;
    param2: string;
    param3: string;
    paramExplain: string;
}

export class DeviceReqVO extends BaseReqVO{
    name: string;
    ip_addr: string;
    user: string;
    port: string;
    os_type: string;
}
export class DeviceRepVO extends BaseRepVO{
    name: string;
    ip_addr: string;
    user: string;
    port: string;
    os_type: string;
    status: number
}
// 用户实体
export class UserRepVO extends BaseRepVO{
    name: string;
    password: string
}
