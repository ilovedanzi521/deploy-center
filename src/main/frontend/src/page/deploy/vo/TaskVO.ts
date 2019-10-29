import { BaseRepVO, BaseReqVO } from "win-biz";
import { GroupDetailVO, DeviceRepVO } from "./GroupVO";
import { StrategyRepVO } from "./StrategyVO";
import { AppModuleVO } from "./AppModuleVO";

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
    logPath: string;
    group: GroupDetailVO;
    strategy: StrategyRepVO;
}

export class TaskDetailVO extends BaseRepVO{
    status: number;
    logPath: string;
    groupName: string;
    strategyName: string;
    logInfo:string;
    appModules: AppModuleVO[];
    devices: DeviceRepVO[];
}