<template>
  <div class="div_mian">
    <div class="user-form">
      <el-form
        :model="meta"
        label-width="150px"
        :rules="rules"
        ref="dataForm"
        class="dataForm"
      >
        <el-form-item label="模型名称" prop="metaName">
          <el-input v-model="meta.metaName"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="16">
            <el-form-item label="数据库表名称"  prop="tableCode">
              <el-input :disabled="meta.id != null"  v-model="meta.tableCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              v-show="meta.id == undefined"
              label="主键是否自增"
              prop="increment"
            >
              <el-checkbox v-model="meta.increment"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述">
          <el-input v-model="meta.metaDescription"></el-input>
        </el-form-item>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span><strong>字段列表</strong>(可拖拽行对字段排序)</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="addCloumn"
              >添加字段</el-button
            >
          </div>
          <vuedraggable class="wrapper" v-model="meta.metaColumnList">
            <transition-group>
              <div
                class="text item"
                v-for="(metaColumn, index) in meta.metaColumnList"
                :key="metaColumn.id"
              >
                <el-row>
                    <el-col :span="1">
                      {{ index+1 }}
                    </el-col>
                  <el-col :span="6">
                    <el-form-item label="字段名称:">
                      <el-input
                        v-model="metaColumn.columnName"
                        :ref="'input' + index"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="5">
                    <el-form-item label="数据库字段名称:">
                      <el-input v-model="metaColumn.columnCode"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-form-item label="字段类型:">
                      <el-select
                        v-model="metaColumn.dataType"
                        :disabled="metaColumn.id != ''"
                      >
                        <el-option
                          v-for="item in dataTypeList"
                          :key="item.value"
                          :label="item.name"
                          :value="item.value"
                        >
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                          <el-col :span="3">
                    <el-form-item label="是否显示:">
                      <el-checkbox v-model="metaColumn.viewShow"></el-checkbox>
                    </el-form-item>
                  </el-col>
                  <el-col :span="3">
                    <el-form-item label="是否查询条件:">
                      <el-checkbox v-model="metaColumn.search"></el-checkbox>
                    </el-form-item>
                  </el-col>
                  <el-col :span="2">
                    <!-- 删除小图标 -->
                    <i
                      @click="deleteItem(index)"
                      class="el-icon-remove-outline dingwei"
                    ></i>
                  </el-col>
                </el-row>
              </div>
            </transition-group>
          </vuedraggable>
        </el-card>
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
import vuedraggable from "vuedraggable";
export default {
  name: "saveMeta",
  props: ["refdata", "dataTypeList"],
  data() {
    return {
      meta: {},
      loading: false, //点击识别后,挑战到查询页面
      rules: {
        // 表单验证
        metaName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" },
        ],
        tableCode: [
          { required: true, message: "数据库表名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  components: {
    vuedraggable,
  },
  created() {
    console.log(this.refdata);
    this.meta = JSON.parse(JSON.stringify(this.refdata));
  },
  mounted() {},
  methods: {
    addCloumn() {
      let column = {
        id:"",
        columnName: "",
        columnCode: "",
        dataType: "varchar(255)",
        viewShow: true,
        search: true
      };
      this.meta.metaColumnList.push(column);
    },
    deleteItem(index) {
      this.meta.metaColumnList.splice(index, 1);
    },
    submitForm() {
      if (!this.meta.metaName) {
        this.$message.warning("请输入模型名称!");
        return;
      }
      if (!this.meta.tableCode) {
        this.$message.warning("请输入数据库表名称!");
        return;
      }
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          if (this.loading) {
            return;
          }
          this.loading = true;
          let isAdd=this.meta.id == null ;
          let message = isAdd ? "添加成功" : "修改成功";
          this.$ajax.post("/api/meta/", this.meta).then((res) => {
            if (res.code == 200) {
              this.$bus.emit('refreshMenus')
              this.$notify({
                title: "提示",
                message: message,
                type: "success",
                duration: 1000,
                customClass: "el-notification-error",
              });
              this.$bus.emit("refreshMetaList");
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
          this.$bus.emit("closeMetaSaveDialog", true);
        })
        .catch((err) => {});
    },
  },
};
</script>
<style scoped>
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
.user-form {
  padding: 44px 20px;
}
</style>
