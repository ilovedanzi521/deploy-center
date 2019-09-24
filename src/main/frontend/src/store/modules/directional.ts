// import { getUserOrders, getDetailOrders } from '@/services/order';

const state = {
    menuAddress: "",//切换当前地址
    refreshAddress:"",//需要刷新的地址
    deleteAddress:"",//当个删除的地址
    otherOnlyAddress:"",//删除其他还剩下的地址
    onlyHome:""//删除全部还剩下首页
};

const mutations = {
    setMenuAddress(state: any, payload: any) {
        if (payload.hasOwnProperty("menuAddress")) {
            state.menuAddress = payload.menuAddress;
        }
    },
    // setRefresh(state: any,refresh:any){
    //      state.refresh=refresh
    // },
    setRefreshAddress(state:any,refreshAddress){
        state.refreshAddress=refreshAddress
    },
    setDeleteAddress(state:any,deleteAddress){
        state.deleteAddress=deleteAddress
    },
    setOtherOnlyAddress(state:any,otherOnlyAddress){
        state.otherOnlyAddress=otherOnlyAddress
    },
    setOnlyHome(state:any,onlyHome){
        state.onlyHome=onlyHome
    }
};

const actions = {};

export default {
    state,
    mutations,
    actions
};
