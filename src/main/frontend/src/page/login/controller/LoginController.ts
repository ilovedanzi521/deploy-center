import Vue from "vue";
import Component from "vue-class-component";
import { LoginVO } from "../vo/loginVO";
import loginService from "../service/LoginService";
import Tool from "../../../mixin/mm";
import Logo from "../view/logo.vue";
import Login from "../view/login.vue";

@Component({ components: { Logo, Login } })
export default class LoginController extends Vue {
    loginVO: LoginVO = new LoginVO();
    $router;
    async login(params) {
        if (this.formIsVerification(params.loginName, params.password)) {
            //表单验证成功;
            const res = await loginService.login(params);
            //登录成功;
            if (res.winRspType == "SUCC") {
                if (this.loginVO.rememberOk) {
                    localStorage.setItem("userName", params.loginName);
                    localStorage.setItem(
                        "list",
                        JSON.stringify(res.data.menuList)
                    );
                } else if (
                    localStorage.getItem("userName") &&
                    !this.loginVO.rememberOk
                ) {
                    localStorage.removeItem("userName");
                }
                localStorage.setItem("Authorization", res.data.token); //将tonk保存到本地
                if (localStorage.getItem("nameLock")) {
                    localStorage.removeItem("nameLock");
                }
                this.loginVO.tipMessage = ""; //将页面跳转到首页
                this.$router.push({
                    path: "/"
                });
            } else {
                this.loginVO.tipMessage = res.msg || "用户名或密码错误";
            }
        }
    }

    /**提示用户不为空* */
    passusenameTip(uesname: string): void {
        if (!Tool.validate(uesname, "require")) {
            this.loginVO.tipMessage = "用户名为空";
        } else {
            this.loginVO.tipMessage = "";
        }
    }

    /**提示密码不为空* */
    passwordTip(password: string): void {
        if (!Tool.validate(password, "require")) {
            this.loginVO.tipMessage = "密码为空";
        } else {
            this.loginVO.tipMessage = "";
        }
    }

    /**判断验证码是否比配* */
    yzmTip(code: string): void {
        this.loginVO.code = code;
        if (
            // this.loginVO.code.toUpperCase() === this.loginVO.yzm.toUpperCase()  不区分大小写
            this.loginVO.code === this.loginVO.yzm //区分大小写
        ) {
            this.loginVO.tipMessage = "";
            this.loginVO.yamisOk = true;
        } else {
            this.loginVO.tipMessage = "验证码错误";
            this.loginVO.yamisOk = false;
        }
    }

    /**切换用户记住用户名状态 */
    changeRemember(rememberisOk: boolean): void {
        this.loginVO.rememberOk = rememberisOk;
    }

    /**将用户名保存在本地 */
    storageUserName(username: string): void {
        localStorage.setItem("uername", username);
    }

    /**用户From表单验证* */
    formIsVerification(userName: string, password: string): boolean {
        if (!Tool.validate(userName, "require")) {
            this.loginVO.tipMessage = "用户名为空"; //判断用户名不能为空
            return false;
        } else if (!Tool.validate(password, "require")) {
            this.loginVO.tipMessage = "密码为空"; //判断密码不能为空
            return false;
            // } else if (!this.loginVO.yamisOk) {
            //     this.loginVO.tipMessage = "验证码错误"; //判断输入的验证码是否一致
            //     return false;
        } else {
            return true;
        }
    }

    /**创建验证码* */
    createCode(): void {
        let code: string = ""; //创建验证码的空字符串
        let codeLength: number = 4; //验证码的长度
        let yzmArray: any = this.loginVO.yzmArray; //随机数
        for (let i = 0; i < codeLength; i++) {
            let charIndex = Math.floor(Math.random() * 62); //取得随机数的索引
            code += yzmArray[charIndex];
        }
        this.loginVO.yzm = code; //把code值赋给验证码
    }

    mounted() {
        //加载组件渲染验证码
        this.createCode();
        // var authorization: string = localStorage.getItem("Authorization");
        // if (authorization && authorization != "") {
        //     this.$router.push({
        //         path: "/"
        //     });
        // }
        // let that = this;
    }
}
