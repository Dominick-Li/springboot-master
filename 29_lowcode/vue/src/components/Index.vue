<template>
  <div class="container">
    <el-container>
      <el-header v-if="!integration">
        <div class="headerD">
          <div class="leftT">
            <span style="color: green">皓亮君</span>的低代码平台
          </div>
          <div class="rightT">
            用户名:
            {{
              this.$utils.getSessionStorage("user")
                ? this.$utils.getSessionStorage("user").username
                : ""
            }}
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                个人信息<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="updatePassword"
                  >修改密码</el-dropdown-item
                >
                <el-dropdown-item command="logOut">退出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-container id="indexMain">
        <el-aside v-if="!integration">
          <el-menu
            :default-active="defaultActive"
            class="el-left-menu"
            background-color="#333333"
            text-color="#fff"
            active-text-color="#ffd04b"
            @select="handleSelect"
            :collapse="isCollapse"
            menu-trigger="click"
            router
          >
            <!-- 遍历路由表，生成左侧菜单 -->
            <template v-for="(item, index) in menus">
              <!-- 单层菜单 -->
              <template v-if="!(item.children && item.children.length > 0)">
                <template v-if="item.path.indexOf('iframeView') != -1">
                  <li
                    class="el-menu-item oulineLi"
                    :key="index"
                    :topath="item.path"
                    @click="handlerClick(item, $event)"
                    style="color: #fff; background-color: #333333"
                  >
                    <i v-if="item.meta.icon" :class="item.meta.icon"></i>
                    <span slot="title" v-show="!isHide">{{
                      item.meta.title
                    }}</span>
                  </li>
                </template>
                <template v-else>
                  <el-menu-item
                    :index="item.path"
                    :key="index"
                    :class="{ hidemenu: item.ishide }"
                    :menuname="item.name"
                  >
                    <template slot="title">
                      <i v-if="item.meta.icon" :class="item.meta.icon"></i>
                      <span slot="title" v-show="!isHide">{{
                        item.meta.title
                      }}</span>
                    </template>
                  </el-menu-item>
                </template>
              </template>
              <!-- 多级菜单 -->
              <template v-else>
                <el-submenu
                  :index="item.path"
                  :key="index"
                  class="menuName"
                  :name="item.name"
                >
                  <template slot="title">
                    <i :class="item.meta.icon"></i>
                    <span slot="title" v-show="!isHide">{{
                      item.meta.title
                    }}</span>
                  </template>
                  <template v-for="(itemChild, indexChild) in item.children">
                    <template
                      v-if="itemChild.children && itemChild.children.length > 0"
                    >
                      <side-meuns
                        :routes="[itemChild]"
                        class="nest-menu"
                        :key="indexChild"
                      ></side-meuns>
                    </template>
                    <template v-else>
                      <template v-if="item.path.indexOf('iframeView') != -1">
                        <li
                          class="el-menu-item oulineLi"
                          :key="indexChild"
                          :topath="itemChild.path"
                          @click="handlerClick(itemChild, $event)"
                          style="color: #fff; background-color: #333333"
                        >
                          <i
                            v-if="itemChild.meta.icon"
                            :class="itemChild.meta.icon"
                          ></i>
                          <span
                            slot="title"
                            v-show="!isHide"
                            :name="itemChild.name"
                            >{{ itemChild.meta.title }}</span
                          >
                        </li>
                      </template>
                      <template v-else>
                        <el-menu-item
                          :index="itemChild.path"
                          :key="indexChild"
                          :to="itemChild.path"
                        >
                          <i
                            v-if="itemChild.meta.icon"
                            :class="itemChild.meta.icon"
                          ></i>
                          <span
                            class="menu-childname"
                            slot="title"
                            v-show="!isHide"
                            :name="itemChild.name"
                            >{{ itemChild.meta.title }}</span
                          >
                        </el-menu-item>
                      </template>
                    </template>
                  </template>
                </el-submenu>
              </template>
            </template>
          </el-menu>
        </el-aside>
        <div class="showhide" v-if="!integration">
          <span
            :class="['iconfont', isHide ? 'icon-weibiaoti25' : 'icon-shouqi']"
            @click="togoole()"
            :title="isHide ? '展开' : '收起'"
          ></span>
        </div>
        <el-button
          type="success"
          id="openClose"
          v-show="false"
          @click="openMenu"
          plain
        >
          ></el-button
        >
        <el-main class="menu-main" :class="{ nologin: integration }">
          <el-tabs
            class="tab-contain"
            v-model="$store.state.editableTabsValue"
            type="card"
            @tab-remove="removeTab"
            @tab-click="clickTab"
          >
            <el-tab-pane
              :key="item.path"
              v-for="(item, index) in $store.state.editableTabs"
              :label="item.title"
              :name="item.path"
              :closable="index == 0 ? false : true"
              class="tabpane"
            >
              <!-- <tab-component :is="item.content" class="tabcomp"></tab-component> -->
            </el-tab-pane>
          </el-tabs>
          <div class="content-wrap">
            <keep-alive v-if="isKeepAlive">
              <router-view :key="$route.path" class="viewContainer" ref="viewContainer" />
            </keep-alive>
          </div>
        </el-main>
      </el-container>
    </el-container>
    <el-dialog
      title="修改密码"
      :visible.sync="dialogTableVisible"
      v-if="dialogTableVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="40%"
      height="70%"
    >
      <update-password> </update-password>
    </el-dialog>
    <div class="closeTab">
      <ul class="ultab">
        <li class="tab-each" @click="close('own')">关闭</li>
        <li class="tab-each" @click="close('other')">关闭其他</li>
        <li class="tab-each" @click="close('other-left')">关闭左侧</li>
        <li class="tab-each" @click="close('other-right')">关闭右侧</li>
        <li class="tab-each" @click="close('all')">关闭所有</li>
      </ul>
    </div>
  </div>
</template>

<script type="text/javascript">
import Vue from "vue";
import updatePassword from "./common/updatePassword";
export default {
  name: "adminIndex",
  data() {
    return {
      isCollapse: false,
      menus: [],
      defaultActive: "",
      isHide: false,

      dialogTableVisible: false,
      id: 0,
      showTitle: true,
      activeDom: "",
      isKeepAlive: true,
      integration: false,
      otherLineMap: {},
    };
  },
  computed: {},
  components: {
    updatePassword,
  },
  created() {
    // var menus=this.$store.state.addRoutes
    var menuRoutes = sessionStorage.getItem("roleMenus") || "[]";
    menuRoutes = JSON.parse(menuRoutes);
    this.setMenuRoutes(menuRoutes);
  },
  mounted() {
    this.$bus.on("addHideMenu", (item) => {
      item.ishide = true;
      this.menus.push(item);
      this.$nextTick(() => {
        $(".hidemenu:last-child").click();
        $(".hidemenu").hide();
      });
    });
    this.$bus.on("refreshMenus", () => {
      // this.$store.commit("addRoutes",[])
      this.refreshMenus();
    });
    // 关闭修改密码弹框
    this.$bus.on("closeUpdatePasswordDialog", () => {
      this.dialogTableVisible = false;
    });
    this.$bus.on("logOut", () => {
      this.logOut();
    });
    this.$bus.on("hideMenu", () => {
      if (!$(".el-aside").hasClass("asidehide")) {
        $(".el-aside").addClass("asidehide");
      }
      if (!$(".showhide").hasClass("ishide")) {
        $(".showhide").addClass("ishide");
      }
      this.isHide = true;
    });
    this.$bus.on("refreshView", () => {
      this.isKeepAlive = false;
      this.$nextTick(() => {
        this.isKeepAlive = true;
      });
    });
    this.$nextTick(() => {
      if (!this.isPC) {
        this.togoole();
      }
      setTimeout(() => {
        this.setTabsEvent();
      }, 100);
    });
  },
  beforeDestroy() {
    this.$bus.off("closeUpdatePasswordDialog");
    this.$bus.off("logOut");
    this.$bus.off("hideMenu");
    this.$bus.off("refreshComponent");
  },
  methods: {
    setMenuRoutes(menuRoutes){
            this.menus = [];
    this.otherLineMap = {};
    menuRoutes.forEach((item, index) => {
      var url, name;
      if (!/^\//g.test(item.path)) {
        url = "/index/iframeView/" + index;
        name = "iframeView";
        this.otherLineMap[item.title] = item.path;
      } else {
        url = item.path;
        name = this.getLastIndexStr(item.path) + index;
      }
      var param = {
        path: url,
        name: name,
        meta: { title: item.title, icon: item.icon, redirectPath: item.path },
        children: [],
      };
      this.menus.push(param);
      if (item.children && item.children.length > 0) {
        var nowItem = this.menus[this.menus.length - 1];
        this.setMenuItem(nowItem, item, index);
      }
    });
    console.log(this.menus);
    this.$utils.setSessionStorage("menuRoutes", this.menus);
    this.defaultActive = this.$store.state.editableTabsValue;
    },
    handlerClick(item, e) {
      $(".el-menu-item").removeClass("is-active").css({
        color: "#fff",
      });
      var $target = $(e.target);
      if (!$target.hasClass("el-menu-item")) {
        $target = $target.parents(".el-menu-item");
      }
      $target.addClass("is-active");

      this.activeTitile = item.meta.title;
      this.$router.push({
        path: item.path,
        name: item.name,
        params: {
          type: item.path.slice(item.path.lastIndexOf("/") + 1),
          redirectPath: item.meta.redirectPath,
        },
      });
      this.defaultActive = item.path;
    },
    getLastIndexStr() {
      return str.slice(str.lastIndexOf("/") + 1);
    },
    setMenuItem(nowItem, item, index) {
      item.children.forEach((it, i) => {
        index = index + i + 1;
        var url, name;
        if (!/^\//g.test(it.path)) {
          url = "/index/iframeView/" + index;
          name = "iframeView";
          this.otherLineMap[it.title] = it.path;
        } else {
          url = it.path;
          name = this.getLastIndexStr(it.path) + index;
        }
        var param = {
          path: url,
          name: name,
          meta: { title: it.title, icon: it.icon, redirectPath: it.path },
          children: [],
        };
        nowItem.children.push(param);
        if (it.children && it.children.length > 0) {
          var thirdItem = nowItem.children[nowItem.children.length - 1];
          this.setMenuItem(thirdItem, it, index);
        }
      });
    },
    refreshMenus() {
      this.$ajax.get("/api/role/getMenuList").then((response) => {
        this.$utils.setSessionStorage("roleMenus", response.data);
        this.setMenuRoutes(response.data);
      })
    },
    getLastIndexStr(str) {
      return str.slice(str.lastIndexOf("/") + 1);
    },
    // tabs 右击关闭tab的弹窗
    close(type) {
      var tabs = this.$store.state.editableTabs;
      var activeValue = $(this.activeDom)
        .attr("aria-controls")
        .replace("pane-", "");
      // console.log(tabs,activeValue)
      var nowRouteItem, nowTabs;
      if (type == "own") {
        // 关闭自身
        for (let i = 0; i < tabs.length; i++) {
          if (tabs[i].path == activeValue) {
            tabs.splice(i, 1);
            break;
          }
        }
        if (this.$store.state.editableTabsValue === activeValue) {
          nowRouteItem = tabs[tabs.length - 1];
        }
      } else if (type == "other") {
        // 关闭其它
        var indexs = [];
        for (let i = 0; i < tabs.length; i++) {
          if (i != 0) {
            if (tabs[i].path != activeValue) {
              indexs.push(i);
            }
          }
        }
        indexs.forEach((item, i) => {
          tabs.splice(item - i, 1);
        });
      } else if (type.indexOf("other-") != -1) {
        var activeIndex = "";
        for (let i = 0; i < tabs.length; i++) {
          if (tabs[i].path == activeValue) {
            activeIndex = i;
            nowRouteItem = tabs[i];
            break;
          }
        }
        if (type == "other-left") {
          // 关闭自身左边
          nowTabs = tabs.slice(activeIndex);
          nowTabs.unshift(tabs[0]);
        } else if (type == "other-right") {
          // 关闭自身右边
          nowTabs = tabs.slice(0, activeIndex + 1);
        }
        this.$store.commit("refresh_tabs", nowTabs);
      } else if (type == "all") {
        // 关闭所有
        nowTabs = tabs.splice(0, 1);
        this.$store.commit("refresh_tabs", nowTabs);
        nowRouteItem = nowTabs[0];
      }
      if (nowRouteItem) {
        console.log(nowRouteItem);
        $(".el-menu-item").removeClass("is-active");
        this.activeTitile = nowRouteItem.title;
        this.$store.commit("set_active_index", nowRouteItem.path);
        if (nowRouteItem.path.indexOf("iframeView") != -1) {
          this.$router.push({
            path: nowRouteItem.path,
            name: nowRouteItem.name,
            params: {
              type: nowRouteItem.path.slice(
                nowRouteItem.path.lastIndexOf("/") + 1
              ),
              redirectPath: nowRouteItem.redirectPath,
            },
          });
          $(".oulineLi[topath='" + nowRouteItem.path + "']").addClass(
            "is-active"
          );
        } else {
          this.$router.push(nowRouteItem.path);
        }
        this.defaultActive = nowRouteItem.path;
      }
      $(".closeTab").hide();
    },
    // 设置右击默认事件
    setTabsEvent() {
      var that = this;
      setTimeout(() => {
        document.body.onclick = function (e) {
          $(".closeTab").hide();
        };
        document.querySelector(".closeTab").onclick = function (e) {
          if (e.stopPropagation) e.stopPropagation();
          else e.cancelBubble = true;
        };
        //去掉默认的contextmenu事件，否则会和右键事件同时出现。
        $(".tab-contain .el-tabs__item").contextmenu(function (e) {
          e.preventDefault();
        });
        $(".tab-contain .el-tabs__item").mousedown(function (e) {
          if (e.button == 2) {
            e.preventDefault();
            that.activeDom = e.target || e.srcElement;
            $(".closeTab").css({
              display: "block",
              left: e.pageX + 5,
              top: e.pageY + 5,
            });
          }
        });
      }, 1000);
    },
    togoole() {
      $(".el-aside").toggleClass("asidehide");
      $(".showhide").toggleClass("ishide");
      this.isHide = !this.isHide;
    },
    handleSelect(key, keyPath) {
      // console.log(key, keyPath,this.$store.state.editableTabs);
      this.defaultActive = key;
      $(".oulineLi").removeClass("is-active").css("color", "#fff");
    },
    openMenu() {
      this.isCollapse = !this.isCollapse;
      if (true) {
        $("#openClose").css("left", "90px");
      } else {
        $("#openClose").css("left", "277px");
      }
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    removeTab(targetName) {
      // console.log(targetName)
      var routeName = targetName.slice(targetName.lastIndexOf("/") + 1);
      this.$store.commit("clearEachCondition", routeName); //清除保存的各页面的condition数据

      this.$store.commit("delete_tabs", targetName);
      setTimeout(() => {
        if (this.$store.state.editableTabsValue == targetName) {
          // 设置当前激活的路由
          var item =
            this.$store.state.editableTabs[
              this.$store.state.editableTabs.length - 1
            ];
          this.$store.commit("set_active_index", item.path);
          this.activeTitile = item.title;
          if (item.path.indexOf("iframeView") != -1) {
            this.$router.push({
              path: item.path,
              name: item.name,
              params: {
                type: item.path.slice(item.path.lastIndexOf("/") + 1),
                redirectPath: item.redirectPath,
              },
            });
          } else {
            $(".oulineLi").removeClass("is-active");
            this.$router.push(item.path);
          }
          this.defaultActive = item.path;
        }
        // console.log(this.$store.state.eachCondition)
      }, 100);
    },
    // 获取正数地址第二个
    getTwoIndexStr(str) {
      var newStr = str.slice(str.indexOf("/") + 1);
      var newstr2 = newStr.slice(newStr.indexOf("/") + 1);
      return newstr2.slice(0, newstr2.indexOf("/"));
    },
    clickTab(tab) {
      // console.log(tab)
      this.$store.commit("set_active_index", tab.name);
      this.activeTitile = tab.label;
      $(".el-menu-item").removeClass("is-active");
      if (tab.name.indexOf("iframeView") != -1) {
        this.$router.push({
          path: tab.name,
          name: this.getTwoIndexStr(tab.name),
          params: {
            type: tab.name.slice(tab.name.lastIndexOf("/") + 1),
            redirectPath: this.otherLineMap[tab.label],
          },
        });
        this.isKeepAlive = false;
        this.$nextTick(() => {
          this.isKeepAlive = true;
        });
        $(".oulineLi[topath='" + tab.name + "']").addClass("is-active");
      } else {
        this.$router.push(tab.name);
      }
      this.defaultActive = tab.name;
      console.log(this.defaultActive);
      this.$forceUpdate();
    },
    logOut() {
      this.$utils.clearSessionStorage();
      this.$store.commit("clearStore");
      this.$router.push({ path: "/" });
    },
    handleCommand(command) {
      if (command == "logOut") {
        this.logOut();
      } else if (command == "updatePassword") {
        this.dialogTableVisible = true;
      }
    },
    getMenuItem(menuList, to) {
      var routeMenuItem,
        that = this;
      function each(menuList, to) {
        for (let i = 0; i < menuList.length; i++) {
          const item = menuList[i];
          if (!(item.children && item.children.length > 0)) {
            if (
              (item.path == to.path && item.path.indexOf("iframeView") == -1) ||
              (item.path.indexOf("iframeView") != -1 &&
                item.meta.title == that.activeTitile)
            ) {
              routeMenuItem = item;
            }
          } else {
            if (!routeMenuItem) {
              each(item.children, to);
            }
          }
        }
      }
      each(menuList, to);
      return routeMenuItem;
    },
  },
  watch: {
    $route(to, from) {
      if (
        from.path == "/index/checkocr" &&
        this.$refs.viewContainer &&
        this.$refs.viewContainer.closeLoading
      ) {
        console.log(this.$refs.viewContainer);
        this.$refs.viewContainer.closeLoading();
      }
      console.log(to, from, this.$store.state.editableTabs, this.menus);
      let flag = false;
      let routers = [];
      for (let i = 0; i < this.$store.state.editableTabs.length; i++) {
        const item = this.$store.state.editableTabs[i];
        console.log(
          item.path.indexOf("iframeView") == -1 && item.path == to.path,
          item.path.indexOf("iframeView") != -1 &&
            item.title == this.activeTitile
        );
        if (
          (item.path.indexOf("iframeView") == -1 && item.path == to.path) ||
          (item.path.indexOf("iframeView") != -1 &&
            item.title == this.activeTitile)
        ) {
          console.log(item, to);
          this.$store.commit("set_active_index", item.path);
          flag = true;
          break;
        }
      }
      console.log(flag);
      if (!flag) {
        this.isKeepAlive = false;
        setTimeout(() => {
          this.isKeepAlive = true;
        }, 100);

        var item = this.getMenuItem(this.menus, to);
        console.log(item);
        if (item) {
          this.$store.commit("add_tabs", {
            path: to.path,
            title: item.meta.title,
            name: item.name,
            redirectPath: item.meta.redirectPath,
          });
          this.$store.commit("set_active_index", to.path);
          this.$nextTick(() => {
            this.setTabsEvent();
          });
        }
      }
      this.activeTitile = "";
    },
  },
};
</script>
<style type="text/css" scoped>
.container {
  /* padding: 5px; */
  width: 100%;
  height: 100%;
}
.el-header {
  padding: 0;
  height: 60px !important;
}
.el-container {
  height: 100%;
}
main.el-main {
  padding: 5px 0 0;
  overflow: hidden;
}
.el-tabs >>> .el-tabs__header {
  margin-left: 27px;
}
.el-aside {
  width: 170px !important;
  height: 100%;
  transition: all 0.3s;
}
.el-aside.asidehide {
  width: 55px !important;
  overflow: hidden;
}

.el-aside i {
  display: inline;
}
.el-aside.asidehide >>> .el-icon-arrow-down {
  display: none;
}
.el-aside li {
  padding-left: 10px !important;
}
.el-aside.asidehide li {
  /* text-align: center; */
}
.showhide {
  position: absolute;
  left: 174px;
  top: 15px;
  z-index: 100;
  cursor: pointer;
  transition: all 0.3s;
}
.showhide.ishide {
  left: 57px;
  top: 13px;
}
.showhide span {
  font-size: 16px;
}
.showhide.ishide span {
  font-size: 21px;
}
/* .el-left-menu:not(.el-menu--collapse) {
  width: 250px;
  min-height: 400px;
} */

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-icon-arrow-down {
  font-size: 12px;
}
.headerD {
  background-color: #333333;
  width: 100%;
  height: 100%;
  border-bottom: solid 1px white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: white;
}

.headerD div {
  margin: auto 0px;
}

.leftT {
  font-size: 20px;
  padding-left: 20px;
}

.rightT {
  padding-right: 20px;
}

#indexMain {
  height: calc(100% - 60px);
  position: relative;
  overflow: hidden;
}

#openClose {
  position: absolute;
  left: 277px;
  width: 20px;
  padding-left: 5px;
  padding-right: 5px;
  top: 40%;
}

/* .el-tabs, */
.el-tabs__content {
  height: 100%;
}
.el-tab-pane,
.tabcomp {
  height: 100%;
}
.el-left-menu .iconfont {
  color: #bec1c5;
  font-size: 17px;
  display: inline-block;
  width: 24px;
  text-align: center;
}
.el-menu-item.is-active .iconfont {
  color: rgb(255, 208, 75);
}
.el-menu-item.oulineLi.is-active {
  color: #ffd04b !important;
}
.el-left-menu >>> .el-submenu__title {
  padding: 0;
  padding-left: 0 !important;
}
.el-left-menu {
  height: 100%;
  overflow: auto;
}
.menuName .el-menu-item {
  min-width: auto;
}
.content-wrap {
  /* position: absolute;
    left: 270px;
    top: 61px; */
  /* width: calc(100% - 270px); */
  height: calc(100% - 54px);
}
.closeTab {
  position: absolute;
  top: 0;
  left: 292px;
  width: 140px;
  background: #fff;
  font-size: 13px;
  color: #515151;
  text-align: left;
  z-index: 666;
  border: 1px solid #eee;
  display: none;
}
.ultab {
  padding: 2px 0;
  line-height: 30px;
  box-shadow: 2px 2px 1px 0px #a2a2a2;
}
.tab-each {
  padding-left: 30px;
  cursor: pointer;
}
.tab-each:hover {
  background: #eee;
}
.tab-contain.el-tabs {
  height: 50px;
}
.viewContainer {
  height: 100%;
}
</style>
<style>
.el-tabs__content {
  height: calc(100% - 57px);
}
#pane-1 {
  height: 100%;
}
</style>
