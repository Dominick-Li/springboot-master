<template>
  <div>
    <el-container v-if="refreshView">
      <el-main>
        <div>
          <el-form label-width="80px">
            <el-row>
              <el-col :span="6">
                <el-button
                  class="btnRight"
                  type="primary"
                  size="small"
                  @click="createUser('add')"
                  >添加用户
                </el-button>
                <el-button
                  class="btnRight"
                  type="warning"
                  size="small"
                  @click="deleteSelected"
                  >批量删除
                </el-button>
              </el-col>
              <el-col :span="4">
                <el-form-item label="帐号" class="names">
                  <el-input v-model="condition.like.username"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" style="padding-left: 20px">
                <el-button type="primary" size="small" @click="getUserList"
                  >查询</el-button
                >
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="table-container">
          <div class="table-list">
            <el-table
              class="tableLimit"
              :data="tabelData"
              style="width: 100%;height: 100%"
              @selection-change="selectAll"
              :row-class-name="tableRowClassName"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="username" label="帐号"></el-table-column>
              <el-table-column
                prop="name"
                label="用户姓名"
                min-width="60"
              ></el-table-column>
              <el-table-column prop="channelName" label="机构" min-width="60">
              </el-table-column>
              <el-table-column prop="roleName" label="角色" min-width="60">
              </el-table-column>
              <el-table-column label="帐号状态" min-width="50">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.enabled"
                     :active-value="1" :inactive-value="0"
                    @change="userEnabled(scope.row.id, scope.row.enabled)"
                  >
                  </el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="120">
                <template slot-scope="scope">
                  <el-button
                    @click="createUser(scope.row)"
                    type="text"
                    size="small"
                    >修改
                  </el-button>
                  <el-button
                    @click="restPassword(scope.row)"
                    type="text"
                    size="small"
                    >重置密码
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div class="table-pagination">
          <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[10, 15, 20, 30, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </div>
      </el-main>
    </el-container>
    <el-dialog
      :title="saveTitle"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="40%"
      height="80%"
    >
      <save :refuser="user"> </save>
    </el-dialog>

  </div>
</template>

<script type="text/javascript">
import save from "./save";
export default {
  name: "ViewOCR",
  data() {
    return {
      user: {},
      condition: {
        like: {
          username: "",
          company: "",
        },
        eq:{
          channelId: ''
        },
        currentPage: 1,
        pageSize: 10,
      },
      saveTitle: "新增用户",
      timer: "",
      isAllChecked: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tabelData: [],
      selectedIds: [],
      dialogTableVisible: false,
      loading: false,
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      value1: "",
      refreshView:true,
    };
  },
  components: {
    save,
  },
  watch:{
    condition:{
      handler(val){
        this.$store.commit("setEachCondition",{
          name:this.$route.name,
          condition:val
        })
      },
      deep: true
    }
  },
  created(){
    var thisCondition=this.$store.state.eachCondition[this.$route.name]
    if(thisCondition){
      this.condition=thisCondition
      this.pageNum= this.condition.currentPage
      this.pageSize= this.condition.pageSize
      this.refreshView=false
      this.$nextTick(()=>{
        this.refreshView=true
      })
    }
  },
  mounted() {
    this.sceneId = this.$route.params.sceneId || "1";
    this.$nextTick(() => {
      this.getUserList();
    });
    this.$bus.on("closeSaveDialog", () => {
      this.dialogTableVisible = false;
    });
    this.$bus.on("refreshList", () => {
      this.dialogTableVisible = false;
      this.getUserList();
    });
  },
  beforeDestroy() {
    this.$bus.off("closeSaveDialog");
    this.$bus.off("refreshList");
  },
  methods: {
    restPassword(row) {
      this.$ajax.get("/api/user/restPassword/" + row.id).then((res) => {
        if (res.code == 200) {
          this.$notify({
            title: "提示",
            message: res.msg,
            type: "success",
            duration: 1000,
            customClass: "el-notification-error",
          });
        } else {
          this.$notify({
            title: "提示",
            message: res.msg,
            type: "error",
            duration: 1500,
          });
        }
      });
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 50;
      if (!isLt2M) {
        this.$notify({
          title: "提示",
          message: "上传文件的大小不能超过50MB!",
          type: "error",
          duration: 2000,
        });
      }
      return isLt2M;
    },
    submit() {
      this.$refs.upload.submit();
    },
    userEnabled(id, statu) {
      let message = statu ? "撤销禁用" : "禁用";
      this.$ajax
        .post("/api/user/userEnabled", { id: id, enabled: statu })
        .then((res) => {
          if (res.code == 200) {
            this.$notify({
              title: "提示",
              message: message,
              type: "success",
              duration: 1000,
              customClass: "el-notification-error",
            });
            this.getUserList();
          } else {
            this.$notify({
              title: "提示",
              message: res.msg,
              type: "error",
              duration: 1500,
            });
          }
        });
    },
    selectAll(sels) {
      this.selectedIds = sels;
    },
    deleteSelected() {
      this.loading = true;
      var ids = this.selectedIds.map((item) => item.id);
      this.$ajax.post("/api/user/deleteByIds", ids).then((res) => {
        if (res.code == 200) {
          this.$notify({
            title: "提示",
            message: "删除成功",
            type: "success",
            duration: 1500,
            customClass: "el-notification-error",
          });
          this.loading = false;
          this.getUserList();
        } else {
          this.$notify({
            title: "提示",
            message: res.msg,
            type: "error",
            duration: 1500,
          });
          this.loading = false;
        }
      });
    },
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "warning-row";
      } else {
        return "success-row";
      }
      return "";
    },
    createUser(row) {
      let isAdd = row === "add";
      if (!isAdd) {
        this.user = row;
        this.user.roleId = this.user.roleId + "";
      } else {
        this.user = { password: "123456" };
      }
      this.saveTitle = isAdd ? "新增用户" : "修改用户";
      this.$nextTick(() => {
        this.dialogTableVisible = true;
      });
    },
    getUserList() {
      this.loading = true;
      this.$ajax
        .post("/api/user/queryByCondition", this.condition)
        .then((res) => {
          if (res.code == 200) {
            this.tabelData = res.data.content;
            this.total = res.data.totalElements;
            this.loading = false;
          }
        })
        .catch((err) => {
          this.loading = false;
          this.$message({
            message: err,
            type: "error",
          });
        });
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize;
      this.getUserList();
    },
    handleCurrentChange(page) {
      this.condition.currentPage = page;
      this.getUserList();
    },
    queryConsultById(id) {
      this.$router.push({ path: "/consult", query: { folderId: id } });
    },
  },
};
</script>
<style type="text/css" scoped>
.el-container {
  height: 80vh;
}
.el-main {
  overflow: hidden;
}
.el-form {
  height: 50px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.uploadFolder-form {
  margin-top: 5%;
}

.upload-user-excel {
  margin: 40px auto;
  height: 300px;
  text-align: center;
}
.el-dialog {
  height: 400px;
}
.footer {
  position: absolute;
  left: 0px;
  bottom: 10px;
  padding: 10px 20px;
  width: 100%;
  height: 50px;
  border-top: 1px solid #dbdcdc;
}

.table-list >>> .el-table__body-wrapper {
  height: calc(100% - 80px);
  overflow: auto;
}
</style>
