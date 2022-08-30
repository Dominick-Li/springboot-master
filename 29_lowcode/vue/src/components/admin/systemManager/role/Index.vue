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
                  @click="create()"
                  >添加角色
                </el-button>
                <el-button
                  class="btnRight"
                  type="warning"
                  size="small"
                  @click="deleteSelected"
                  >批量删除
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="table-container">
          <div class="table-list">
            <el-table
              class="tableLimit"
              :data="tabelData"
              style="width: 100%;height :100%"
              @selection-change="selectAll"
              :row-class-name="tableRowClassName"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                prop="roleName"
                label="角色名称"
              ></el-table-column>
              <el-table-column
                prop="roleCode"
                label="角色编码"
              ></el-table-column>
              <el-table-column
                prop="userStr"
                label="使用的用户"
                min-width="60"
              ></el-table-column>
              <el-table-column
                prop="createTime"
                label="创建时间"
                min-width="60"
              ></el-table-column>
              <el-table-column label="操作" min-width="120">
                <template slot-scope="scope">
                  <el-button @click="create(scope.row)" type="text" size="small"
                    >修改
                  </el-button>
                  <el-button :disabled="scope.row.builtIn==1"
                    @click="deleteSelected(scope.row)"
                    type="text"
                    size="small"
                    >删除
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
      v-if="dialogTableVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="65%"
      height="70%"
    >
      <save :refdata="role"> </save>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import save from "./save";
export default {
  name: "RoleList",
  data() {
    return {
      role: {},
      condition: {
        like: {},
        currentPage: 1,
        pageSize: 10,
      },
      saveTitle: "新增角色",
      isAllChecked: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tabelData: [],
      selectedIds: [],
      dialogTableVisible: false,
      loading: false,
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
    this.$nextTick(() => {
      this.getDataList();
    });
    this.$bus.on("closeRoleSaveDialog", () => {
      this.dialogTableVisible = false;
    });

    this.$bus.on("refreshRoleList", () => {
      this.dialogTableVisible = false;
      this.getDataList();
    });
  },
  beforeDestroy() {
    this.$bus.off("closeRoleSaveDialog")
    this.$bus.off("refreshRoleList")
  },
  methods: {
    selectAll(sels) {
      this.selectedIds = sels;
    },
    deleteSelected(row) {
      let ids;
      if (row.id) {
        ids = [row.id];
        this.$confirm(
            '提示', {
              message: '确认要执行删除操作?',
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then((res) => {
            this.sureDeleteSelect(ids)
          }).catch((err) => {

          })
      } else {
        if (this.selectedIds.length == 0) {
          this.$notify({
            title: "提示",
            message: "您未选择数据",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        ids = this.selectedIds.map((item) => item.id);
        this.sureDeleteSelect(ids)
      }
    },
    sureDeleteSelect(ids){
      this.$ajax.post("/api/role/delete",ids).then((res) => {
        if (res.code == 200) {
          this.$notify({
            title: "提示",
            message: "删除成功",
            type: "success",
            duration: 1500,
            customClass: "el-notification-error",
          });
          this.loading = false;
          this.getDataList();
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
    },
    create(row) {
      if (row === undefined) {
        this.saveTitle = "新增角色";
        this.role = {};
      } else {
        this.saveTitle = "修改角色";
        this.role = row;
      }
      this.$nextTick(() => {
        this.dialogTableVisible = true;
      });
    },
    getDataList() {
      this.loading = true;
      this.$ajax
        .post("/api/role/queryByCondition", this.condition)
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
      this.getDataList();
    },
    handleCurrentChange(page) {
      this.condition.currentPage = page;
      this.getDataList();
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
.table-pagination {
  text-align: right;
    /* position: absolute;
    left: 50%;
    transform: translateX(-45%);
    bottom: 0px; */
}
.table-list >>>.el-table__body-wrapper{
  height: calc( 100% - 55px);
  overflow: auto;
}
</style>
