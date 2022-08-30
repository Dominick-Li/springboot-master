<template>
    <div class="div_mian">
        <div class="user-form">
          
            <el-form  :model="role"  label-width="100px" :rules="rules" ref="dataForm"  class="dataForm">
                    <el-form-item label="角色名称" prop="roleName">
                        <el-input v-model="role.roleName"   placeholder="请输入角色名称"></el-input>
                    </el-form-item>
                    <el-form-item label="角色编码" prop="roleCode">
                        <el-input v-model="role.roleCode"   placeholder="请输入角色编码"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单">
                      <ul class="menuTree" ref="menuTree">
                        <li v-for="(item) in menuData" :key="item.id">
                          <el-checkbox v-model="item.check" @change="fcheckChange(item)">{{item.title}}</el-checkbox>
                           <ul class="menuTree-child" v-if="item.children&&item.children.length>0">
                            <li v-for="(it) in item.children" :key="it.id">
                              <el-checkbox v-model="it.check" @change="cCheckChange(it,item)">{{it.title}}</el-checkbox>
                            </li>
                          </ul>
                        </li>
                      </ul>
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
    name: 'saveRole',
    props: ['refdata'],
    data () {
      return {
        role:{ },
        loading: false,   //点击识别后,挑战到查询页面
        rules: {
            // 表单验证
            roleName: [
              { required: true, message: "角色名称不能为空", trigger: "blur" },
            ],
            roleCode: [
              { required: true, message: "角色编码不能为空", trigger: "blur" },
            ]
        },
        menuData:[],
        menuIds:[],
      }
    },
    created(){
      console.log(this.refdata)
      this.role = this.refdata
      if(JSON.stringify(this.refdata)!="{}"){
        this.refdata.menuIds.forEach(item=>{
          this.menuIds.push(item)
        })
      }
      this.getAllMenus()
    },
    mounted(){

    },
    methods: {
      fcheckChange(item){
        console.log(item)
        if(item.children.length>0){
          if(item.check){
            item.children.forEach(it => {
              it.check=true
            });
          }else{
            item.children.forEach(it => {
              it.check=false
            });
          }
        }
      },
      cCheckChange(it,item){
        console.log(it,item)
        if(it.check){
          item.check=true
        }else{
          var chilsChecks=item.children.filter(itemchild=>{
            return itemchild.check
          })
          if(chilsChecks.length==0){
            item.check=false
          }else{
            item.check=true
          }
        }
      },
      getAllMenus(row,length){
        this.$ajax.get("/api/menu/findAll").then(res=>{
          if(res.code==200){
            if(JSON.stringify(this.refdata)!="{}"){
              res.data.forEach(item=>{
                if(this.menuIds.indexOf(item.id)!=-1){
                  item.check=true
                }else{
                  item.check=false
                }
                item.children.forEach(it=>{
                  if(this.menuIds.indexOf(it.id)!=-1){
                    it.check=true
                  }else{
                    it.check=false
                  }
                })
              })
            }else{
              res.data.forEach(item=>{
                item.check=false
                item.children.forEach(it=>{
                  it.check=false
                })
              })
            }
            this.menuData=res.data
          }
        })
      },
      submitForm(){
        if(!this.role.roleName){
          this.$message.warning("请输入角色名称")
          return
        }
        if(!this.role.roleCode){
          this.$message.warning("请输入角色编码")
          return
        }
        
        var checkedKeys=[]
        this.menuData.forEach(item=>{
          if(item.check){
            checkedKeys.push(item.id)
          }
          if(item.children&&item.children.length>0){
            item.children.forEach(it => {
              if(it.check){
                checkedKeys.push(it.id)
              }
            });
          }
        })
        if(checkedKeys.length==0){
          this.$message.warning("请选择角色使用菜单")
          return
        }
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if(this.loading){
            return;
          }
          this.loading=true;
          let message=this.role.id==null?'添加成功':'修改成功';
          this.role.menuIds=checkedKeys
          console.log(this.role)
          this.$ajax.post('/api/role/', this.role).then(res => {
          if (res.code == 200) {
            this.$notify({
              title: '提示',
              message: message,
              type: 'success',
              duration: 1000,
              customClass: 'el-notification-error'
            })
            this.$bus.emit('refreshRoleList')
            if(this.$utils.getSessionStorage('roleCode')==this.refdata.roleCode){
              this.$utils.setSessionStorage('roleMenus', res.data)
              this.$bus.emit('refreshMenus')
            }
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
          this.$bus.emit('closeRoleSaveDialog', true)
        }).catch((err) => {

        })
      }
    }
  }

</script>
<style scoped>
  .div_mian{
    width: 100%;
    height: 100%;
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
   .el-tree.menuTree {
    width: 90%;
    margin: 0 auto;
}
/* .el-tree.menuTree /deep/>.el-tree-node>.el-tree-node__content {
    background: #eee;
} */
.menuTree >>>.el-tree-node__children{
  display: flex;
}

.menuTree >>>.el-tree-node__content{
  background: #fff;
}
.menuTree >>>.el-tree-node__content:hover{
  background: #fff;
}
.menuTree >>>.el-tree-node__content>.el-tree-node__expand-icon{
  display: none;
}
.user-form {
    padding: 44px 20px;
}
ul.menuTree {
    text-align: left;
    width: 90%;
    margin-left: 5%;
}
.menuTree>li {
    line-height: 27px;
}
ul.menuTree-child {
    display: flex;
    margin-left: 24px;
}
.menuTree-child>li {
    margin-right: 18px;
}
.menuTree >>>.el-checkbox__input.is-checked+.el-checkbox__label{
  color: #606266;
}
</style>
