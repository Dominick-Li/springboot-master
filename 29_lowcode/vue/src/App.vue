<template>
  <div id="app" >
      <router-view style="width: 100%;height: 100%;"></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',

  computed: {
    // key() {
    // return this.$route.name !== undefined? this.$route.name +new Date(): this.$route +new Date()
    // }
  },
  mounted(){
    // 移动端  竖屏强制转换为横屏，本身是横屏则不转换
    // widnow  onorientationchange 屏幕旋转事件
    // window.addEventListener(
    //   "onorientationchange" in window ? "orientationchange" : "resize",
    //   function() {
    //     if (window.orientation === 90 || window.orientation === -90) {   
    //       //
    //     }else{

    //     }
    //   },
    //   false
    //   );
  var width = document.documentElement.clientWidth || window.innerWidth,
      height = document.documentElement.clientHeight || window.innerHeight,
      $wrapper = document.getElementById("app")
    if(!this.isPC){
      console.log('plus')
      // alert(plus.device.model)
      // plus.screen.lockOrientation("landscape-primary")
      // var style = "";
      // if (width >= height) { // 横屏 不用转换
      //   style += "width:" + width + "px;";  // 注意旋转后的宽高切换
      //   style += "height:" + height + "px;";
      //   style += "-webkit-transform: rotate(0); transform: rotate(0);";
      //   style += "-webkit-transform-origin: " + width / 2 + "px " + width / 2 + "px;";
      //   style += "transform-origin: " + width / 2 + "px " + width / 2 + "px;";
      // }
      // else { // 竖屏 转为横屏
      //   style += "width:" + height + "px;";
      //   style += "height:" + width + "px;";
      //   style += "-webkit-transform: rotate(90deg); transform: rotate(90deg);";
      //   // 注意旋转中点的处理
      //   style += "-webkit-transform-origin: " + width / 2 + "px " + width / 2 + "px;";
      //   style += "transform-origin: " + width / 2 + "px " + width / 2 + "px;";
      // }
      $($wrapper).css('height',height+"px")
    }else{
      $($wrapper).addClass("minWidthApp")
    }
  },
  // watch:{
  //   '$route'(to,from){
  //     console.log(to,from)
  //   }
  // }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 100%;
  height: 100%;
}
.el-table {
    position: absolute;
    top: 0;
}
.el-table__header{
    width: 100% !important;
}
.el-table__body{
    width: 100% !important;
}
::-webkit-scrollbar {
  width: 7px;
  height: 10px;
}

::-webkit-scrollbar-thumb {
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0px 1px rgba(0, 0, 0, 0.2);
  background: #b3b3b3;
}

::-webkit-scrollbar-track {
  border-radius: 20px;
  -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0.2);
  border-radius: 1px;
  background: #dbdcdc;
}

.minWidthApp{
  min-width: 1280px;
  overflow: auto;
}

  @media (max-width: 768px){
      #app{
          font-size: 12px!important;
      }
  }
  @media (min-width: 768px){
      #app{
          font-size: 13px!important;
      }
  }
  @media (min-width: 992px) { 
      #app{
          font-size: 14px!important;
      }
  }
  @media (min-width: 1200px){
      #app{
          font-size: 15px!important;
      }
  }
  @media (min-width: 1920px){
      #app{
          font-size: 16px!important;
      }
  }
</style>
