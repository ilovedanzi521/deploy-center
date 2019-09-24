/* @二级导航组件
 * @Author: mikey.zhz
 * @Date: 2019-05-21 13:42:17 
 * @Last Modified time: 2019-05-21 13:10:04
 */

<template>
    <section class="menu-container">
        <div>
            <div class="menu-panel">
                <div class="search">
                    <i class="win win-el-icon-search"></i>
                    <input type="text" class="searchInput" placeholder="请输入关键词" v-model.trim="serverValueArray[index]" @input="searchMeunItem(serverValueArray[index])" v-winfocus v-for="(element,index) in serverValueArray"
                        v-if="index==layoutReqVO.firstMenuIndex" :key="serverValueArray[index]" :test_name="'indexMenu_'+index" />
                </div>
                <div class="menu-second">
                    <div v-if="layoutReqVO.isNoSearch" class="noSearch">
                        <p>
                            <i class="win win-hint"></i>
                        </p>
                        <p>
                            无结果
                        </p>
                    </div>
                    <div v-else class="dl_contanier">
                        <dl class="second-menu-floor" v-for="(item,firstIndex) in layoutReqVO.secondMenus[layoutReqVO.firstMenuIndex]" :key="item.id" v-show="item.children && item.children.length > 0">
                            <dt>{{item.menuName}}</dt>
                            <dd v-for="(menuItem,index) in item.children" :key="menuItem.menuName" @click="gotoMenuPath(menuItem,item)" :class="{'active':menuItem.id==layoutReqVO.activeMenuId}"
                                @mouseover="changeMeunItem(menuItem,firstIndex,index)">
                                <span :class="['win',menuItem.menuIcon||'win-kezhiyazhengquanchaxun']" :style="{'marginRight':'10px','paddingLeft':'10px'}"></span>
                                <span :class="{'noAddress':!menuItem.menuAddress}">{{menuItem.menuName}}</span>
                                <i @click.stop="handleToggleFast(menuItem)" v-if="!menuItem.collect" class="startImg">
                                    <img src="../assets/image/start.png" width="14" height="14" />
                                </i>
                                <i :class="['icon-11','win','win-star' ,{'active':menuItem.collect}]" @click.stop="handleToggleFast(menuItem)" v-else></i>
                            </dd>
                        </dl>
                    </div>
                </div>
                <span class="close icon-2" @click="closeSecondMenus">✖</span>
            </div>
        </div>
    </section>
</template>
<script lang="ts">
import Vue from "vue";
import { Component, Prop, Emit } from "vue-property-decorator";
import { LayoutReqVO } from "./vo/LayoutVO";
import { clearTimeout, setTimeout } from "timers";

@Component({})
export default class LSecondMenu extends Vue {
    serverValue: string = "";
    times;
    serverValueArray = new Array(10);
    // storageMenus;
    @Prop()
    layoutReqVO: LayoutReqVO;

    /**
     *添加菜单到快速导航栏
     * */
    @Emit("toggleFastItem")
    handleToggleFast(item) {
        return item;
    }

    /**
     *
     *跳转点击菜单
     *
     * */
    @Emit("gotoPath")
    gotoMenuPath(item) {
        return item;
    }

    /**
     *关闭当前菜单
     *
     * */
    close() {
        this.closeSecondMenus();
    }

    /**
     *菜单查询
     *
     * */

    searchMeunItem(searchValue) {
        if (this.times) {
            clearTimeout(this.times);
        }

        this.times = setTimeout(() => {
            this.setSearchMenuItem(searchValue);
        }, 500);
    }

    changeMeunItem(item, firstIndex, index) {
        this.layoutReqVO.menuSize = index;
        this.layoutReqVO.currentMenu = firstIndex;
        this.layoutReqVO.activeMenuId = item.id;
    }
    /**
     *查找菜单的搜索的元素
     *
     * */
    setSearchMenuItem(searchValue) {
        this.layoutReqVO.isNoSearch = false;
        this.layoutReqVO.secondMenus[
            this.layoutReqVO.firstMenuIndex
        ] = this.layoutReqVO.storageMenus[this.layoutReqVO.firstMenuIndex];
        let searchArray = [];
        this.layoutReqVO.secondMenus[this.layoutReqVO.firstMenuIndex].forEach(
            (item, pareIndex) => {
                item.children.forEach((element, index) => {
                    if (element.menuName.indexOf(searchValue) >= 0) {
                        let searchItem = {
                            ...element,
                            parentMenuName: item.menuName,
                            parentfirstId: item.parentId
                        };
                        searchArray.push(searchItem);
                    } else if (
                        element.firstPy.indexOf(searchValue.toLowerCase()) >= 0
                    ) {
                        let searchItem = {
                            ...element,
                            parentMenuName: item.menuName,
                            parentfirstId: item.parentId
                        };
                        searchArray.push(searchItem);
                    }
                });
            }
        );
        if (!searchArray.length) {
            this.layoutReqVO.isNoSearch = true;
            return;
        }
        this.formatArray(searchArray);
    }

    /*
     *
     *
     *
     *  获取到搜索的结构组装成搜索要的数据
     *
     *
     */

    formatArray(searchArray) {
        let d = {};
        searchArray.forEach(item => {
            if (!d[item.parentId]) {
                d[item.parentId] = {
                    parentId: item.parentfirstId,
                    menuName: item.parentMenuName,
                    id: item.parentId,
                    children: [item]
                };
            } else {
                d[item.parentId].children.push(item);
            }
        });
        let lastSearchArray = [];
        let f = Object.keys(d);
        f.forEach(item => {
            lastSearchArray.push(d[item]);
        });
        this.layoutReqVO.secondMenus[this.layoutReqVO.firstMenuIndex] = [
            ...lastSearchArray
        ];
        this.layoutReqVO.secondMenus = [...this.layoutReqVO.secondMenus];
        try {
            this.layoutReqVO.activeMenuId = this.layoutReqVO.secondMenus[
                this.layoutReqVO.firstMenuIndex
            ][0].children[0].id;
        } catch (e) {
            console.log(e);
        }
        this.$forceUpdate();
    }

    closeSecondMenus() {
        this.layoutReqVO.stwichController.switchsScondMeunIsDetailed = false;
        this.layoutReqVO.isKeyCode = false; //将键盘事件重置到默认属性
        this.layoutReqVO.secondMeunIsOpen = false;
        this.layoutReqVO.secondMenus = [...this.layoutReqVO.storageMenus];
    }

    mounted() {
        this.layoutReqVO.storageMenus = [...this.layoutReqVO.secondMenus];
    }
}
</script>

<style lang="scss" scoped>
@import "../assets/style/element.scss";
.menu-container {
    // overflow-y: auto;
    overflow: hidden;
    .menu-panel {
        position: relative;
        max-width: 800px;
        left: 0;
        top: 0;
        bottom: 0;
        padding: 12px 34px 12px 50px;
        // background: #1f2640;//换色
        box-sizing: border-box;
        z-index: 1;
        .lately {
            dt {
                margin-bottom: 16px;
                font-size: 12px;
                font-weight: bolder;
                color: $color-orange;
            }
            dd {
                display: inline-block;
                margin: 0 130px 10px 0;
                vertical-align: middle;
                font-size: 12px;
                color: $color-white;
                cursor: pointer;
                // &:hover {
                //     color: $color-orange;
                // }
                &:last-of-type {
                    margin-right: 0;
                }
            }
        }
        .menu-second {
            max-width: 800px;
            .dl_contanier {
                display: flex;
                max-width: 800px;
                flex-wrap: wrap;
                overflow-y: auto;
                height: 800px;
                &::-webkit-scrollbar {
                    /*滚动条整体样式*/
                    width: 16px;
                    height: 1px;
                    /*高宽分别对应横竖滚动条的尺寸*/
                    box-sizing: border-box;
                }

                &::-webkit-scrollbar-track {
                    /*滚动条里面轨道*/
                    width: 6px !important;
                    background: #323952ff;
                }

                &::-webkit-scrollbar-thumb {
                    /*滚动条里面小方块*/
                    border-radius: 36%;
                    background: linear-gradient(
                        to right,
                        transparent 0,
                        transparent 10%,
                        transparent 20%,
                        #090f26ff 30%,
                        #090f26ff 40%,
                        #090f26ff 50%,
                        #090f26ff 60%,
                        #090f26ff 70%,
                        transparent 80%,
                        transparent 90%,
                        transparent 100%
                    );
                }
                &::-webkit-scrollbar-button{
                    background:#323952ff
                }
            }
            // overflow: hidden;
            .second-menu-floor {
                width: 200px;
                margin-top: 42px;
                margin-right: 30px;
                box-sizing: border-box;

                &:last-of-type {
                    margin-right: 0;
                }
                dt {
                    margin-bottom: 16px;
                    font-size: 14px;
                    // color: #fff;//换色
                    font-weight: bold;
                }
                dd {
                    position: relative;
                    left: -10px;
                    margin-bottom: 2px;
                    height: 32px;
                    line-height: 32px;
                    font-size: 14px;
                    &.active {
                        background: #0b1431; //换色
                        span {
                            color: $color-orange;
                        }
                        .startImg {
                            display: inline-block;
                        }

                        span,
                        i {
                            &.win-star.active {
                                + span {
                                    color: $color-orange;
                                }
                                &::before {
                                    opacity: 1;
                                    font-size: 12px;
                                    // color: red;
                                }
                            }
                        }
                    }
                    .startImg {
                        display: none;
                        position: absolute;
                        right: 12px;
                        top: 2px;
                    }
                    // background: red;
                    // color: #adb5bb;//换色
                    cursor: pointer;
                    //当没有地址的时候
                    & span.noAddress {
                        // color: #999; //换色
                        // &:hover {
                        //     color: #999;
                        //     & + .win-star {
                        //         &::before {
                        //             opacity: 1;
                        //         }
                        //     }
                        // }
                        & + .startImg {
                            display: none;
                        }
                    }
                    span,
                    i {
                        display: inline-block;
                        vertical-align: middle;
                        &.win-star {
                            position: absolute;
                            right: 12px;
                            top: -1px;
                            &::before {
                                opacity: 0;
                            }
                        }
                        &.win-star.active {
                            + span {
                                color: $color-orange;
                            }
                            &::before {
                                opacity: 1;
                                font-size: 12px;
                                color: $color-orange;
                            }
                        }
                    }
                    &:hover {
                        // .startImg {
                        //     display: inline-block;
                        // }
                        // background: #0b1431; //换色
                        // // span {
                        // //     color: red;
                        // // }
                        // .win-star {
                        //     &::before {
                        //         opacity: 1;
                        //     }
                        // }
                    }
                }
            }
        }
    }

    .close {
        position: absolute;
        top: 8px;
        right: 22px;
        font-size: 16px;
        color: #fff; //换色
        cursor: pointer;
        &:hover {
            color: $color-orange;
        }
    }
    .search {
        width: 80%;
        height: 26px;
        margin-top: 14px;
        // background: red;
        border-bottom: 1px solid #adb5bb;
        .win-el-icon-search {
            display: inline-block;
            vertical-align: middle;
            margin-right: 6px;
            font-size: 24px;
            font-size: 24px;
            margin-top: -7px;
            &::before {
                color: #848993;
            }
        }
        .searchInput {
            display: inline-block;
            width: 70%;
            vertical-align: middle;
            height: 26px;
            line-height: 26px;
            color: #adb5bb;
            border: none;
            margin-top: -10px;
            background: transparent;
        }
    }
    .noSearch {
        position: relative;
        left: -19px;
        top: 8px;
        width: 300px;
        height: 200px;
        line-height: 100px;
        color: #666;
        font-size: 22px;
        text-align: center;
        background: transparent;
        z-index: 1;
        p {
            height: 50x;
            line-height: 50px;
            .win {
                font-size: 40px;
            }
        }
    }
}
</style>
