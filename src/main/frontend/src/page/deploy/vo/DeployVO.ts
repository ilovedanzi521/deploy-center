import { BaseReqVO, BaseRepVO } from "../../common/vo/BaseVO";
/**********************request VO***********************/
//查询VO
export class QueryReqVO extends BaseReqVO{

}

/*********************response VO****************/
export class StatisticsRepVO{
    total:number=0;
    success:number=0;
    error:number=0;
    warning:number=0;
    successPercent:number=0;
    errorPercent:number=0;
    warningPercent:number=100;
}

