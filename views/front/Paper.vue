<template>
  <el-card style="margin-bottom: 100px">
    <div style="padding: 20px 0"><span style="margin-left: 60px">Resume attachment</span>  <span style="margin-left: 10px">
      <el-upload action="http://localhost:9090/file/upload" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">Upload files <i class="el-icon-top"></i></el-button>
      </el-upload>
    </span></div>
    <el-form inline label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
      <el-form-item prop="papername" label="Resume name">
        <el-input v-model="form.papername" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="Name">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="birth" label="Birth">
        <el-input v-model="form.birth" autocomplete="off"></el-input>
      </el-form-item>
<!--      <el-form-item prop="sex" label="Gender">-->
<!--        <el-input v-model="form.sex" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="workdate" label="Date to work">-->
<!--        <el-date-picker v-model="form.workdate" type="date" value-format="yyyy-MM-dd" placeholder="Select date" style="width: 190px"></el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item prop="phone" label="Phone">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="Email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
<!--      <el-form-item prop="location" label="address">-->
<!--        <el-input v-model="form.location" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item prop="expect" label="Target position">
        <el-input v-model="form.expect" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="expect" label="Skills">
        <el-input v-model="form.skills" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="education" label="Educational background">
        <el-radio v-model="form.education" label="Bachelor's degree">Bachelor's degree</el-radio>
        <el-radio v-model="form.education" label="Master's degree">Master's degree</el-radio>
        <el-radio v-model="form.education" label="PhD">PhD</el-radio>
      </el-form-item>
      <el-form-item prop="years" label="Work experience">
        <el-radio v-model="form.years" label="Less than one year">Less than one year</el-radio>
        <el-radio v-model="form.years" label="1-3 years">1-3 years</el-radio>
        <el-radio v-model="form.years" label="3-5 years">3-5 years</el-radio>
        <el-radio v-model="form.years" label="5 years or more">5 years or more</el-radio>
      </el-form-item>
    </el-form>
    <div style="margin: 20px 0; display: flex">
      <div style="color: #666; width: 120px; text-align: right">Personal profile</div>
      <el-input type="textarea" v-model="form.descr" autocomplete="off" style="width: 65%; margin-left: 10px"></el-input>
    </div>
    <div style="margin: 20px 0; display: flex">
      <div style="color: #666; width: 120px; text-align: right">Experiences
        (work/project)</div>
      <div id="div1" style="width: 65%; margin-left: 10px"></div>
    </div>
    <div style="margin: 20px 0; text-align: center">
      <el-button type="primary" @click="save">Submit</el-button>
    </div>

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
          { required: true, message: 'Please enter your name', trigger: 'blur'}
        ],
        phone: [
          { required: true, message: 'Please enter your phone number', trigger: 'blur'}
        ],
      }
    }
  },
  mounted() {
    this.load()


  },
  methods: {
    load() {
      this.request.get("/paper/user/" + this.user.id).then(res => {
        this.form = res.data.length ? res.data[0] : {}

        this.$nextTick(() => {
          if (editorContext) {
            editorContext.destroy();
          }
          editorContext = new E( document.getElementById('div1') )
          editorContext.config.uploadImgServer = 'http://localhost:9090/file/uploadImg'
          editorContext.config.uploadFileName = 'file'
          editorContext.config.zIndex = 0
          editorContext.create()
          editorContext.txt.html(this.form.content)
        })
      })
    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (editorContext &&  editorContext.txt.html()) {
            this.form.content = editorContext.txt.html()  // get html content
          }
          this.request.post("/paper", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("Save successful")
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {

      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if (editorContext) {
          editorContext.destroy();
        }
        editorContext = new E( document.getElementById('div1') )
        editorContext.config.uploadImgServer = 'http://localhost:9090/file/uploadImg'
        editorContext.config.uploadFileName = 'file'
        editorContext.config.zIndex = 0
        editorContext.create()
        editorContext.txt.html(this.form.content)
      })
    },
    del(id) {
      this.request.delete("/paper/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete successful")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("Please select the data you want to delete")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/paper/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete successful")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.$message.success("Upload successful")
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/paper/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("Import successful")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
