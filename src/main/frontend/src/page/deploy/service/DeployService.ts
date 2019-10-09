import {WinResponseData} from "../../common/vo/BaseVO";
import {GroupQueryVO} from "../vo/DeployVO";
import AxiosFun from "../../../api/AxiosFun";

export default class DeployService {
    userList() {
        return AxiosFun.get("http://localhost:8080/deploy/sys/user/list", null);
    }
    groupTreeList(vo: GroupQueryVO): Promise<WinResponseData>{
        return AxiosFun.post("http://localhost:8888/deploy/group/pageList", vo);
    }
}
