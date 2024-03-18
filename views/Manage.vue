<template>
  <el-container style="min-height: 100vh;background-color: #F0E9D6">

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" style="padding-bottom: 20px" />
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user" />
      </el-header>

      <el-main>

        <router-view @refreshUser="getUser" />
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 230,
      logoTextShow: true,
      user: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    // get user data
    this.getUser()
  },
  methods: {
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // collapse
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // expand
        this.sideWidth = 230
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // get user data
        this.request.get("/user/username/" + username).then(res => {
          this.user = res.data
        })
      }
    }
  }
}
</script>

