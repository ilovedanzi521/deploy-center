import { BaseRepVO } from "win-biz";
import { GroupDetailVO } from "./GroupVO";
import StrategyRepVO from "./StrategyVO";

/**********************request VO***********************/


/*********************response VO****************/
export default class TaskTableVO extends BaseRepVO{
    strategyId: number;
    groupId: number;
    status: number;
    group: GroupDetailVO;
    strategy: StrategyRepVO;
}

