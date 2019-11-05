import Component from "vue-class-component";
import { WinRspType } from "../../common/enum/BaseEnum";
import { WinResponseData } from "../../common/vo/BaseVO";
import BaseController from "../../common/controller/BaseController";
import { LogDetailVO, LogPageVO, LogQueryReqVO } from "../vo/LogVO";

@Component({ components: {} })

export default class IndexController extends BaseController {
    // 日志表格数据
    private logTableData: LogDetailVO[]=[];
    private logQueryVO: LogQueryReqVO=new LogQueryReqVO();
    public logPageVO: LogPageVO = new LogPageVO();

    mounted(){
        this.queryLogPageList(this.logQueryVO);
    }
    queryLogPageList(queryVO: LogQueryReqVO) {
        let logVO ={
            "name":"admin",
            "operate": "新增设备",
            "createTime" :"2019-11-04 12:12:12"
        };
        for (let index = 0; index < 25; index++) {
            this.logTableData.push(logVO);
        }
    }
    logPageQuery(pageVO: LogPageVO) {
        this.logQueryVO.reqPageNum = pageVO.pageNum;
        this.logQueryVO.reqPageSize = pageVO.pageSize;
        this.queryLogPageList(this.logQueryVO);
    }
    // 跳到任务列表页
    toTaskVue(){
        this.win_message_box_info("跳到任务列表页");
    }
    // 跳到应用模块列表页
    toAppModuleVue(){
        this.win_message_box_info("跳到应用模块列表页");
    }
    progressColor(percentage:number){
        if (percentage < 30) {
            return '#909399';
          } else if (percentage < 70) {
            return '#e6a23c';
          } else {
            return '#67c23a';
        }
    }
}
