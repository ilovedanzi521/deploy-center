import { BaseRepVO, BaseReqVO } from "win-biz";
import { AppModuleVO } from "./AppModuleVO";

export class StrategyReqVO extends BaseReqVO{

}

export class StrategyRepVO extends BaseRepVO {
    name:String;
    path:String;
    desc:String;
}
export class StrategyDetailVO extends BaseRepVO{
    name:string;
    path:string;
    desc:string;
    help:string;
    allow_delete:number;
    shellContent: string[];
    appModules: AppModuleVO[];
}
