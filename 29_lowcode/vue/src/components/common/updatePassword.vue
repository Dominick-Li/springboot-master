<template>
    <div class="div_mian">
        <div class="update-password-form">
          
            <el-form  :model="user"  label-width="150px" :rules="rules" ref="userForm"  class="userForm">

                   <el-form-item label="请输入旧密码" prop="oldPassword">
                       <el-input v-model="user.oldPassword"   show-password  placeholder="请输入原密码"></el-input>
                   </el-form-item>
                   <el-form-item label="请输入新密码"  prop="password">
                       <el-input v-model="user.password"   show-password placeholder="请输入新密码"></el-input>
                   </el-form-item>
                   <el-form-item label="确认密码"  prop="password2">
                       <el-input v-model="user.password2"   show-password placeholder="请输入确认密码"></el-input>
                   </el-form-item>
            </el-form>
        </div>
        <div class="dialog-footer">
            <el-button size="mini" class="fr" type="primary" @click="submitForm">保存
            </el-button>
            <el-button size="mini" class="fr" type="default" @click="abort">取消</el-button>
        </div>
    </div>
</template>
<script type="text/javascript">
  export default {
    name: 'saveUser',
    data () {
     var validatePass2 = (rule, value, callback) => {
      if (value !== this.user.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
      return {
        user:{ 
            userId: this.$utils.getSessionStorage("user").id
        },
        loading: false,   //点击识别后,挑战到查询页面
    rules: {
        // 表单验证
        oldPassword: [
          { required: true, message: "旧密码不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: "长度要在6到20个字符", trigger: "blur" }
        ],
        password2: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { validator: validatePass2, trigger: "blur" }
        ]
      }
    }
    }
   ,
    methods: {
      submitForm(){
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          if(this.loading){
            return;
          }
          this.loading=true;
          console.log("success submit!!");
           this.$ajax.post('/api/user/updatePasswordDTO', this.user).then(res => {
          if (res.code == 200) {
            this.$notify({
              title: '提示',
              message: '修改成功,请重新登录!',
              type: 'success',
              duration: 1000,
              customClass: 'el-notification-error'
            })
            this.$bus.emit('logOut')
          } else {
            this.$notify({title: '提示', message: res.msg, type: 'error', duration: 1500})
          }
            this.loading=false;
          })
        } else {
          console.log("error submit!!");
          return false;
        }
      });
      },
     abort () {
        // 把写的提示信心需要换行的地方分成数组 confirmText
        this.$confirm(
          '提示', {
            message: '确认要关闭本页面?',
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then((res) => {
          this.serialId = ''
          this.$bus.emit('closeUpdatePasswordDialog', true)
        })
      }
    }
  }

</script>
<style type="text/css" scoped>
  .user-form{
    padding: 30px 10px 5px 20px
  }
  .div_mian{
    width: 100%;
    height: 100%;
  }
  .update-password-form{
      padding-top:30px;
  }
  .dialog-footer{
    margin-top: 20px;
    height: 40px;
  }
   .dialog-footer button{
     margin-right: 10px;
   }
   .el-input{
     width: 90%;
   }
</style>
