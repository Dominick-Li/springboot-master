<template>
  <div>
    <el-container v-if="refreshView">
      <el-main>
        <div>
          <el-form label-width="80px">
            <el-row>
              <el-col :span="4">
                <el-button
                  class="btnRight"
                  type="primary"
                  size="small"
                  @click="create()"
                  >添加 
                </el-button>
                <el-button
                  class="btnRight"
                  type="warning"
                  size="small"
                  @click="deleteSelected"
                  >批量删除
                </el-button>
              </el-col>
              <el-col :span="6">
                <el-form-item label="模型名称" class="names">
                  <el-input v-model="condition.like.metaName"></el-input>
                </el-form-item>
              </el-col>
                <el-col :span="12">
                <div class="block">
                  <span class="demonstration">创建日期</span>
                  <el-date-picker
                    v-model="createDate"
                    type="daterange"
                    align="right"
                    value-format="yyyy-MM-dd"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  >
                  </el-date-picker>
                </div>
              </el-col>
              <el-col :span="2">
                <el-button type="primary" size="small" @click="getDataList"
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
              style="width: 100%; height: 100%"
              @selection-change="selectAll"
              :row-class-name="tableRowClassName"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                prop="metaName"
                label="模型名称"
              ></el-table-column>
              <el-table-column
                prop="tableCode"
                label="数据库表名称"
              ></el-table-column>
              <el-table-column label="主键是否自增" min-width="50">
                <template slot-scope="scope">
                  <el-checkbox
                    v-model="scope.row.increment"
                    disabled
                  ></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column
                prop="metaDescription"
                label="描述"
                min-width="60"
              ></el-table-column>
              <el-table-column
                prop="createTime"
                label="创建时间"
                min-width="60"
              ></el-table-column>
              <el-table-column
                prop="lastmodifiedTime"
                label="修改时间"
                min-width="60"
              ></el-table-column>
              <el-table-column label="操作" min-width="120">
                <template slot-scope="scope">
                  <el-button @click="create(scope.row)" type="text" size="small"
                    >修改
                  </el-button>
                  <el-button
                    :disabled="scope.row.builtIn == 1"
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
      :visible.sync="dialogSaveVisible"
      v-if="dialogSaveVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="80%"
      class="saveMeatDialog"
    >
      <save :refdata="meta" :dataTypeList="dataTypeList"> </save>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import save from "./save";
export default {
  name: "baseData",
  data() {
    return {
      meta: {},
      createDate:'',
      condition: {
        like: {
          metaName: ''
        },
        currentPage: 1,
        pageSize: 10,
      },
      saveTitle: "添加",
      isAllChecked: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tabelData: [],
      dataTypeList: [],
      selectedIds: [],
      dialogSaveVisible: false,
      loading: false,
      refreshView: true,
    };
  },
  components: {
    save,
  },
  watch: {
    condition: {
      handler(val) {
        this.$store.commit("setEachCondition", {
          name: this.$route.name,
          condition: val,
        });
      },
      deep: true,
    },
  },
  created() {
    var thisCondition = this.$store.state.eachCondition[this.$route.name];
    if (thisCondition) {
      this.condition = thisCondition;
      this.pageNum = this.condition.currentPage;
      this.pageSize = this.condition.pageSize;
      this.refreshView = false;
      this.$nextTick(() => {
        this.refreshView = true;
      });
    }
    this.$ajax.get("/api/meta/getDataTypeList").then((res) => {
      if (res.code == 200) {
        this.dataTypeList = res.data;
      }
    });
  },
  mounted() {
    this.$nextTick(() => {
      this.getDataList();
    });
    this.$bus.on("closeMetaSaveDialog", () => {
      this.dialogSaveVisible = false;
    });

    this.$bus.on("closeMetaViewDialog", () => {
      this.dialogViewVisible = false;
    });

    this.$bus.on("refreshMetaList", () => {
      this.dialogSaveVisible = false;
      this.getDataList();
    });
  },
  beforeDestroy() {
    this.$bus.off("closeMetaSaveDialog");
    this.$bus.off("refreshMetaList");
  },
  methods: {
    selectAll(sels) {
      this.selectedIds = sels;
    },
    deleteSelected(row) {
      let ids;
      if (row.id) {
        ids = [row.id];
        this.$confirm("提示", {
          message: "确认要执行删除操作?",
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then((res) => {
            this.sureDeleteSelect(ids);
          })
          .catch((err) => {});
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
        this.sureDeleteSelect(ids);
      }
    },
    sureDeleteSelect(ids) {
      this.$ajax.post("/api/meta/delete", ids).then((res) => {
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
          this.$bus.emit('refreshMenus')
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
        this.saveTitle = "新增";
        this.meta = {
          metaColumnList: [
            {
              id: "",
              columnName: "",
              columnCode: "",
              dataType: "varchar(255)",
              viewShow: true,
              search: true,
            },
          ],
        };
      } else {
        this.saveTitle = "修改";
        this.meta = row;
      }
      this.$nextTick(() => {
        this.dialogSaveVisible = true;
      });
    },
    getDataList() {
      if (this.loading) {
        return;
      }
      if(this.createDate!=null) {
          this.condition.beginDate = this.createDate[0]
          this.condition.endDate = this.createDate[1]
        }else{
          this.condition.beginDate =''
          this.condition.endDate = ''
        }
      this.loading = true;
      this.$ajax
        .post("/api/meta/queryByCondition", this.condition)
        .then((res) => {
          this.loading = false;
          if (res.code == 200) {
            this.tabelData = res.data.content;
            this.total = res.data.totalElements;
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
    getDataTypeList() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      this.$ajax
        .get("/api/meta/getDataTypeList")
        .then((res) => {
          this.loading = false;
          if (res.code == 200) {
            this.dataTypeList = res.data;
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
.saveMeatDialog >>> .el-dialog {
  margin-top: 7vh !important;
  height: 85vh;
}
.saveMeatDialog >>> .el-dialog__body {
  height: calc(100% - 49px);
  overflow: hidden;
}
.viewDialog >>> .el-dialog__body {
  height: 60%;
  overflow: hidden;
}
.saveMeatDialog >>> .dialog-footer {
  position: absolute;
  right: 10px;
  bottom: 10px;
}

.table-pagination {
  text-align: right;
}
.table-list >>> .el-table__body-wrapper {
  height: calc(100% - 55px);
}
</style>
