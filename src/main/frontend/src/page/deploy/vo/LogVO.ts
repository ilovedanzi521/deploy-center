import { BaseReqVO} from "win-biz";


export class LogQueryReqVO{
    reqPageNum: number;
    reqPageSize: number;
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

export class LogDetailVO {
    name:string;
    operate: string;
    createTime:string;
}
