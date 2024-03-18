<template>
  <div>
    <div style="margin: 10px 0">
      <el-select v-model="value" placeholder="Please select a position">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
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
          title="Are you sure you want to delete these data?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-button type="primary" @click="exp" class="ml-5">Export <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="Position name" width="125"></el-table-column>
      <el-table-column prop="time" label="Application date" width="140"></el-table-column>
      <el-table-column prop="paperid" label="Resume">
        <template v-slot="scope">
          <a :href="'/paperDetail?id=' + scope.row.paperid" target="_blank" style="color: #1E90FF">Click to view</a>
        </template>
      </el-table-column>
      <el-table-column prop="paper.education" label="Education background" width="180"></el-table-column>
      <el-table-column prop="paper.years" label="Work experience" width="140"></el-table-column>
      <el-table-column prop="file" label="Resume attachment" width="165">
        <template v-slot="scope">
          <a :href="scope.row.file" v-if="scope.row.file" target="_blank" style="color: #1E90FF">Download</a>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="Review status" width="120"></el-table-column>
      <el-table-column prop="managerid" label="Publisher id" width="105"></el-table-column>
      <el-table-column prop="reason" label="Interview invitation" width="165"></el-table-column>
      <el-table-column prop="scores" label="Score"></el-table-column>

      <el-table-column label="Review"  width="230" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="changeStatus(scope.row, 'Approved')" v-if="scope.row.status === 'Under review'">Approved</el-button>
          <el-button type="danger" @click="changeStatus(scope.row, 'Rejected')"  v-if="scope.row.status === 'Under review'">Rejected</el-button>
        </template>
      </el-table-column>
      <el-table-column label="Operations"  width="310" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="scope.row.status === 'Approved'">Schedule an interview</el-button>
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

    <el-dialog title="Add" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="180px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="reason" label="Interview invitation">
          <el-input type="textarea" v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Add" :visible.sync="dialogFormVisible1" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="scores" label="Score">
          <el-input v-model="form.scores" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>
  </div>
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
      dialogFormVisible1: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: 'Please enter a name', trigger: 'blur'}
        ],
      },
      value: '',
      options: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeStatus(row, status) {
      row.status = status
      this.request.post("/applys", row).then(res => {
        if (res.code === '200') {
          this.$message.success("Review successful")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load() {
      this.request.get("/applys/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.value,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/job/all").then(res => {
        this.options = res.data
      })
    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.request.post("/applys", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("Save successful")
              this.dialogFormVisible = false
              this.dialogFormVisible1 = false
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
    handleEdit2(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible1 = true
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
        this.$message.error("Please select the data you want to delete")
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
