import { Component } from "vue";

import Component from "vue-class-component";

<template>
    <win-fdialog :title="dialogTitle" class="uploadDialog" :before-close="closeDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false" width="500px" >
        <el-upload
            style="margin-bottom: 20px;height: 180px"
            ref="upload"
            action=""
            accept="zip"
            :auto-upload="false">
                <win-button slot="trigger" size="small" type="primary">选取文件</win-button>
                <win-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</win-button>
                <div slot="tip" class="el-upload__tip">只能上传zip压缩文件</div>
        </el-upload>
    </win-fdialog>

</template> 

<script lang="ts">
    import Component from "vue-class-component";
    import UploadDialogController from "../controller/UploadDialogController";

    @Component
    export default class UploadDialog extends UploadDialogController{}
</script>

<style lang="scss" scoped>

</style>