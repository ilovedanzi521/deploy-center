import { WinResponseData } from "../../common/vo/BaseVO";
import { LoginVO } from "../vo/loginVO";
import AxiosFun from "../../../api/AxiosFun";

/**
 * 登录获取数据
 */
export default class loginService {
    static login(vo: LoginVO): Promise<WinResponseData> {
        return AxiosFun.post(
            AxiosFun.authCenterServiceName + "/api/web/login",
            vo
        );
    }
}
