<template>
  <el-card style="margin-bottom: 100px">
    <el-form inline label-width="140px" size="small" style="width: 85%;" ref="ruleForm">
      <el-form-item prop="papername" label="Resume name">
        <div class="items">{{form.papername }}</div>
      </el-form-item>
      <el-form-item prop="name" label="Name">
        <div class="items">{{form.name }}</div>
      </el-form-item>
      <el-form-item prop="birth" label="Birth date">
        <div class="items">{{form.birth }}</div>
      </el-form-item>
<!--      <el-form-item prop="sex" label="gender">-->
<!--        <div class="items">{{form.sex }}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="workdate" label="workdate">-->
<!--        <div class="items">{{form.workdate }}</div>-->
<!--      </el-form-item>-->
      <el-form-item prop="phone" label="Phone">
        <div class="items">{{form.phone }}</div>
      </el-form-item>
      <el-form-item prop="email" label="Email">
        <div class="items">{{form.email }}</div>
      </el-form-item>
<!--      <el-form-item prop="location" label="address">-->
<!--        <div class="items">{{form.location }}</div>-->
<!--      </el-form-item>-->
      <el-form-item prop="expect" label="Target position">
        <div class="items">{{form.expect }}</div>
      </el-form-item>
      <el-form-item prop="skills" label="Skills">
        <div class="items">{{form.skills }}</div>
      </el-form-item>
      <el-form-item prop="education" label="Educational background" >
        <div class="items">{{form.education }}</div>
      </el-form-item>
      <el-form-item prop="years" label="Work experience">
        <div class="items">{{form.years }}</div>
      </el-form-item>

    </el-form>
    <div style="margin: 10px 0; display: flex">
      <div style="color: #666; width: 140px; text-align: right">Personal profile:</div>
      <div class="items" style="margin-left: 10px">{{form.descr }}</div>
    </div>
    <div style="margin: 20px 0; display: flex">
      <div style="color: #666; width: 120px; text-align: right">Experiences(work/project):</div>
      <div v-html="form.content" style="margin-left: 90px"></div>
    </div>

    <el-button type="success">
      <router-link to="/applys">Back</router-link>
    </el-button>
  </el-card>
</template>

<script>
import E from 'wangeditor'
let editorContext;

export default {
  name: "Paper",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        papername: [
          { required: true, message: 'Please enter resume name', trigger: 'blur'}
        ],
        name: [
          { required: true, message: 'Please enter name', trigger: 'blur'}
        ],
        phone: [
          { required: true, message: 'Please enter name', trigger: 'blur'}
        ],
      }
    }
  },
  mounted() {
    this.load()


  },
  methods: {
    load() {
      this.request.get("/paper/" + this.$route.query.id).then(res => {
        this.form = res.data
      })
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.items {
  color: #333333;
  font-weight: bold;
}
</style>
