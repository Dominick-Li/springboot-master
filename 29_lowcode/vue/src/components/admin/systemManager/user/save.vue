<template>
  <div class="div_mian">
    <div class="user-form">
      <el-form
        :model="user"
        label-width="100px"
        :rules="rules"
        ref="userForm"
        class="userForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="user.username"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="user.id == null" prop="password">
          <el-input v-model="user.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="user.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
       
        <el-form-item label="角色">
          <el-select v-model="user.roleId" :value="user.roleName">
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="渠道">
          <el-tree
            :data="channelList"
            class="select-contain"
            ref="treeDom"
            show-checkbox
            :render-after-expand="true"
            @check="handleNodeClick"
            accordion
            check-strictly
            :props="defaultProps"
            :default-checked-keys="checkIdList"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
          >
            <span
              class="custom-tree-node"
              slot-scope="{ node, data }"
              :rowdata="JSON.stringify(data)"
            >
              <span :class="{ rightAdd: data.id ? false : true }">{{
                node.label
              }}</span>
            </span>
          </el-tree>
        </el-form-item>
      </el-form>
    </div>
    <div class="dialog-footer">
      <el-button size="mini" class="fr" type="primary" @click="submitForm"
        >保存
      </el-button>
      <el-button size="mini" class="fr" type="default" @click="abort"
        >取消</el-button
      >
    </div>
  </div>
</template>
<script type="text/javascript">
export default {
  name: "saveUser",
  props: ["refuser"],
  data() {
    return {
      user: {},
      channelList: [],
      checkIdList: [1],
      roleOptions: [],
      isAdd: true,
      loading: false, //点击识别后,挑战到查询页面
      defaultProps: {
        children: "children",
        label: "channelName",
      },
      rules: {
        // 表单验证
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 6, max: 30, message: "长度要在6到20个字符", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.user = this.refuser;
    if(this.user.id){
      this.isAdd=false;
    }
  },
  mounted() {
    this.$ajax.get("/api/channel/findAll").then((res) => {
      if (res.code == 200) {
        if (this.isAdd) {
          this.checkIdList[0] = res.data[0].id;
        } else {
          this.checkIdList[0] = this.user.channelId;
        }
        this.channelList = res.data;
      }
    });
    this.$ajax.get("/api/role/getSelectList").then((res) => {
      if (res.code == 200) {
        this.roleOptions = res.data;
        if (this.isAdd) {
          let that = this;
          this.$nextTick(() => {
            this.user.roleId = res.data[0].value;
            this.checkRoleName = res.data[0].name;
          });
        }
      }
    });
  },
  methods: {
    handleNodeClick(data) {
      let labvalojb = data; //暂存选中节点
      this.user.channelId = data.id;
      this.$refs.treeDom.setCheckedKeys([]);
      this.$refs.treeDom.setCheckedNodes([labvalojb]);
    },
    submitForm() {
      this.$refs["userForm"].validate((valid) => {
        if (valid) {
          if (this.loading) {
            return;
          }
          if(!this.user.channelId){
          this.user.channelId = this.checkIdList[0];
          }
          this.loading = true;
          let message = this.user.id == null ? "添加成功" : "修改成功";
          console.log("success submit!");
          this.$ajax.post("/api/user/save", this.user).then((res) => {
            if (res.code == 200) {
              this.$notify({
                title: "提示",
                message: message,
                type: "success",
                duration: 1000,
                customClass: "el-notification-error",
              });
              this.$bus.emit("refreshList", true);
            } else {
              this.$notify({
                title: "提示",
                message: res.msg,
                type: "error",
                duration: 1500,
              });
            }
            this.loading = false;
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    abort() {
      // 把写的提示信心需要换行的地方分成数组 confirmText
      this.$confirm("提示", {
        message: "确认要关闭本页面?",
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then((res) => {
          this.serialId = "";
          this.$bus.emit("closeSaveDialog", true);
        })
        .catch((err) => {});
    },
  },
  watch: {
    refuser(newVal) {
      console.log("newVal" + newVal);
      this.user = newVal;
      this.isAdd = this.user.id == undefined;
      if (this.isAdd) {
        this.user.roleName = this.roleOptions[0].name;
        this.user.roleId = this.roleOptions[0].value;
        this.checkIdList[0] = this.channelList[0].id;
      } else {
        this.checkIdList[0] = this.user.channelId;
      }
      this.$refs.treeDom.setCheckedKeys(this.checkIdList,true);
    },
  },
};
</script>
<style type="text/css" scoped>
.user-form {
  padding: 30px 10px 5px 20px;
}
.div_mian {
  width: 100%;
  height: 100%;
}
.dialog-footer {
  margin-top: 20px;
  height: 40px;
}
.dialog-footer button {
  margin-right: 10px;
}
.el-input {
  width: 90%;
}
</style>
