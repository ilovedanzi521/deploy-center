import {  BaseRepVO} from "win-biz";
import { BaseReqVO } from "../../common/vo/BaseVO";


export class LogQueryReqVO extends BaseReqVO{
    id: number;
    reqPageNum: number = 1;
    reqPageSize: number = 25;

}

export class LogDetailVO extends BaseRepVO{
    username:string;
    operation: string;
    method:string;
    params:string;
    time:number;
    ip:string;
}
