/**
 * 表单弹框对象
 */
export default class DialogVO {
    /** 弹框标题 */
    public title: string = "";
    /** 弹框是否禁用 */
    public disableFlag: boolean = false;
    /** 弹框功能，0查看,1新增 2修改 3删除 */
    public action: number = 1;
    /** 是否显示 */
    public visible: boolean = false;
    /** 是否可以通过点击 modal 关闭 Dialog */
    public closeOnClickModal: boolean = false;

    public getSeeDialog(title): DialogVO {
        this.title = title;
        this.action = 0;
        this.disableFlag = true;
        this.visible = true;
        return this;
    }

    public getAddDialog(title): DialogVO {
        this.title = title;
        this.action = 1;
        this.disableFlag = false;
        this.visible = true;
        return this;
    }

    public getUpdateDialog(title): DialogVO {
        this.title = title;
        this.action = 2;
        this.disableFlag = false;
        this.visible = true;
        return this;
    }

    public getDeleteDialog(title): DialogVO {
        this.title = title;
        this.action = 3;
        this.disableFlag = true;
        this.visible = true;
        return this;
    }
}
