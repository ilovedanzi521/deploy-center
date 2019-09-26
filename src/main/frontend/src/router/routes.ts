import layout from "@/layout/Layout.vue";
import Login from "@/page/login/view/index.vue";
import Home from "@/page/home/view/index.vue";
import Directional from "@/page/fram/view/Directional.vue";
import DeployIndex from "@/page/deploy/view/index.vue";
import DeployDevice from "@/page/deploy/view/device.vue";
import DeployUser from "@/page/deploy/view/userList.vue";


const routers = [
    {
        path: "/",
        name: "layout",
        component: layout,
        children: [
            {
                path: "/directional",
                name: "directional",
                component: Directional
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
    {
        path: "/deployIndex",
        name: "deployIndex",
        component: DeployIndex,
    },
    {
        path: "/deployDevice",
        name: "deployDevice",
        component: DeployDevice,
    },
    {
        path: "/deployUser",
        name: "deployUser",
        component: DeployUser,
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
}
export default routers;
