import { BaseRepVO, BaseReqVO } from "win-biz";
import { GroupDetailVO } from "./GroupVO";
import { StrategyRepVO } from "./StrategyVO";

/**********************request VO***********************/
export class TaskReqVO{
    id: number;
    strategyId: number=null;
    groupId: number=null;
    status: number = 0;
}

/*********************response VO****************/
export class TaskTableVO extends BaseRepVO{
    strategyId: number;
    groupId: number;
    status: number;
    group: GroupDetailVO;
    strategy: StrategyRepVO;
}

