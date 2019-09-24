const state = {
    standardFareMarketInfo: {
        marketCode: ""
    }
};

const mutations = {
    // tslint:disable-next-line: no-shadowed-variable
    setStandardFareMarketInfo(state, payload) {
        state.standardFareMarketInfo = {
            marketCode: ""
        };
        if (payload.hasOwnProperty("marketCode")) {
            state.standardFareMarketInfo.marketCode = payload.marketCode;
        }
    }
};

const actions = {};
export default {
    state,
    mutations,
    actions
};
