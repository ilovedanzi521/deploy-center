import Vue from "vue";
import Component from "vue-class-component";
import { HomeVO } from "../vo/homeVO";
import loginService from "../service/homeService";
import Tool from "../../../mixin/mm";
import Logo from "../view/logo.vue";
import Login from "../view/login.vue";

@Component({ components: { Logo, Login } })
export default class LoginController extends Vue {
    homeVO: HomeVO = new HomeVO();
    $router;

    async getFirstMenu() {
        let result = await loginService.getFirstMenu({
            username: this.homeVO.username,
            tonk: this.homeVO.tonk
        });
        this.homeVO.firstMenuArray;
    }

    getFastMenu() {}

    mounted() {
        this.$nextTick(() => {
            //判断前端是否用户已经登录
            if (this.homeVO.username && this.homeVO.tonk) {
                this.getFirstMenu();
                this.getFastMenu();
            } else {
                this.$router.push({
                    path: "/login"
                });
            }
        });
    }
}
