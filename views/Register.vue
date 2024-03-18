<template>
  <div class="wrapper">
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">XXXX System</div>
    <div style="margin: 150px auto; background-color: #fff; width: 400px; height: 450px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="Please enter your account" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="Please enter your password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="Please confirm your password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="user.role">
            <el-radio label="ROLE_USER">Candidate</el-radio>
            <el-radio label="ROLE_MANAGER">Admin</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">Back</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">Sign Up</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {role: 'ROLE_USER'},
      rules: {
        username: [
          { required: true, message: 'Please enter username', trigger: 'blur' },
          { min: 3, max: 10, message: 'Length between 3 and 5 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length between 1 and 20 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm password', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length between 1 and 20 characters', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("Passwords do not match")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("Registration successful")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
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
