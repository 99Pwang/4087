<template>
  <el-card>
    <div style="margin-bottom: 10px"><el-button size="small" type="primary" @click="handleApply">One-click apply</el-button></div>
    <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="Position name"></el-table-column>
      <el-table-column prop="link" label="Link">
        <template v-slot="scope">
          <a :href="scope.row.link" style="color: #1E90FF">Click to view</a>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="Bookmark time"></el-table-column>

      <el-table-column label="Operations"  width="205" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to cancel the bookmark？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Cancel the bookmark<i class="el-icon-remove-outline"></i></el-button>
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

<!--    <el-dialog title="information" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">-->
<!--      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">-->
<!--        <el-form-item prop="name" label="name">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="jobid" label="job id">-->
<!--          <el-input v-model="form.jobid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="link" label="link">-->
<!--          <el-input v-model="form.link" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="date" label="date">-->
<!--          <el-date-picker v-model="form.date" type="date" value-format="yyyy-MM-dd" placeholder="select date"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="userid" label="user id">-->
<!--          <el-input v-model="form.userid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">cancel</el-button>-->
<!--        <el-button type="primary" @click="save">submit</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

    <el-dialog title="Select resume" :visible.sync="dialogFormVisible1" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="paperid" label="Resume">
          <el-select clearable v-model="paperid" placeholder="Please select"  style="width: 100%">
            <el-option v-for="item in papers" :key="item.id" :label="item.papername" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">Cancel</el-button>
        <el-button type="primary" @click="apply">Submit</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  name: "Collect",
  data() {
    return {
      tableData: [],
      papers: [],
      paperid: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      dialogFormVisible1: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: 'Please enter a name', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleApply() {
      if (!this.multipleSelection.length) {
        this.$message.warning("Please select the position")
        return
      }
      this.dialogFormVisible1 = true
    },
    load() {
      this.request.get("/collect/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/paper/user/" + this.user.id).then(res => {
        this.papers = res.data
      })
    },
    save() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.request.post("/collect", this.form).then(res => {
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
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/collect/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete successful")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    apply() {
      if (this.multipleSelection.length) {
        this.multipleSelection.forEach(v => {
          this.request.post("/applys", { jobid: v.id, name: v.name, managerid: v.managerid, paperid: this.paperid }).then(res => {
            if (res.code === '200') {
              this.$message.success("Apply successful")
              this.dialogFormVisible1 = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        })
      }

    },

    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("Please select the data you want to delete")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/collect/del/batch", ids).then(res => {
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
      window.open("http://localhost:9090/collect/export")
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
