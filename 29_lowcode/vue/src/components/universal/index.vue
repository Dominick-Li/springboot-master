<template>
  <div>
    <el-container v-if="refreshView">
      <el-main>
        <div style="height:200px">
          <el-form label-width="80px">
            <el-row style="margin-bottom:30px">
              <el-col :span="15">
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
                 <el-col :span="9">
                <el-button
                  class="btnRight"
                  type="primary"
                  size="small"
                  @click="search()"
                  >查询
                </el-button>
                 </el-col>
            </el-row>
            <el-row v-for="columnList in searchMetaColumnList" :key="columnList" >
                <el-col
                :span="8"
                v-for="column in columnList"
                :key="column.code"
              >
                 <el-form-item :label="column.name+':'" :key="column.code" class="names">
                        <el-input
            v-if="column.type=='0'"
            type="number"
            v-model="column.value"
          ></el-input>
          <el-date-picker style="width:55%"
            v-else-if="column.type=='1'"
                     v-model="column.betweenValue"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  >
                  </el-date-picker>
          <el-input v-else v-model="column.value"></el-input>
                </el-form-item>
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
                v-for="column in metaColumnList"
                v-if="column.viewShow"
                :key="column.code"
                :prop="column.code"
                :label="column.name"
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
      <save :refMetaColumnList="refMetaColumnList" :metaId="condition.metaId">
      </save>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import save from "./save";
export default {
  name: "baseData",
  data() {
    return {
      condition: {
        //元数据模型的Id
        metaId: "",
        //当前页
        currentPage: 1,
        //每页显示的数据量
        pageSize: 10,
        //初始化加载需要获取列信息
        firstInit: true,
        //动态查询条件 暂时不实现
        conditionList: [
          {
            name: "username",
            type: 2,
            value: "",
          },
        ],
      },
      saveTitle: "新增数据",
      isAllChecked: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tabelData: [],
      metaColumnList: [],
      refMetaColumnList: [],
      searchMetaColumnList: [],
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
    let path = this.$route.path;
    let arr=path.split("/");
    this.condition.metaId=arr[arr.length-1];
    this.condition.firstInit = true;
  },
  mounted() {
    this.$nextTick(() => {
      this.getDataList();
    });
    this.$bus.on("closeMetaSaveDialog", () => {
      this.dialogSaveVisible = false;
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
    search(){
      let arr=[];
      this.searchMetaColumnList.forEach((items=>{
         items.forEach((item)=>{
            arr.push(item);
         });
      }));
      this.condition.conditionList=arr;
      this.getDataList();
    },
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
      this.$ajax
        .post("/api/universal/delete", {
          metaId: this.condition.metaId,
          idList: ids,
        })
        .then((res) => {
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
      this.refMetaColumnList = JSON.parse(JSON.stringify(this.metaColumnList));
      if (row === undefined) {
        this.saveTitle = "新增数据";
      } else {
        this.refMetaColumnList.forEach((item, index) => {
          item.value = row[item.code];
        });
        let id = {
          code: "id",
          dataType: "int",
          value: row["id"],
        };
        this.refMetaColumnList.push(id);
        this.saveTitle = "修改数据";
      }
      this.$nextTick(() => {
        this.dialogSaveVisible = true;
      });
    },
    getDataList() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      this.$ajax
        .post("/api/universal/query", this.condition)
        .then((res) => {
          this.loading = false;
          if (res.code == 200) {
            this.tabelData = res.data.tabelData;
            this.total = res.data.totalElements;
            if (this.condition.firstInit) {
              this.metaColumnList = res.data.metaColumnList;
              this.searchMetaColumnList = res.data.searchMetaColumnList;
              this.condition.firstInit = false;
            }
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
