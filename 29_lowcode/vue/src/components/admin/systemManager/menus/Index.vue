<template>
  <div class="menus">
    <el-container>
      <el-main>
        <div>
          <el-form label-width="80px" class="header-btn">
            <el-row>
              <el-col :span="6">
                <el-button
                  class="btnRight"
                  type="primary"
                  size="small"
                  @click="updateAll"
                  >顺序刷新
                </el-button>
              </el-col>
              <el-col :span="6" style="padding-left: 20px">
                <el-button type="primary" size="small" @click="getAllMenus"
                  >查询</el-button
                >
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="table-container">
          <div class="table-list">
            <el-tree
              :data="menuList"
              class="select-contain"
              ref="selectContain"
              :render-after-expand="true"
              @node-click="selectNodeClick"
              :props="defaultProps"
              draggable
              :allow-drop="allowDrop"
              icon-class=""
              default-expand-all
              :expand-on-click-node="false"
            >
              <span
                class="custom-tree-node"
                slot-scope="{ node, data }"
                :rowdata="JSON.stringify(data)"
              >
                <span :class="data.icon"></span>
                <span :class="{ rightAdd: data.id ? false : true }">{{
                  node.label
                }}</span>
                <!-- <el-button
                    type="info"
                    size="mini"
                    class="addRight exitBtn"
                    @click.stop="exitSelect(data, $event)"
                    >取消选中</el-button
                  > -->
              </span>
            </el-tree>
          </div>
          <div class="content-edit">
            <div class="center-Abso">
              <el-form
                class="proect-name"
                ref="selectform"
                :model="selectParams"
                label-width="88px"
                label-position="left"
              >
                <el-form-item label="名称" prop="title">
                  <el-input
                    v-model="selectParams.title"
                    placeholder="请输入名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="访问地址">
                  <el-input
                    v-model="selectParams.path"
                    placeholder="请输入访问地址"
                  ></el-input>
                </el-form-item>
                <el-form-item label="使用组件">
                  <el-input
                    v-model="selectParams.importStr"
                    placeholder="请输入使用组件"
                  ></el-input>
                </el-form-item>
                <el-form-item label="图标" prop="icon">
                  <el-input
                    v-model="selectParams.icon"
                    placeholder="请输入图标"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div class="btns">
                <el-button type="primary" size="mini" @click="rightAdd(0)"
                  >添加根菜单</el-button
                >
                <el-button type="primary" size="mini" @click="rightAdd(1)"
                  >添加子菜单</el-button
                >
                <el-button type="primary" size="mini" @click="editSelect"
                  >修改</el-button
                >
                <el-button type="primary" size="mini" @click="delSelect"
                  >删除</el-button
                >
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// import save from './Save'
export default {
  name: "",
  data() {
    return {
      loading: false,
      tabelData: [],
      expandRowKeys: [], //展开行id的数组
      menuList: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      checkRigthRow: { rowData: "", rowNode: "", el: "" },
      selectParams: {
        // title:'',
        // path:'',
        // icon:''
      },
      role: {},
    };
  },
  mounted() {
    this.getAllMenus();
    this.$bus.on("closeMenuSaveDialog", () => {
      this.dialogMenusVisible = false;
    });

    this.$bus.on("refreshMenuList", () => {
      this.dialogMenusVisible = false;
      this.getAllMenus();
    });
  },
  beforeDestroy() {
    this.$bus.off("closeMenuSaveDialog");
    this.$bus.off("refreshMenuList");
  },
  methods: {
    rightAdd(type) {
      if (!this.selectParams.title) {
        this.$message({
          message: "请输入菜单名称",
          type: "warning",
        });
        return;
      }
      var params = {
        title: this.selectParams.title,
        path: this.selectParams.path,
        importStr: this.selectParams.importStr,
        icon: this.selectParams.icon,
        children: [],
      };
      if (type == 0) {
        this.submitForm(1);
      } else {
        if (this.notAdd) {
          this.$message.warning("菜单仅两层，请选择根菜单添加");
          return;
        }
        this.submitForm(2);
      }
    },
    editSelect() {
      if (!this.checkRigthRow.rowData) {
        this.$message({
          message: "请选中菜单",
          type: "warning",
        });
        return;
      }
      if (!this.selectParams.title) {
        this.$message({
          message: "请输入菜单名称",
          type: "warning",
        });
        return;
      }
      this.submitForm(0);
    },
    clearAll() {
      this.selectParams = {};
    },
    submitForm(type) {
      this.role = {
        title: this.selectParams.title,
        path: this.selectParams.path,
        importStr: this.selectParams.importStr,
        icon: this.selectParams.icon,
        sortIndex: this.selectParams.sortIndex,
      };
      if (type == 0) {
        this.role.id = this.selectParams.id;
        if (this.selectParams.parentId) {
          this.role.parentId = this.selectParams.parentId;
        }
      } else {
        this.role.id = null;
        if (type == 1) {
          this.role.parentId = 0;
          this.role.sortIndex = this.menuList.length + 1;
        } else if (type == 2) {
          this.role.parentId = this.selectParams.id;
          this.role.sortIndex = this.checkRigthRow.rowData.children.length + 1;
        }
      }
      if (this.loading) {
        return;
      }
      this.loading = true;
      let message = this.role.id ? "修改成功" : "添加成功";
      console.log(this.role);
      this.$ajax
        .post("/api/menu/saveMenu", this.role)
        .then((res) => {
          if (res.code == 200) {
            this.$notify({
              title: "提示",
              message: message,
              type: "success",
              duration: 1000,
              customClass: "el-notification-error",
            });
            this.clearAll();
            this.$bus.emit("refreshMenuList");
          } else {
            this.$notify({
              title: "提示",
              message: res.msg,
              type: "error",
              duration: 1500,
            });
          }
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
          this.$notify({
            title: "提示",
            message: "服务器错误",
            type: "success",
            duration: 1000,
            customClass: "el-notification-error",
          });
        });
    },
    updateAll() {
      console.log(this.menuList);
      this.$ajax
        .post("/api/menu/reloadMenu", this.menuList)
        .then((res) => {
          if (res.code == 200) {
            this.$notify({
              title: "提示",
              message: "刷新成功",
              type: "success",
              duration: 1000,
              customClass: "el-notification-error",
            });
            this.clearAll();
            this.$bus.emit("refreshMenuList");
            this.$utils.setSessionStorage("roleMenus", res.data);
            this.$bus.emit("refreshMenus");
          } else {
            this.$notify({
              title: "提示",
              message: res.msg,
              type: "error",
              duration: 1500,
            });
          }
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
          this.$notify({
            title: "提示",
            message: "服务器错误",
            type: "success",
            duration: 1000,
            customClass: "el-notification-error",
          });
        });
    },
    delSelect() {
      if (!this.checkRigthRow.rowData) {
        this.$message({
          message: "请选中数据",
          type: "warning",
        });
        return;
      }
      this.deleteSelected(this.checkRigthRow.rowData);
    },
    selectNodeClick(data, node, el) {
      console.log(data, node, el);
      if (
        [].slice
          .call(el.$el.parentNode.parentNode.parentNode.classList)
          .indexOf("el-tree") != -1
      ) {
        // 该操作会导致超过当前最大深度，请另选一项
        this.notAdd = true;
        this.isChild = true;
      } else {
        this.notAdd = false;
        this.isChild = false;
      }
      this.$set(this.checkRigthRow, "rowData", data);
      this.selectParams = JSON.parse(JSON.stringify(data));
      if (el) {
        this.$set(this.checkRigthRow, "rowNode", node);
        this.$set(this.checkRigthRow, "el", el.$el);
        this.$nextTick(() => {
          $(".select-contain .el-tree-node__content")
            .removeClass("active")
            .removeClass("searchon");
          el.$el
            .querySelectorAll(".select-contain .el-tree-node__content")[0]
            .classList.add("active");
        });
      }
    },
    allowDrop(draggingNode, dropNode, type) {
      if (type == "inner") {
        if (draggingNode.data.children.length > 0 || dropNode.level == 2) {
          return false;
        } else {
          return true;
        }
      } else {
        if (dropNode.level == 2 && draggingNode.data.children.length > 0) {
          return false;
        } else {
          return true;
        }
      }
    },
    create(row, parentId) {
      this.saveTitle = row ? "修改菜单" : "创建菜单";
      this.updateRow = row ? row : "";
      this.parentId = parentId ? parentId : "";
      this.$nextTick(() => {
        this.dialogMenusVisible = true;
      });
    },
    getAllMenus(row, length) {
      this.$ajax.get("/api/menu/findAll").then((res) => {
        if (res.code == 200) {
          this.menuList = res.data;
        }
      });
    },
    deleteSelected(row) {
      let ids;
      if (row.id) {
        ids = [row.id];
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
      }
      this.$ajax.post("/api/menu/deleteByIds", ids).then((res) => {
        if (res.code == 200) {
          this.$notify({
            title: "提示",
            message: "删除成功",
            type: "success",
            duration: 1500,
            customClass: "el-notification-error",
          });
          this.checkRigthRow = {
            rowData: "",
            rowNode: "",
            el: "",
          };
          this.notAdd = false;
          this.clearAll();
          this.getAllMenus();
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
  },
};
</script>

<style>
.row-expand-cover .el-table__expand-icon {
  visibility: hidden;
  opacity: 0;
}
</style>
<style scoped>
.menus {
  height: 100%;
}
.el-container {
  height: 100%;
}
.el-main {
  overflow: hidden;
}
.header-btn {
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
.onlineDiago /deep/.el-dialog {
  margin: 0 auto;
  margin-top: 3vh !important;
  height: 95%;
}
.onlineDiago /deep/.el-dialog__body {
  height: calc(100% - 54px);
}
.menus .table-container {
  display: flex;
  height: calc(100% - 35px);
}
.menus .table-list {
  height: 100%;
  width: 60%;
  border-right: 1px solid #ccc;
}
.content-edit {
  width: 40%;
  padding: 20px;
  position: relative;
}
.menus .select-contain {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.select-contain >>> .el-tree-node__content.active,
.select-contain >>> .el-tree-node__content.active .custom-tree-node {
  background-color: #e9eaeb;
  line-height: 27px;
}
.center-Abso {
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -55%);
}
.btns {
  text-align: center;
}
.proect-name >>> .el-form-item {
  margin-bottom: 36px;
}
.select-contain >>> .el-tree-node__content {
  height: 33px;
}
</style>
