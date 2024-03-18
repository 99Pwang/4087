<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter a name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <!--      <el-input style="width: 200px" placeholder="please enter" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
      <!--      <el-input style="width: 200px" placeholder="please enter" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Clear</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Yes'
          cancel-button-text='No'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete these data？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <!-- <el-upload action="http://localhost:9090/paper/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">import <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">export <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="papername" label="Resume name"></el-table-column>
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="birth" label="Birth"></el-table-column>
<!--      <el-table-column prop="sex" label="gender"></el-table-column>-->
<!--      <el-table-column prop="workdate" label="workdate"></el-table-column>-->
      <el-table-column prop="phone" label="Phone"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
<!--      <el-table-column prop="location" label="Address"></el-table-column>-->
      <el-table-column prop="descr" label="Personal profile" width="135"></el-table-column>
      <el-table-column prop="expect" label="Target position" width="130"></el-table-column>
      <el-table-column prop="education" label="Educational background" width="195"></el-table-column>
      <el-table-column prop="years" label="Work experience" width="140"></el-table-column>
      <el-table-column prop="skills" label="Skills" ></el-table-column>
      <el-table-column prop="content" width="140" label="Experiences(work/project)" >
        <template v-slot="scope">
          <el-button type="primary" @click="view(scope.row.content)">Click to view</el-button>
        </template>
      </el-table-column>

      <el-table-column label="Operations"  width="220" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="New resume" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false">
      <el-form inline label-width="180px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="papername" label="Resume name">
          <el-input v-model="form.papername" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="birth" label="Birth">
          <el-input v-model="form.birth" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item prop="sex" label="gender">-->
<!--          <el-input v-model="form.sex" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="workdate" label="workdate">-->
<!--          <el-date-picker v-model="form.workdate" type="date" value-format="yyyy-MM-dd" placeholder="select date" style="width: 200px"></el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item prop="phone" label="Phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="Email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item prop="location" label="Address">-->
<!--          <el-input v-model="form.location" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="descr" label="Personal profile">
          <el-input v-model="form.descr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="expect" label="Target position">
          <el-input v-model="form.expect" autocomplete="off"></el-input>
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
      <div style="padding: 0 50px">
        <div style="margin-bottom: 10px">Experiences (work/project)</div>
        <div id="div1"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Content" :visible.sync="dialogFormVisible1" width="60%" :close-on-click-modal="false">
      <el-card>
        <div v-html="content"></div>
      </el-card>
    </el-dialog>
  </div>
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
      dialogFormVisible1: false,
      content: '',
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        papername: [
          { required: true, message: 'Please enter resume name', trigger: 'blur'}
        ],
        name: [
          { required: true, message: 'Please enter name', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    view(data){
      this.content = data
      this.dialogFormVisible1 = true;
    },
    load() {
      this.request.get("/paper/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
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
        if (editorContext) {
          editorContext.destroy();
        }
        editorContext = new E( document.getElementById('div1') )
        editorContext.config.uploadImgServer = 'http://localhost:9090/file/uploadImg'
        editorContext.config.uploadFileName = 'file'
        editorContext.config.zIndex = 0
        editorContext.create()
        editorContext.txt.html('')
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
