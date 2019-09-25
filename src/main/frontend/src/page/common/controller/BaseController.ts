import Vue from "vue";
import { Component } from "vue-property-decorator";
/**Win插件开始 */
import {
    WinButton,
    // WinDialog,
    WinForm,
    WinFormItem,
    WinInput,
    WinMenu,
    WinMenuItem,
    WinSubmenu,
    WinPagination,
    WinSelect,
    WinOption,
    WinSwitch,
    WinTable,
    WinTableColumn,
    WinTabs,
    WinTabPane,
    WinRow,
    WinCol,
    WinDatePicker,
    WinCheckbox,
    WinTree,
    WinAutocomplete,
    WinDivider,
    WinSelectTable,
    WinButtonGroup,
    WinFold
} from "win-plus";
import { WinDicSelect } from "@win-frond-frameworks/trade-biz";

//新增抽出来的业务组件
import {
    win_message_box_success,
    win_message_box_error,
    win_message_box_info,
    win_message_box_warning,
    win_message_success,
    win_message_warning,
    win_message_error,
    win_message_info,
    WinFdialog
} from "../../../components/index";
//新增抽出来的业务组件

/**Win插件结束 */
// import { DicRepVO } from "../../dictionary/vo/DicVO";
import PageVO from "../vo/PageVO";
@Component({
    components: {
        WinButton,
        WinDatePicker,
        WinForm,
        WinFormItem,
        WinInput,
        WinMenu,
        WinMenuItem,
        WinSubmenu,
        WinPagination,
        WinSelect,
        WinOption,
        WinSwitch,
        WinTable,
        WinTableColumn,
        WinTabs,
        WinTabPane,
        WinRow,
        WinCol,
        WinCheckbox,
        WinTree,
        WinAutocomplete,
        WinDicSelect,
        WinDivider,
        WinSelectTable,
        WinButtonGroup,
        WinFdialog,
        WinFold
    }
})
export default class BaseController extends Vue {
    pageVO: PageVO = new PageVO();

    /**消息提示框方法声明开始 */
    win_message_success: any = win_message_success;
    win_message_warning: any = win_message_warning;
    win_message_error: any = win_message_error;
    win_message_info: any = win_message_info;
    //弹出框
    win_message_box_success: any = win_message_box_success;
    win_message_box_error: any = win_message_box_error;
    win_message_box_info: any = win_message_box_info;
    win_message_box_warning: any = win_message_box_warning;
    /**消息提示框方法声明结束 */

    /**提示信息,success */
    successMessage(info: string) {
        this.$message({
            showClose: true,
            type: "success",
            message: info,
            customClass: "win-success",
            iconClass: "win win-zijintiaozhengfuhe"
        });
    }

    /**提示信息,error */
    errorMessage(info: string) {
        this.$message({
            showClose: true,
            type: "error",
            message: info,
            customClass: "win-error",
            iconClass: "win win-zijintiaozhengfuhe"
        });
    }

    /**提示信息,info */
    infoMessage(info: string) {
        this.$message({
            showClose: true,
            type: "info",
            message: info
        });
    }

    /**提示信息,warning */
    warningMessage(info: string) {
        this.$message({
            showClose: true,
            type: "warning",
            message: info
        });
    }

    /**字典值,表格显示 */
    // dicFormatter(cellValue: string, dics: DicRepVO[]) {
    //     for (var i = 0; i < dics.length; i++) {
    //         if (cellValue === dics[i].dicCode) {
    //             return dics[i].dicExplain;
    //         }
    //     }
    //     return "";
    // }

    /**对象复制 */
    copy(arr) {
        return JSON.parse(JSON.stringify(arr));
    }
}
