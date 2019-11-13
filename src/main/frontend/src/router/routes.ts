import {Layout} from "win-biz";
import Login from "@/page/login/view/index.vue";
import Home from "@/page/home/view/index.vue";
import Directional from "@/page/fram/view/Directional.vue";
import DeployIndex from "@/page/deploy/view/index.vue";
import DeployGroup from "@/page/deploy/view/group.vue";
import DeployAppModule from "@/page/deploy/view/appModule.vue";
import DeployTask from "@/page/deploy/view/task.vue";
import StrategyManagement from "@/page/deploy/view/strategyManagement.vue";
import DeviceManagement from "@/page/deploy/view/device.vue";

const routers = [
    {
        path: "/",
        name: "layout",
        component: Layout,
        children: [
            {
                path: "/directional",
                name: "directional",
                component: Directional
            },
            {
                path: "/deploy/index",
                name: "deployIndex",
                component: DeployIndex,
            },
            {
                path: "/deploy/group",
                name: "deployGroup",
                component: DeployGroup,
            },
            {
                path: "/deploy/appmodule",
                name: "deployAppModule",
                component: DeployAppModule,
            },
            {
                path: "/deploy/task",
                name: "deployTask",
                component: DeployTask,
            },
            {
                path: "/deploy/strategy",
                name: "strategyManagement",
                component: StrategyManagement,
            },
            {
                path: "/deploy/device",
                name: "deviceManagement",
                component: DeviceManagement,
            },
        ]
    },
    {
        path: "/login",
        name: "login",
        component: Login
    },
];

const routerChildren = [
    {
        path: "/home",
        name: "home",
        component: Home
    },
];

if ("development" === process.env.NODE_ENV) {
    routerChildren.forEach(item => {
        routers[0].children.push(item);
    });
    localStorage.setItem("Authorization", "development");
} else {
    routerChildren.forEach(item => {
        if (item.children) {
            routers.push({ ...item, children: item.children });
        } else {
            routers.push({ ...item, children: [] });
        }
    });
    localStorage.setItem("Authorization", "development");
}
export default routers;
