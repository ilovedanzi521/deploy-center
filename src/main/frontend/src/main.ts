// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueDND from "awe-dnd";
import ElementUI from "element-ui";
import "xe-utils";
import App from "./App.vue";
import router from "./router/index";
import store from "./store/index";
import { PluginsPage } from "win-biz";
import "win-plus/dist/static/index.css";

Vue.use(VueDND);
Vue.use(ElementUI);
Vue.use(PluginsPage);

Vue.config.productionTip = false;
Vue.config.productionTip = false;
let $env = process.env.NODE_ENV;
Vue.prototype.$env = $env;


//全局锁屏判断
router.beforeEach((to, from, next) => {
    if (localStorage.getItem("lockName")) {
        next(false);
    } else {
        next();
    }
});

/* eslint-disable no-new */
// tslint:disable-next-line: no-unused-expression
new Vue({
    el: "#app",
    router,
    store,
    components: { App },
    template: "<App/>"
});
