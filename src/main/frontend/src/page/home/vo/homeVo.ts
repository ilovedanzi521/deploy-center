import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
interface Menu {
    id: string;
    title: string;
    icon: string;
    path: string;
}
export class HomeVO extends BaseReqVO {
    firstMenuArray: Menu[];
    secondMenuArray: Menu[];
    fastMenuArray: Menu[];
    username: string = "";
    tonk: string = "";
}
