import Vue from "vue";
import { Component } from "vue-property-decorator";
import pinyin from "convertPinyin";
import LayoutService from "../service/layoutService";
import { LayoutReqVO } from "../vo/LayoutVO";
import BaseController from "../../page/common/controller/BaseController";
import { WinRspType } from "../../page/common/enum/BaseEnum";
import { WinResponseData } from "../../page/common/vo/BaseVO";
import { keyCodeOperation } from "./keyDown";
@Component({})
export default class LayoutController extends BaseController {
    public $router;
    public $set;
    public $refs;
    public tabIndex = 1;
    public time = null;
    public layoutReqVO: LayoutReqVO = new LayoutReqVO();
    public currentColor;
    public fastId: string = ""; // 获取快速菜单ID好，进行键盘DElete操作
    /** 加载一级二级菜单 */
    public async getMenuList() {
        const menuTreeParam = { userId: localStorage.getItem("userName") };
        const result = await LayoutService.menuTree(menuTreeParam);
        const firstMenus = result.data; // 获取一级菜单
        this.layoutReqVO.firstMents = firstMenus.map(item => {
            return {
                menuName: item.menuName,
                id: item.id,
                menuIcon: item.menuIcon || "win-kezhiyazhengquanchaxun"
            };
        });
        // 加载用户最近访问菜单
        const lasterVisitMenus = await LayoutService.lasterVisitMenu();
        lasterVisitMenus.data[0].id = 0;
        lasterVisitMenus.data[0].menuIcon =
            lasterVisitMenus.data[0].menuIcon || "win-kezhiyazhengquanchaxun";
        // 添加最近访问一级菜单
        this.layoutReqVO.firstMents = [
            lasterVisitMenus.data[0],
            ...this.layoutReqVO.firstMents
        ];

        // 添加最近访问二级菜单
        const storageArray = [];
        firstMenus.forEach(next => {
            if (next.children) {
                next.children.forEach(next2 => {
                    if (!storageArray[next2.parentId]) {
                        storageArray[next2.parentId] = [];
                        const storageArea = {
                            parentId: next2.parentId,
                            menuName: next2.menuName,
                            id: next2.id,
                            children: next2.children.map(item => {
                                return {
                                    ...item,
                                    firstPy: this.toPinyinFirst(item.menuName)
                                };
                            }),
                            menuAddress: next2.menuAddress
                        };
                        storageArray[next2.parentId].push(storageArea);
                    } else {
                        const storageArea = {
                            parentId: next2.parentId,
                            menuName: next2.menuName,
                            id: next2.id,
                            children: next2.children.map(item => {
                                return {
                                    ...item,
                                    firstPy: this.toPinyinFirst(item.menuName)
                                };
                            }),
                            menuAddress: next2.menuAddress
                        };
                        storageArray[next2.parentId].push(storageArea);
                    }
                });
            }
        });

        this.layoutReqVO.secondMenus = storageArray;
        lasterVisitMenus.data[0].children.forEach(element => {
            element.children = element.children.map(item => {
                item = Object.assign({}, item, {
                    firstPy: this.toPinyinFirst(item.menuName)
                });
                return item;
            });
        });
        this.layoutReqVO.secondMenus[0] = lasterVisitMenus.data[0].children;
    }

    /** 增加最近访问区 */
    public async addLasterVisitMenuItem(menu) {
        await LayoutService.addVisitMenu({ menuId: menu.id });
        const lasterVisitMenus = await LayoutService.lasterVisitMenu();
        this.layoutReqVO.secondMenus[0] = lasterVisitMenus.data[0].children;
    }

    /**
     * 加载快速导航区
     */
    public async getFastList() {
        const fastMenus = await LayoutService.collectMenus({});
        fastMenus.data.forEach(item => {
            if (!item.menuIcon) {
                item.menuIcon = "win-kezhiyazhengquanchaxun";
            }
        });
        this.layoutReqVO.fastMenus = fastMenus.data;
    }

    /**
     *  点击菜单面板的菜单跳转到页面，并添加一个tab
     */
    public gotoPath(menu) {
        const menuAddress = menu.menuAddress;
        if (!menuAddress) {
            return;
        }
        //  增加一个最近访问
        this.addLasterVisitMenuItem(menu);
        // 同步vuex
        clearTimeout(this.time);
        this.time = setTimeout(() => {
            this.$store.commit("setMenuAddress", {
                menuAddress: menuAddress
            });
        }, 300);
        //切换页面刷新状态
        // this.$store.commit("setRefresh",true);
        this.addTab(menu);
        this.$router.push({
            name: "directional",
            params: { address: menuAddress }
        });
        this.layoutReqVO.secondMeunIsOpen = false;
        this.layoutReqVO.isKeyCode = false;
        this.layoutReqVO.stwichController.switchsScondMeunIsDetailed = false;
        if (this.layoutReqVO.moreFastPanelisOpen) {
            this.layoutReqVO.moreFastPanelisOpen = false;
        }
    }
    /**
     *  切换收藏快速菜单
     */
    public async toggleFastItem(item) {
        if (item.collect) {
            this.$set(item, "collect", 0);
            const result = await LayoutService.deleteCollectMenuByMenuId(
                item.id
            );
            this.successMessage("快捷菜单删除成功");
        } else {
            this.$set(item, "collect", 1);
            if (!item.menuIcon) {
                this.$set(item, "menuIcon", "win-kezhiyazhengquanchaxun");
            }

            await LayoutService.addCollectmenu({ menuId: item.id });
            this.layoutReqVO.fastMenus = [item, ...this.layoutReqVO.fastMenus];
            this.successMessage("快捷菜单添加成功");
        }
        this.$forceUpdate();
        this.getFastList();
        // this.getMenuList();
    }

    /**
     *  删除快捷菜单导航区
     */
    public async deleteFastItem(id) {
        this.layoutReqVO.fastMenus = this.layoutReqVO.fastMenus.filter(
            item => item.id !== id
        );
        const storageArray = [];
        this.layoutReqVO.secondMenus.forEach(item => {
            storageArray.push(JSON.stringify(item));
        });
        const result = await LayoutService.deleteCollectmenu(id);
        this.getMenuList();
        this.successMessage("删除快捷菜单成功");
        return result;
    }
    /**
     *  获取快速导航区元素ID
     */
    public hasFastId(fastId) {
        this.fastId = fastId;
    }

    /**
     *  修改密码
     */

    public modifyPassWord(params) {
        LayoutService.modifyPassword(params).then(
            (winResponseData: WinResponseData) => {
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    this.layoutReqVO.passwordController = false;
                    this.successMessage("修改密码成功");
                } else {
                    this.errorMessage(winResponseData.msg);
                }
            }
        );
    }

    // @Watch("layoutReqVO.isKeyCode")
    // onChildChanged(val: string, oldVal: string) {
    //     document.body.removeEventListener('keydown');
    // }
    /**
     *  点击导航菜单切换菜单面板
     */

    public changeSecondMenu(id) {
        this.layoutReqVO.activeMenuId = "";
        this.layoutReqVO.firstMenuIndex = id;
        const secondMenus = this.layoutReqVO.secondMenus[id][0];

        if (secondMenus.children && secondMenus.children.length > 0) {
            this.layoutReqVO.activeMenuId = secondMenus.children[0].id;
        }
    }

    public closeSecondMenus() {
        this.layoutReqVO.secondMeunIsOpen = false;
    }

    /***添加页面tab标签 */
    public addTab(targetName) {
        let findItem = this.layoutReqVO.tabs.filter(
            item => item.id === targetName.id
        );
        if (findItem[0]) {
            this.layoutReqVO.editableTabsValue2 = findItem[0].name;
            return;
        }
        let newTabName = ++this.tabIndex + "";
        this.layoutReqVO.tabs.push({
            ...targetName,
            name: newTabName
        });

        this.layoutReqVO.editableTabsValue2 = newTabName;
        //  点击增加一个最近访问菜单
        this.layoutReqVO.secondMenus[0];
    }

    /***锁频 */
    public handleLockScreen(loginPassword) {
        // let option = {
        //     loginPassword: loginPassword
        // };
        // LayoutService.lockScreen(option).then(
        //     (winResponseData: WinResponseData) => {
        //         if (WinRspType.SUCC == winResponseData.winRspType) {
        //             localStorage.setItem("lockName", Math.random() * 54 + "");
        //             this.layoutReqVO.nameLock = localStorage.getItem(
        //                 "lockName"
        //             );
        //             this.successMessage("锁屏成功");
        //             this.inFullCreeen();
        //         } else {
        //             this.errorMessage(winResponseData.msg);
        //         }
        //     }
        // );
    }
    /***解屏 */
    public handleSolutionScreen(loginPassword) {
        let option = {
            loginPassword: loginPassword
        };
        LayoutService.solutionScreen(option).then(
            (winResponseData: WinResponseData) => {
                if (WinRspType.SUCC === winResponseData.winRspType) {
                    localStorage.removeItem("lockName");
                    this.layoutReqVO.nameLock = "";
                    this.layoutReqVO.lockisOpen = false;
                    this.successMessage("解屏成功");
                } else {
                    this.errorMessage(winResponseData.msg);
                }
            }
        );
    }

    /***关闭锁屏面板 */
    public closeLock() {
        this.layoutReqVO.lockisOpen = false;
    }

    /***全屏功能*/
    public inFullCreeen() {
        if (document.body.requestFullscreen) {
            document.body.requestFullscreen();
        }
    }

    /***关闭所用功能*/
    public closeAllPanle() {
        this.layoutReqVO.otherPanel = false;
        this.layoutReqVO.secondMeunIsOpen = false;
        this.layoutReqVO.stwichController.switchsScondMeunIsDetailed = false;
        this.layoutReqVO.secondMenus = [...this.layoutReqVO.storageMenus];
    }

    /*获取菜单页面的汉字首字母*/
    public toPinyinFirst(value) {
        let d = pinyin(value);
        let pinyinFirst = "";
        d.forEach(element => {
            pinyinFirst += element.substring(0, 1);
        });
        return pinyinFirst;
    }

    /***页面初始化功能*/
    public mounted() {
        // 页面第一次加载获取主题皮肤
        if (localStorage.getItem("theme")) {
            this.layoutReqVO.changeTheme = localStorage.getItem("theme");
        }
        // 开发环境
        console.log(process.env.NODE_ENV);
        if ("development" === process.env.NODE_ENV) {
            return;
        }

        // var authorization: string =
        localStorage.getItem("Authorization");
        // if (!authorization) {
        //     this.$router.push({
        //         path: "/login"
        //     });
        //     return;
        // }

        this.$store.commit("setMenuAddress", {
            menuAddress: this.layoutReqVO.tabs[0].menuAddress
        });

        // 页面加载首页路由
        this.$router.push({
            name: "directional",
            params: { address: this.layoutReqVO.tabs[0].menuAddress }
        });

        this.layoutReqVO.userName = localStorage.getItem("userName");
        // 加载快捷菜单
        this.getFastList();
        // 加载用户菜单
        this.getMenuList();
        // 加载用户菜单 键盘快速操作展开菜单
        keyCodeOperation(this);
    }

    /*换亮色 */
    public changeLight() {
        this.changeColor("light");
    }
    /*换暗色*/
    public changeDark() {
        this.changeColor("dark");
    }
    /*换色*/
    public changeColor(color) {
        this.currentColor = color;
        let frameDoms = document.getElementsByTagName("iframe");
        for (let i = 0; i < frameDoms.length; i++) {
            if (
                frameDoms[i].contentDocument.getElementsByTagName("section")[0]
            ) {
                frameDoms[i].contentDocument
                    .getElementsByTagName("section")[0]
                    .setAttribute("class", color);
            } else {
                console.error("该IFRAMES下未找到section");
            }
        }
        this.layoutReqVO.changeTheme = this.currentColor;
        localStorage.setItem("theme", this.currentColor);
    }
}
