<template>
    <div class="login-container">
        <el-form ref="form" class="login-form" label-width="80px" lable autocomplete="on">
            <div class="title-container">
                <h3 class="title2">欢迎登录低代码平台系统</h3>
            </div>
            <el-form-item label="用户名" >
                <el-input v-model="username" tabindex="1"></el-input>
            </el-form-item>
            <el-tooltip v-model="capsTooltip" content="大写" placement="right" manual>
                <el-form-item label="密码">
                    <el-input v-model="password" tabindex="2" @keyup.native="checkCapslock" show-password></el-input>
                </el-form-item>
            </el-tooltip>
            <div class="btnsure">
                <el-button class="subBtn" type="primary" tabindex="3" @click="onSubmit">登录</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
import Vue from 'vue'
  export default {
    data () {
      return {
        capsTooltip: false,
        username: '',
        password: ''
      }
    },
    mounted(){
        this.changeFavicon()
    },
    methods: {
        changeFavicon(){
            let $favicon = document.querySelector("link[rel*='Icon']");
            if ($favicon !== null) {
                $favicon.href = '';
                document.head.removeChild($favicon)
            }
        },
      checkCapslock ({shiftKey, key} = {}) {
        if (key && key.length === 1) {
          if (shiftKey && (key >= 'a' && key <= 'z') || !shiftKey && (key >= 'A' && key <= 'Z')) {
            this.capsTooltip = true
          } else {
            this.capsTooltip = false
          }
        }
        if (key === 'CapsLock' && this.capsTooltip === true) {
          this.capsTooltip = false
        }
        if (key == 'Enter') {
            this.onSubmit()
        }
      },
      onSubmit () {
        this.$ajax.post('/api/user/login', {
          username: this.username,
          password: this.password
        }).then((res) => {
          if (res.code == 200) {
            this.$utils.setSessionStorage('token', res.data.token)
            this.$utils.setSessionStorage('user', res.data.user)
            this.$utils.setSessionStorage('roleMenus', res.data.menus)
            this.$utils.setSessionStorage('roleCode', res.data.roleCode)
            let msg="登录成功";
            this.$notify({title: '提示', message: msg, type: 'success', duration: 2000})
            this.$router.push({path: '/index'})
          } else {
            this.$notify({title: '提示', message: res.msg, type: 'error', duration: 1500})
          }
        }).catch((e) => {
          this.$notify({title: '提示', message: '连接服务器异常!', type: 'error', duration: 1500})
        })
      }
    }
  }
</script>


<style scoped>
    h3 {
        margin: 40px 0 0;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }

    @supports (-webkit-mask: none) and (not (cater-color: #fff)) {
        .login-container .el-input input {
            color: #fff;
        }
    }

    .el-input {
        display: inline-block;
        height: 40px;
        width: 100%;
    }

    .el-form-item {
        border-radius: 5px;
        color: white;
    }

    input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: #fff;
        height: 47px;
        caret-color: #fff;
    }

</style>

<style scoped>
    .login-container {
        min-height: 100%;
        width: 100%;
        background:-webkit-linear-gradient(top,white,lightblue,skyblue);
        overflow: hidden;
    }

    .login-form {
        width: 580px;
        max-width: 100%;
        overflow: hidden;
        transform: translate(-50%,-50%);
        position: fixed;
        left: 50%;
        top: 50%;
        margin-top: 10px;
    }

    .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 10px;
    }

    .first-of-type {
        margin-right: 16px;
    }

    .title-container {
        position: relative;
    }

    .title1{
        font-size: 28px;
        font-weight: 400;
        color: #eee;
        margin: 0px auto 30px 10%;
        text-align: center;
    }

    .title2{
        font-size: 26px;
        color: black;
        margin: 0px auto 30px 10%;
        text-align: center;
        font-weight: bold;
    }
    .cppyright{
        position: relative;
        margin: 40px auto 30px auto;
        text-align: center;
        color: #eee;
        font-size: 14px;
        line-height: 30px;
    }
    .show-pwd {
        position: absolute;
        right: 10px;
        top: 7px;
        font-size: 16px;
        color: #889aa4;
        cursor: pointer;
        user-select: none;
    }

    .thirdparty-button {
        position: absolute;
        right: 0;
        bottom: 6px;
    }

    @media only screen and (max-width: 470px) {
        .thirdparty-button {
            display: none;
        }
    }

    @media only screen and (max-width: 550px) {
        .login-form {
            width: 80%;
            max-width: 100%;
            overflow: hidden;
            transform: translate(-50%,-50%);
            position: absolute;
            left: 50%;
            top: 50%;
            margin-top: 30px;
        }
    }

    #logoImg{
        position: absolute;
        left:20%;
        top:10%;
        width: 151px;
    }
    .login-form >>>.el-form-item__label{
        width: 80px!important;
    }
    .login-form >>>.el-form-item__content{
        margin-left: 80px!important;
    }
    .btnsure{
        padding-left: 80px;
    }
    .subBtn{
        width: 100%;
        margin-bottom:30px;
    }
</style>
<style >
   .login-container .el-form-item__label{
        color: black;
        font-weight: bold;
    }
</style>

