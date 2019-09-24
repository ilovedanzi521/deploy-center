import { BaseReqVO, BaseRepVO, WinResponseData } from "../../common/vo/BaseVO";
export class LoginUserReqVO extends BaseReqVO {
    userName: string;
    userPassword: string;
    validateCode: string;
}
