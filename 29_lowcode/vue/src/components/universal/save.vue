<template>
  <div class="div_mian">
    <div class="user-form">
      <el-form label-width="150px" class="dataForm">
        <el-form-item
          v-for="metaColumn in metaColumnList"
          :key="metaColumn.code"
          :label="metaColumn.name"
        >
          <el-input
            v-if="metaColumn.dataType.includes('int')"
            type="number"
            v-show="metaColumn.code!='id'"
            v-model="metaColumn.value"
          ></el-input>
          <el-date-picker
            v-else-if="metaColumn.dataType.includes('date')"
            v-model="metaColumn.value"
            type="date"
            placeholder="选择日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
          <el-input v-else v-model="metaColumn.value"></el-input>
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
  name: "saveData",
  props: ["refMetaColumnList", "metaId"],
  data() {
    return {
      loading: false,
      metaColumnList:[]
    };
  },
  created() {
    this.metaColumnList = JSON.parse(JSON.stringify(this.refMetaColumnList));
  },
  mounted() {},
  methods: {
    submitForm() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      this.$ajax
        .post("/api/universal/", {
          data: this.metaColumnList,
          metaId: this.metaId,
        })
        .then((res) => {
          if (res.code == 200) {
            this.$notify({
              title: "提示",
              message: "保存成功",
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
