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
          title="Are you sure you want to delete these data?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <!-- <el-upload action="http://localhost:9090/job/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">import <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">export <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="Position Name" width="125"></el-table-column>
      <el-table-column prop="experience" label="Experience required" width="165"></el-table-column>
      <el-table-column prop="location" label="Location" width="85"></el-table-column>
      <el-table-column prop="edu" label="Minimum educational qualification" width="269"></el-table-column>
      <el-table-column prop="descr" label="Position description" show-overflow-tooltip width="170"></el-table-column>
      <el-table-column prop="price" label="Wages range" width="115"></el-table-column>
      <el-table-column prop="date" label="Application deadline" width="180"></el-table-column>
      <el-table-column prop="manager" label="Person in charge" width="140"></el-table-column>
<!--      <el-table-column prop="phone" label="Phone"></el-table-column>-->
      <el-table-column prop="time" label="Publication date" width="140"></el-table-column>
      <el-table-column prop="status" label="Recruitment status" width="160"></el-table-column>
      <el-table-column prop="category" label="Category" width="105"></el-table-column>
      <el-table-column prop="tags" label="Keywords" width="105"></el-table-column>
      <el-table-column prop="skills" label="Skills required" width="125"></el-table-column>
<!--      <el-table-column prop="salary" label="wage"></el-table-column>-->
<!--      <el-table-column prop="highlights" label="position highlight"></el-table-column>-->
      <el-table-column prop="type" label="Position type" width="115"></el-table-column>
      <el-table-column prop="ways" label="Mode of position" width="145"></el-table-column>
      <el-table-column prop="time2" label="Hours of work" width="125"></el-table-column>
      <el-table-column prop="welfare" label="Package benefits" width="145"></el-table-column>
      <el-table-column prop="recommend" label="Recommendation requirement" width="240"></el-table-column>

      <el-table-column label="Operations"  width="220" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete these data?"
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

    <el-dialog title="New Position" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="260px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="name" label="Position Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="experience" label="Experience required">
          <el-input v-model="form.experience" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="location" label="Location">
          <el-input v-model="form.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="edu" label="Minimum educational qualification">
          <el-input v-model="form.edu" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="descr" label="Position description">
          <el-input type="textarea" :row="5" v-model="form.descr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="price" label="Wages range">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="date" label="Application deadline">
          <el-date-picker v-model="form.date" type="date" value-format="yyyy-MM-dd" placeholder="Please select a date"></el-date-picker>
        </el-form-item>
        <el-form-item prop="status" label="Recruitment status">
          <el-radio label="Recruiting" v-model="form.status">Recruiting</el-radio>
          <el-radio label="Recruitment closed" v-model="form.status">Recruitment closed</el-radio>
        </el-form-item>
        <el-form-item prop="category" label="Category">
          <el-select clearable v-model="form.category" placeholder="Please select"  style="width: 100%">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="tags" label="Keywords（comma separate）">
          <el-input v-model="form.tags" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="skills" label="Skills required（comma separate）">
          <el-input v-model="form.skills" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item prop="salary" label="wage">-->
<!--          <el-input v-model="form.salary" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="highlights" label="position highlight">-->
<!--          <el-input v-model="form.highlights" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="type" label="Position type">
          <el-radio v-model="form.type" label="Full time">Full time</el-radio>
          <el-radio v-model="form.type" label="Part-time">Part-time</el-radio>
          <el-radio v-model="form.type" label="Contract">Contract</el-radio>
        </el-form-item>
        <el-form-item prop="ways" label="Mode of position">
          <el-radio v-model="form.ways" label="hybrid">hybrid</el-radio>
          <el-radio v-model="form.ways" label="On-site">On-site</el-radio>
        </el-form-item>
        <el-form-item prop="time2" label="Hours of work">
          <el-input v-model="form.time2" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="welfare" label="Package benefits">
          <el-input type="textarea" v-model="form.welfare" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="recommend" label="Recommendation requirement">
          <el-input  type="textarea" v-model="form.recommend" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Job",
  data() {
    return {
      tableData: [],
      categories: [],
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
      this.request.get("/job/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/category").then(res => {
        this.categories = res.data
      })
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
        this.$message.error("Please select the data to delete")
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

</style>
