<template>
  <div class="wrapper">
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">XXXX System</div>

    <div class="wrapper2" style="height: 90vh;display: flex;align-items: center;justify-content: center;">
    <div style="display: flex;background-color: #F3ECDB;width: 48%;;overflow: hidden;box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1), 0 6px 20px rgba(0, 0, 0, 0.1);">
    <div class="login-image" style="flex: 1">
      <img src="../assets/cover.jpg" alt="" style="width: 100%">
    </div>


    <div class="login-form" style="flex: 1;">
    <div style=" background-color: #F3ECDB; width: 400px; height: 390px;  border-radius: 10px;padding-left: 33px;padding-top: 80px;padding-right: 20px">
      <div style="margin: 20px 0; text-align: center">
        <img src="../assets/login1.png" alt="Log in" style="width: 200px;margin-bottom: 20px">
      </div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item>
          <div style="display: flex">
            <el-input size="mid" v-model="code" style="width: 200px"></el-input>
            <span @click="refreshCode" style="cursor: pointer; flex: 1;">
              <Identify :identifyCode="identifyCode"></Identify>
           </span>
          </div>
        </el-form-item>


        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">Sign Up</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">Start</el-button>

        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="text" size="mid"  autocomplete="off" @click="handlePass">Forgot Password?</el-button>
        </el-form-item>
      </el-form>
    </div>
    </div>
    </div>
    </div>


    <el-dialog title="Password Reset" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="120px" size="small">
        <el-form-item label="User Name">
          <el-input v-model="pass.username" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="Phone Number">
          <el-input v-model="pass.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="passwordBack">Reset Password</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {resetRouter, setRoutes} from "@/router";
import Identify from "@/components/Identify";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      pass: {},
      code: '',
      dialogFormVisible: false,
      // verification code
      identifyCode: '',
      // rules
      identifyCodes: '3456789ABCDEFGHGKMNPQRSTUVWXY',
      rules: {
        username: [
          { required: true, message: 'Please enter username', trigger: 'blur' },
          { min: 3, max: 10, message: 'Length between 3 and 10 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length between 1 and 20 characters', trigger: 'blur' }
        ],
      }
    }
  },
  components: {Identify},
  mounted() {
    // reset router
    resetRouter()
    this.refreshCode()
  },
  methods: {
    login() {
      // if (this.code !== this.identifyCode.toLowerCase()) {
      //   this.$message({
      //     type: "error",
      //     message: "code wrong"
      //   })
      //   return;
      // }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // valid
          this.request.post("/user/login", this.user).then(res => {
            if(res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // save user information to browser
              localStorage.setItem("menus", JSON.stringify(res.data.menus))

              // set user's router
              setRoutes()
              if (res.data.role === 'ROLE_USER') {
                this.$router.push("/front/home")
                this.$message.success("Login successful")
              } else {
                this.$router.push("/")
                this.$message.success("Login successful")
              }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },

    refreshCode() {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    // random code
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length))]
      }
    },
    handlePass() {
      this.dialogFormVisible = true
      this.pass = {}
    },
    passwordBack() {
      this.request.put("/user/reset", this.pass).then(res => {
        if (res.code === '200') {
          this.$message.success("Password reset successful. Your new password is: 123. Please change your password as soon as possible")
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: url("../assets/bg.jpg");
  background-size: cover;
  overflow: hidden;
}



</style>
