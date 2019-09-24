/** 键盘操作 */

const keyCodeOperation = that => {
    const MENU_SIZE = 9; // 最多menu为9
    let menuLength = 0; // 该页面的有多少个分类
    let body = document.body;
    body.addEventListener("keydown", e => {
        /** ESC快速展开一级菜单 */
        if (e.keyCode === 27) {
            that.layoutReqVO.secondMeunIsOpen = !that.layoutReqVO
                .secondMeunIsOpen;
            if (that.layoutReqVO.secondMeunIsOpen) {
                that.layoutReqVO.isKeyCode = true;
                e.preventDefault();
                /*菜单面板第一次展开到第一个*/
                that.layoutReqVO.stwichController.switchsScondMeunIsDetailed = true;
                if (that.layoutReqVO.firstMenuIndex < 0) {
                    that.layoutReqVO.firstMenuIndex = 1;
                }
                /** 记入展开之后在第几个面的index */
                that.layoutReqVO.activeMenuId =
                    that.layoutReqVO.secondMenus[
                        that.layoutReqVO.firstMenuIndex
                    ][0].children[0].id;
            } else {
                that.layoutReqVO.isKeyCode = false;
                that.layoutReqVO.stwichController.switchsScondMeunIsDetailed = false;
            }
        }
        /** 向下按钮向下选择一级菜单 */
        if (e.keyCode === 40) {
            // 重置当前的二级菜单类
            if (!that.layoutReqVO.isKeyCode) {
                return;
            }
            e.preventDefault();
            that.layoutReqVO.menuSize = 0;
            that.layoutReqVO.currentMenu = 0;
            that.layoutReqVO.isNoSearch = false;
            if (!that.layoutReqVO.stwichController.switchsScondMeunIsDetailed) {
                that.layoutReqVO.stwichController.switchsScondMeunIsDetailed = true;
            }
            if (that.layoutReqVO.isKeyCode) {
                that.layoutReqVO.firstMenuIndex =
                    that.layoutReqVO.firstMenuIndex + 1;

                if (that.layoutReqVO.firstMenuIndex > MENU_SIZE) {
                    that.layoutReqVO.firstMenuIndex = 0;
                }

                that.layoutReqVO.activeMenuId =
                    that.layoutReqVO.secondMenus[
                        that.layoutReqVO.firstMenuIndex
                    ][0].children[0].id;
            }
        }

        /** 向上按钮向上选择一级菜单 */
        if (e.keyCode === 38) {
            if (!that.layoutReqVO.isKeyCode) {
                return;
            }
            e.returnValue = false;
            // 重置当前的二级菜单类
            that.layoutReqVO.menuSize = 0;
            that.layoutReqVO.currentMenu = 0;
            that.layoutReqVO.isNoSearch = false;
            if (that.layoutReqVO.isKeyCode) {
                that.layoutReqVO.firstMenuIndex =
                    that.layoutReqVO.firstMenuIndex - 1;

                if (that.layoutReqVO.firstMenuIndex < 0) {
                    that.layoutReqVO.firstMenuIndex = MENU_SIZE;
                }

                that.layoutReqVO.activeMenuId =
                    that.layoutReqVO.secondMenus[
                        that.layoutReqVO.firstMenuIndex
                    ][0].children[0].id;
            }
        }
        /** 向右按钮选择二级菜单分类项 */
        if (e.keyCode === 39) {
            if (
                !that.layoutReqVO.isKeyCode ||
                !that.layoutReqVO.stwichController.switchsScondMeunIsDetailed
            ) {
                return;
            }
            e.preventDefault();
            menuLength = getMeunLength(that);
            that.layoutReqVO.menuSize = that.layoutReqVO.menuSize + 1;
            // 判断每个类是否走完
            if (
                that.layoutReqVO.menuSize >=
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children.length
            ) {
                that.layoutReqVO.currentMenu = that.layoutReqVO.currentMenu + 1;
                that.layoutReqVO.menuSize = 0;
            }
            // 判断整个二级分类是否走完
            if (that.layoutReqVO.currentMenu === menuLength) {
                that.layoutReqVO.menuSize = 0;
                that.layoutReqVO.currentMenu = 0;
            }
            that.layoutReqVO.activeMenuId =
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children[that.layoutReqVO.menuSize].id;
        }

        /** 向左按钮选择二级菜单分类项 */
        if (e.keyCode === 37) {
            if (
                !that.layoutReqVO.isKeyCode ||
                !that.layoutReqVO.stwichController.switchsScondMeunIsDetailed
            ) {
                return;
            }
            e.preventDefault();
            menuLength = getMeunLength(that); // 获取该页面的的二级菜单分类个数
            that.layoutReqVO.menuSize = that.layoutReqVO.menuSize - 1;
            // 判断每个类是否走完
            if (
                that.layoutReqVO.menuSize < 0 &&
                that.layoutReqVO.currentMenu !== 0
            ) {
                // 鼠标定位到上一列的最后一个
                that.layoutReqVO.currentMenu = that.layoutReqVO.currentMenu - 1;
                that.layoutReqVO.menuSize =
                    that.layoutReqVO.secondMenus[
                        that.layoutReqVO.firstMenuIndex
                    ][that.layoutReqVO.currentMenu].children.length - 1;
            }
            // 判断整 个二级分类是走到第一个
            if (
                that.layoutReqVO.currentMenu === 0 &&
                that.layoutReqVO.menuSize < 0
            ) {
                that.layoutReqVO.currentMenu = menuLength - 1;
                that.layoutReqVO.menuSize =
                    that.layoutReqVO.secondMenus[
                        that.layoutReqVO.firstMenuIndex
                    ][that.layoutReqVO.currentMenu].children.length - 1;
            }
            that.layoutReqVO.activeMenuId =
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children[that.layoutReqVO.menuSize].id;
        }

        /** tab的快速切换一组 */
        if (e.keyCode === 9) {
            if (!that.layoutReqVO.isKeyCode) {
                return;
            }
            // e.returnValue = false;
            e.preventDefault();
            that.layoutReqVO.currentMenu = that.layoutReqVO.currentMenu + 1;
            menuLength = getMeunLength(that);
            that.layoutReqVO.menuSize = 0;
            if (that.layoutReqVO.currentMenu >= menuLength) {
                that.layoutReqVO.currentMenu = 0;
            }
            that.layoutReqVO.activeMenuId =
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children[that.layoutReqVO.menuSize].id;
        }

        /** F2按钮添加快捷键 */
        if (e.keyCode === 113) {
            if (
                !that.layoutReqVO.isKeyCode ||
                !that.layoutReqVO.stwichController.switchsScondMeunIsDetailed
            ) {
                return;
            }
            that.toggleFastItem(
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children[that.layoutReqVO.menuSize]
            );
        }
        /** enter按钮快速跳转 */
        if (e.keyCode === 13) {
            if (
                !that.layoutReqVO.isKeyCode ||
                !that.layoutReqVO.stwichController.switchsScondMeunIsDetailed
            ) {
                return;
            }
            that.gotoPath(
                that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex][
                    that.layoutReqVO.currentMenu
                ].children[that.layoutReqVO.menuSize]
            );
            that.layoutReqVO.isKeyCode = false;
            that.layoutReqVO.stwichController.switchsScondMeunIsDetailed = false;
            that.layoutReqVO.otherPanel = false;
            that.layoutReqVO.secondMeunIsOpen = false;
        }

        /** delete删除快捷菜单 */
        if (e.keyCode === 46) {
            if (that.fastId) {
                e.preventDefault();
                that.deleteFastItem(that.fastId);
                that.fastId = "";
            }
        }

        if (e.altKey && e.keyCode === 76) {
            that.$refs.lheader.handleLocking();
        }
    });
};

// 获取二级菜单的长度
const getMeunLength = (that): number => {
    let menuLength: number = 0;
    that.layoutReqVO.secondMenus[that.layoutReqVO.firstMenuIndex].forEach(
        (item, index) => {
            if (item.children.length > 0) {
                menuLength = index;
            }
        }
    );
    menuLength = menuLength + 1;
    return menuLength;
};

export { keyCodeOperation };
