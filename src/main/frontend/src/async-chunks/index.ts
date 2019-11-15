/**
 * chunks: async
 */

// 异步模块
export const winBiz = (content?: string) =>
    import(/* webpackChunkName: "win-biz" */ "win-biz").then(moudle => {
        return !content ? moudle : moudle[content];
    });

// 异步组件
export const DeployIndex=()=>import("@/page/deploy/view/index.vue");
export const DeployGroup=()=>import("@/page/deploy/view/group.vue");
export const DeployDevice=()=>import("@/page/deploy/view/device.vue");
export const DeployAppModule=()=>import("@/page/deploy/view/appModule.vue");
export const DeployTask=()=>import("@/page/deploy/view/task.vue");
export const StrategyManagement=()=>import("@/page/deploy/view/strategyManagement.vue");



export default {
    winBiz,
    DeployIndex,
    DeployGroup,
    DeployDevice,
    DeployAppModule,
    DeployTask,
    StrategyManagement
};
