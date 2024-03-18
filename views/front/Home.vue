<template>
  <div style="margin-bottom: 100px;" >
    <div style="margin: 10px 0; margin-bottom: 30px">
      <el-carousel height="450px" :interval="10000">
        <el-carousel-item v-for="item in imgs" :key="item">
          <img :src="item" alt="" style="width: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 20px 0 10px 0; font-size: 20px; color: dodgerblue; font-weight: bold">
      All positions
    </div>
    <div style="margin: 10px 0">
      <el-input style="width: 260px" size="small" clearable placeholder="Please enter keywords" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load" size="small">Search</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button v-for="item in categories" :key="item.id" :class="{'active': active === item.name}" @click="loadCategory(item.name)">{{ item.name }}</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-card v-for="item in tableData" :key="item.id" style="margin-bottom: 5px">
        <div @click="$router.push('/front/detail?id=' + item.id)">
          <div style="font-size: 20px; font-weight: bold">{{ item.name }}</div>
          <div style="margin: 10px 0; font-size: 12px; color: #666" v-html="item.descr" class="line2"></div>
          <div style="font-size: 12px; color: #666">
            <span><i class="el-icon-location"></i> {{ item.location }}</span>
            <span style="margin-left: 10px"><i class="el-icon-goods"></i> {{ item.experience }}</span>
            <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.edu }}</span>
          </div>
        </div>
        <div style="margin-top: 10px">
          <el-button type="primary" v-if="item.hasApply" disabled>Applied</el-button>
          <el-button type="primary" v-else @click="handleApply(item)">Apply</el-button>
          <el-button type="success" v-if="item.hasCollect" disabled>Bookmarked</el-button>
          <el-button type="success" v-else @click="collect(item)">Bookmark</el-button>
        </div>
      </el-card>
    </div>

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

    <el-dialog title="Select resume" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="paperid" label="Resume">
          <el-select clearable v-model="paperid" placeholder="Please select"  style="width: 100%">
            <el-option v-for="item in papers" :key="item.id" :label="item.papername" :value="item.id"></el-option>
          </el-select>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="apply">Submit</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Job",
  data() {
    return {
      imgs: [
          require('../../assets/cover2.jpg'),
          require('../../assets/img_1.png'),
      ],
      users: [],
      tableData: [],
      recommend: [],
      papers: [],
      categories: [],
      active: '',
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      job: "",
      paperid: null,
      form: {},
      row: {},
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
    handleApply(row) {
      this.dialogFormVisible = true
      this.row = row
    },
    apply() {
      this.request.post("/applys", { jobid: this.row.id, name: this.row.name, managerid: this.row.managerid, paperid: this.paperid }).then(res => {
        if (res.code === '200') {
          this.$message.success("Apply successful")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    collect(row) {
      this.request.post("/collect", { jobid: row.id, name: row.name, link: '/front/detail?id=' + row.id }).then(res => {
        if (res.code === '200') {
          this.$message.success("Bookmark saved")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadCategory(name) {
      this.active = name
      this.load()
    },
    load() {
      this.request.get("/job/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          category: this.active
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/category").then(res => {
        this.categories = res.data
      })

      this.request.get("/paper/user/" + this.user.id).then(res => {
        this.papers = res.data
      })

      this.request.get("/user").then(res => {
        this.users = res.data.filter(v => v.role === 'ROLE_USER')
      })

      // this.request.get("/job/recommend").then(res => {
      //   this.recommend = res.data?.splice(0, 3)
      // })

    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.request.post("/job", this.form).then(res => {
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
      this.request.delete("/job/" + id).then(res => {
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
      this.request.post("/job/del/batch", ids).then(res => {
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
      window.open("http://localhost:9090/job/export")
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
.line2 {
  overflow : hidden;
  text-overflow: ellipsis;
  display: -webkit-box; -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.active {
  background-color: #1E90FF;
  color: white;
}


</style>


