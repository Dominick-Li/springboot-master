import Vue from "vue";
import Router from "vue-router";
import store from '../store/index'
Vue.use(Router);

// 固定的路由表
var fixedRouter = [
  {
    path: "/",
    redirect: {
      name: "login",
    },
  },
  {
    path: "/login",
    name: "login",
    component: (resolve) => require(["@/components/login.vue"], resolve),
    meta: { title: "登录页面" },
  },
  {
    path: "/index",
    name: "index",
    component: (resolve) => require(["@/components/Index"], resolve),
    meta:{
      name: '主页'
    },
    children:[]
  }, 
  {
    path: '*',
    redirect: '/'
  }
];

var createRouter = ()=>new Router({
	routes: fixedRouter,
  base: process.env.BASE_URL,
	mode:"history"
})
var VueRouterPush = Router.prototype.push;
Router.prototype.push = function push(to) {
  return VueRouterPush.call(this, to).catch((err) => err);
};

//写一个重置路由的方法，切换用户，或者退出时清除动态加载的路由
const router = createRouter()
function resetRouter(){
	const newRouter = createRouter()
	router.matcher = newRouter.matcher    //新路由的实例matcher，赋值给旧路由实例的matcher，(相当于replaceRouter)
}
var needPushRoute
for (let i = 0; i < router.options.routes.length; i++) {
  const routeItem= router.options.routes[i];
  if(routeItem.name=="index"){
    needPushRoute=routeItem
    break
  }
}

router.beforeEach((to, from, next) => {
  console.log(sessionStorage.getItem('token'),store.state.addRoutes.length==0,from.name, to.name)
  if(sessionStorage.getItem('token')&&store.state.addRoutes.length==0&& to.name){ // 
    var getRoutes = menuGetRoutes()
      getRoutes.forEach(item=>{
        needPushRoute.children.push(item)
      })
      console.log(getRoutes,router.options.routes)
      router.addRoutes(router.options.routes)
      store.commit("setAddRoutes",getRoutes)
      var routeItem
      if(getRoutes[0].children&&getRoutes[0].children.length>0){
        routeItem=getRoutes[0].children[0]
      }else{
        routeItem=getRoutes[0]
      }
      store.commit("updateBegin", {
        path:routeItem.path,
        title:routeItem.meta.title,
        name:routeItem.name
      }); 
      // 设置首个默认显示的tab页面
      store.commit("set_active_index",routeItem.path); // 设置默认打开的tab页面
      next(routeItem.path)
      return
  }
  //如果有token或者在登录页放行
  if (to.name&&( to.name === "login") 
  ) {
    next();
    return;
  }
   if (sessionStorage.getItem("token") || to.name === "login") {
      resetRouter()
      next();
    } else {
      window.console.log("没登陆,被拦截");
      resetRouter()
      next({
        path: "/login",
      });
    }
});
// 根据路由地址获取最后/的字符生成 路由name数据
function getLastIndexStr(str){
  return str.slice(str.lastIndexOf("/")+1)
}
// 根据菜单数据生成 路由信息
function menuGetRoutes(){
  var menuRoutes=sessionStorage.getItem("roleMenus")
  menuRoutes=JSON.parse(menuRoutes)
  var otherMenus=[]
  var noPage=false
  menuRoutes.forEach((item,index)=>{
    if(!(/^\//g.test(item.path))&&!noPage){
      var param={
        path: "/index/iframeView/:type?",
        name: "iframeView",
        component: getRouteCom("admin/iframeView/index.vue"),
        meta: { title: item.title,  icon: item.icon, redirectPath: item.path},
      }
      otherMenus.push(param)
      noPage=true
      return
    }
    var param={
      path: item.path,
      name: getLastIndexStr(item.path)+index,
      component: getRouteCom(item.importStr),
      meta: { title: item.title,  icon: item.icon},
    }
    if(item.children&&item.children.length>0){
      param.children=[]
      item.children.forEach((it,i)=>{
        var childParam={
          path: it.path,
          name: getLastIndexStr(it.path)+i,
          component: getRouteCom(it.importStr),
          meta: { title: it.title,  icon: it.icon},
        }
        param.children.push(childParam)
      })
    }
    otherMenus.push(param)
  })
  return otherMenus
}
// 根据 菜单返回的引用页面地址返回 组件对象
function getRouteCom(path){
  return (reslove) => require(["@/components/"+path],reslove)
}
export default router;
