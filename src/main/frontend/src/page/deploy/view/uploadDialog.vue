import { Component } from "vue";

import Component from "vue-class-component";

<template>
    <win-fdialog :title="dialogTitle" class="uploadDialog" :before-close="closeDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false" width="500px" >
        <el-upload class="uploadPanel" ref="upload" 
            :auto-upload="false" :multiple="false" :limit="1"
            :action="uploadUrl" :accept="acceptFileType"
            :before-upload="beforeUpload" :on-success="successUpload" :on-error="errorUpload" >
                <win-button slot="trigger" type="primary">选取文件</win-button>
                <win-button style="margin-left: 10px;" type="success" @click="submitUpload">上传</win-button>
                <div slot="tip" class="el-upload__tip">注：只能上传一个小于 1GB 的 zip 压缩文件</div>
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
    .uploadPanel{
        margin-bottom: 20px;
        height: 180px
    }

</style>