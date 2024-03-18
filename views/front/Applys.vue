<template>
  <el-card>

    <el-table :data="tableData" stripe >
      <el-table-column prop="id" label="ID" width="60" sortable></el-table-column>
      <el-table-column prop="name" label="Position name"></el-table-column>
      <el-table-column prop="link" label="Position link">
        <template v-slot="scope">
          <a :href="'/front/detail?id=' + scope.row.jobid" style="color: #1E90FF">Click to view</a>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="Application time"></el-table-column>
      <el-table-column prop="status" label="Review status"></el-table-column>
      <el-table-column prop="reason" label="Interview notification"></el-table-column>
      <el-table-column label="Operations"  width="120" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              v-if="scope.row.status === 'Under review'"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to revoke?"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Revoke <i class="el-icon-remove-outline"></i></el-button>
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

<!--    <el-dialog title="Information" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">-->
<!--      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">-->
<!--        <el-form-item prop="name" label="name">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="jobid" label="job id">-->
<!--          <el-input v-model="form.jobid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="userid" label="userid">-->
<!--          <el-input v-model="form.userid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="time" label="time">-->
<!--          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="select date"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="paperid" label="resume id">-->
<!--          <el-input v-model="form.paperid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="status" label="review status">-->
<!--          <el-input v-model="form.status" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="managerid" label="manager id">-->
<!--          <el-input v-model="form.managerid" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">cancel</el-button>-->
<!--        <el-button type="primary" @click="save">submit</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </el-card>
</template>

<script>
export default {
  name: "Applys",
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
    load() {
      this.request.get("/applys/page", {
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
            this.request.post("/applys", this.form).then(res => {
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
      this.request.delete("/applys/" + id).then(res => {
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
        this.$message.error("Please select the data to delete")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/applys/del/batch", ids).then(res => {
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
      window.open("http://localhost:9090/applys/export")
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
