import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
/**********************request VO***********************/
//组查询VO
export class GroupQueryVO extends BaseReqVO {

}
export class DeployPageVO {
    /**
     * 总数量
     */
    public total: number;
    /**
     * 当前页,默认为1
     */
    public current: number = 1;
    /**
     * 每页行数, 默认为10
     */
    public size: number = 10;
    /**
     * 查询总数, 默认为true
     */
    public searchCount: boolean = true;
    /**
     * 数据
     */
    public records: any[];
}

/*********************response VO****************/
//响应设备组树形VO
export class GroupTreeVO {

}
