<template>
    <win-fdialog :title="dialogTitle" class="taskDialog" :before-close="closeDialog" :visible.sync="dialogVisibleSon" :close-on-click-modal="false" :close-on-press-escape="false" width="500px" >
        
    </win-fdialog>

</template> 

<script lang="ts">
    import Component from "vue-class-component";
    import TaskDialogController from "../controller/TaskDialogController";

    @Component
    export default class TaskDialog extends TaskDialogController{}
</script>

<style lang="scss" scoped>
   

</style>