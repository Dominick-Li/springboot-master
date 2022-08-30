
import 'babel-polyfill'
import promise from 'es6-promise'
promise.polyfill()

import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import bus from 'vue-bus'
import axios from 'axios'
import utils from  './utils/utils'


import App from './App'
import router from './router'
import Request from './utils/ajax'
import VueCropper from 'vue-cropper'
import '../statics/css/icon/iconfont.css'
import store from './store/index'
// 引入echarts图表工具
import echarts from 'echarts'

Vue.prototype.$echarts = echarts

Vue.config.productionTip = false
Vue.prototype.$ajax = Request
Vue.prototype.$axios=axios
Vue.prototype.$utils = utils

// 判断是PC端还是移动端
var sUserAgent = navigator.userAgent.toLowerCase();
var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
var bIsMidp = sUserAgent.match(/midp/i) == "midp";
var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
var bIsAndroid = sUserAgent.match(/android/i) == "android";
var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
if ((bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) ){
  // 移动端
  Vue.prototype.isPC = false
}else{
  // PC端
  Vue.prototype.isPC = true
}
// 控制页面是否需要logo
Vue.prototype.isNeedLogo = false

Vue.use(ElementUI)
Vue.use(bus)
Vue.use(VueCropper)
// eslint-disable-next-line no-new
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
