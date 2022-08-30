<template>
    <div>
        <iframe :src="viewPath" frameborder="0" class="viewNew" scrolling="no"></iframe>
    </div>
</template>

<script>
export default {
    name: "iframeView",
    data(){
        return {

        }
    },
    created(){
        console.log(this.$route)
    },
    computed:{
        viewPath(){
            return this.$route.params.redirectPath
        }
    }
}
</script>

<style lang="css">
    .viewNew{
        width: 100%;
        height: 100%;
    }
</style>
    