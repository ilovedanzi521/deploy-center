// const files = require.context(".", false, /\.vue$/)
// let configLayoutArray = []
// files.keys().forEach(key => {
//     if (key == "./Layout.vue") return;
//     configLayoutArray = configLayoutArray.concat(files(key).default)
// })
// // let path = require("path")
// // const getContentFile = (isFindChildrenFile, file) => {
// //     const files = require.context("../", isFindChildrenFile, file);
// //     let configLayoutArray = []
// //     // let filesPath = files.keys()
// //     files.keys().forEach(key => {
// //         if (key == "./Layout.vue") return;
// //         configLayoutArray = configLayoutArray.concat(files(key).default)
// //     });
// //     return configLayoutArray;
// // }

// export {
//     configLayoutArray,
//     getContentFile
// }

import Vue from "vue";

function capitalizeFirstLetter(str) {
    return str;
}

function validateFileName(str) {
    return (
        /^\S+\.vue$/.test(str) &&
        str.replace(/^\S+\/(\w+)\.vue$/, (rs, $1) => capitalizeFirstLetter($1))
    );
}

const requireComponent = require.context("./", true, /\.vue$/);
requireComponent.keys().forEach(filePath => {
    const componentConfig = requireComponent(filePath);
    const fileName = validateFileName(filePath);
    const componentName = fileName;
    Vue.component(componentName, componentConfig.default || componentConfig);
});
