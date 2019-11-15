import {Layout} from "win-biz";
import Directional from "@/page/fram/view/Directional.vue";
import asyncChunks from "../async-chunks";

import DeviceManagement from "@/page/deploy/view/device.vue";

const routers = [
	{
        path: "/",
        name: "layout",
        component: () => asyncChunks.winBiz("Layout"),
        children: [
            {
                path: "/directional",
                name: "directional",
                component: () => asyncChunks.winBiz("Directional")
            }
        ]
    }
];



const routerChildren = [
    {
        path: "/deploy/index",
        name: "deployIndex",
        component: asyncChunks.DeployIndex
    },
    {
        path: "/deploy/group",
        name: "deployGroup",
        component: asyncChunks.DeployGroup
    },
    {
        path: "/deploy/device",
        name: "deployDevice",
        component: asyncChunks.DeployDevice
    },
    {
        path: "/deploy/appmodule",
        name: "deployAppModule",
        component: asyncChunks.DeployAppModule
    },
    {
        path: "/deploy/strategy",
        name: "strategyManagement",
        component: asyncChunks.StrategyManagement
    },
    {
        path: "/deploy/task",
        name: "deployTask",
        component: asyncChunks.DeployTask
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
