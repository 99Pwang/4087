<template>
  <div class="backg" style="background-color: #eee;position: relative;">
<!--    header-->

    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 2px solid #C3BFBF; ">
      <div style="width: 400px; display: flex; padding-left: 30px">
        <div style="width: 50px">
          <img src="../../assets/logo1.png" alt="" style="width: 40px; position: relative; top: 8px; right: 0">
        </div>
        <div style="flex: 1; color: #1E90FF; font-weight: bold">Welcome to XXXX System</div>
      </div>

      <div class="rectangle1" style="position: absolute; top: 59px; left: 93px;"></div>
      <div class="rectangle2" style="position: absolute; top: 59px; left: 150px;"></div>
      <div class="rectangle3" style="position: absolute; top: 59px; left: 207px;"></div>
      <div class="rectangle4" style="position: absolute; top: 99px; left: 93px;"></div>
      <div class="rectangle5" style="position: absolute; top: 99px; left: 150px;"></div>
      <div class="rectangle6" style="position: absolute; top: 99px; left: 207px;"></div>

      <div style="flex: 1;">

        <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" router style="border: none; height: 100%;background-color: #eee ">
          <el-menu-item index="/front/home">Home</el-menu-item>
          <el-menu-item index="/front/applys">My applications</el-menu-item>
          <el-menu-item index="/front/collect">My bookmarks</el-menu-item>
          <el-menu-item index="/front/person">Profile</el-menu-item>
          <el-menu-item index="/front/paper">My resumes</el-menu-item>
        </el-menu>
      </div>
      <div style="width: 150px; text-align: right; padding-right: 10px;background-color: #333333;border-radius: 55px;">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">Log in</el-button>
          <el-button @click="$router.push('/register')">Register</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right;color: white">
            <div style="display: inline-block;margin-right: 22px">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 9px; right: 8px">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 150px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password"><div>Password change</div></router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person"><div>Personal information</div></router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout"><div>Log out</div></span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width: 60%; margin: 5px auto">
      <router-view  @refreshUser="getUser" style="min-height: calc(100vh - 140px)" />
    </div>
    <div style="margin-top: 20px; border-top: 1px solid #ccc; background-color: #fcfcfc">
      <div style="text-align: center; margin: 10px 0; color: #666; font-size: 14px">by Peilongyu Wang / COSC 4087</div>

    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  },
  methods: {
    logout() {
      this.$store.commit("logout")
      this.$message.success("Logout successful")
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // get User data
        this.request.get("/user/username/" + username).then(res => {
          this.user = res.data
        })
      }
    }
  }
}
</script>

<style>
.item{
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer
}
.item a {
  color: 	#1E90FF;
}
.item:hover{
  background-color: 	LightPink;
}
.rectangle1 {
  background-color: #5C7F70;
  width: 50px;
  height: 10px;
  margin-bottom: 10px;
}
.rectangle2 {
  background-color: #BB852F;
  width: 50px;
  height: 10px;
  margin-bottom: 10px;
}
.rectangle3 {
  background-color: #802520;
  width: 50px;
  height: 10px;
  margin-bottom: 10px;
}
.rectangle4 {
  background: linear-gradient(to bottom, rgba(92,127,112,0) 0%, #5C7F70 50%, rgba(92,127,112,0) 100%);
  width: 50px;
  height: 300px;
  position: absolute;
  top: 30px;
  left: 0;
}
.rectangle5 {
  background: linear-gradient(to bottom, rgba(187,133,47,0) 0%, #BB852F 50%, rgba(187,133,47,0) 100%);
  width: 50px;
  height: 500px;
  position: absolute;
  top: 30px;
  left: 0;
}
.rectangle6 {
  background: linear-gradient(to bottom, rgba(128,37,32,0) 0%, #802520 50%, rgba(128,37,32,0) 100%);
  width: 50px;
  height: 700px;
  position: absolute;
  top: 30px;
  left: 0;
}

</style>
