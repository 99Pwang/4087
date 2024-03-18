<template>
  <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
           :default-active="$route.path"
           :style="{ background: menuBackgroundGradient }"
           text-color="#fff"
           active-text-color="DodgerBlue"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; display: flex; align-items: center; justify-content: center; text-align: center;margin-top: 10px">
      <img src="../assets/logo2.png" alt="" style="width: 60px;">
      <b style="color: #fff; margin-left: 5px; margin-bottom: 0px;" v-show="logoTextShow">XXXX System</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>

          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div  v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>

      </div>
      <div class="color-block bottom-left"></div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : [],
      menuBackgroundGradient: 'linear-gradient(to right, #7B4325, #E87E37)'
    }
  },
}
</script>

<style scoped>



.el-menu--collapse span {
  visibility: hidden;
}
.el-menu-item:hover,
.el-submenu__title:hover {
  color: white !important;
  background-color: #815138 !important;
}
.el-menu-item.is-active {
  color: white !important;
  background-color: #313131 !important;
}
.color-block {
  position: fixed;
  width: 62px;
  height: 10px;
}


.bottom-left {
  bottom: 150px;
  left: 0;
  background-image: linear-gradient(to right, #7DB3A3 33%, #DB9E37 33%, #DB9E37 66%, #982321 66%);
}
</style>
