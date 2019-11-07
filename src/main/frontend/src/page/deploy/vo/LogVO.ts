import {  BaseRepVO} from "win-biz";
import { BaseReqVO } from "../../common/vo/BaseVO";


export class LogQueryReqVO extends BaseReqVO{


}
export class LogPageVO{
    /**
     * 总数量
     */
    total: number;
    /**
     * 当前页
     */
    pageNum: number = 1;
    /**
     * 每页行数
     */
    pageSize: number = 50;
    /**
     * 数据
     */
    list: any[];
}

export class LogDetailVO extends BaseRepVO{
    username:string;
    operation: string;
    method:string;
    params:string;
    time:number;
    ip:string;
}
