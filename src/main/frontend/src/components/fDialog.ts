/* @Dialog继承组件
 * @Author: mikey.zhaopeng
 * @Date: 2019-06-05 14:01:33
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2019-09-02 09:26:26
 */
import "./style/fdialog.scss";
import { Dialog } from "element-ui";
// export const WinFdialog = Dialog;
export const WinFdialog = {
    extends: Dialog,
    mounted() {
        this.drag();
    },
    watch: {
        visible: {
            handler(newState) {
                if (newState) {
                    this.drag();
                }
            }
        }
    },
    props: {
        noDrag: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        drag() {
            if (this.noDrag) {
                return;
            }
            let el = document.querySelector(".el-dialog__wrapper");
            let time;
            if (time) {
                clearTimeout(time);
            }
            time = setTimeout(() => {
                const dragDom = el.querySelector(".el-dialog");
                const dialogHeaderEl = dragDom.querySelector(
                    ".el-dialog__header"
                );
                const dialogBodyEl = dragDom.querySelector(".el-dialog__body");
                const dialogFootEl = dragDom.querySelector(
                    ".el-dialog__footer"
                );
                let screeWidth = document.body.clientWidth;
                let screeHeght = document.body.clientHeight - 100;
                let dialogHeight;
                if (dialogBodyEl) {
                    dialogHeight =
                        dialogBodyEl.clientHeight +
                        dialogHeaderEl.clientHeight +
                        dialogFootEl.clientHeight;
                }

                let dialoWidth = dragDom.clientWidth;

                let diffWidth = (screeWidth - dialoWidth) / 2;
                let diffHeight = Math.floor((screeHeght - dialogHeight) / 2);
                dialogHeaderEl.style.cursor = "move";
                const sty =
                    dragDom.currentStyle ||
                    window.getComputedStyle(dragDom, null);
                dialogHeaderEl.onmousedown = e => {
                    // 鼠标按下，计算当前元素距离可视区的距离
                    const disX = e.clientX - dialogHeaderEl.offsetLeft;
                    const disY = e.clientY - dialogHeaderEl.offsetTop;

                    // 获取到的值带px 正则匹配替换
                    let styL, styT;
                    if (sty.left.includes("%")) {
                        styL =
                            +document.body.clientWidth *
                            (+sty.left.replace(/\%/g, "") / 100);
                        styT =
                            +document.body.clientHeight *
                            (+sty.top.replace(/\%/g, "") / 100);
                    } else {
                        styL = +sty.left.replace(/\px/g, "");
                        styT = +sty.top.replace(/\px/g, "");
                    }
                    document.onmousemove = function(e) {
                        // 通过事件委托，计算移动的距离
                        const l = e.clientX - disX;
                        const t = e.clientY - disY;
                        // 移动当前元素
                        let left = l + styL;
                        let top = t + styT;
                        if (left < -diffWidth + 20) {
                            left = -diffWidth + 20;
                        }
                        if (left > diffWidth - 20) {
                            left = diffWidth - 20;
                        }

                        if (top > diffHeight - 20) {
                            top = diffHeight - 20;
                        }

                        if (top < -diffHeight + 20) {
                            top = -diffHeight + 20;
                        }
                        dragDom.style.transform = `translate3d(${left}px,${top}px,0)`;

                        //将此时的位置传出去
                        //binding.value({x:e.pageX,y:e.pageY})
                    };

                    document.onmouseup = function(e) {
                        document.onmousemove = null;
                        document.onmouseup = null;
                    };
                };
            }, 300);
        }
    }
};
