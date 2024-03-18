<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter a name" suffix-icon="el-icon-search" v-model="username"></el-input>
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
        <el-upload action="http://localhost:9090/category/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
          <el-button type="primary" class="ml-5">Import <i class="el-icon-bottom"></i></el-button>
        </el-upload>
        <el-button type="primary" @click="exp" class="ml-5">Export <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="User name" width="140"></el-table-column>
      <el-table-column label="Role">
        <template v-slot="scope">
          <span v-if="scope.row.role">{{ roles.find(v => v.flag === scope.row.role) ? roles.find(v => v.flag === scope.row.role).name : ''  }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="Name" width="120"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column prop="phone" label="Phone"></el-table-column>
<!--      <el-table-column prop="address" label="address"></el-table-column>-->
      <el-table-column label="Operations"  width="240" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete these data？"
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

    <el-dialog title="New User" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="90px" size="small">
        <el-form-item label="User name">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="Address">-->
<!--          <el-input v-model="form.address" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
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
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          role: 'ROLE_USER'
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      this.form.role = 'ROLE_USER'
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save successful")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save failed")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
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
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete successful")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    reset() {
      this.username = ""
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
    exp() {
      window.open("http://localhost:9090/user/export")
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
