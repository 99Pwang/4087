<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter a name" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="please enter email" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="please enter address" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Clear</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd(null)">Add <i class="el-icon-circle-plus-outline"></i></el-button>
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
<!--      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">import <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exp" class="ml-5">export <i class="el-icon-top"></i></el-button>-->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="Menu name"></el-table-column>
      <el-table-column prop="path" label="Path"></el-table-column>
      <el-table-column prop="pagePath" label="URL Path"></el-table-column>
      <el-table-column label="Icon" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="Description"></el-table-column>
      <el-table-column prop="sortNum" label="Sequence"></el-table-column>
      <el-table-column label="Operations"  width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">Add sub-menu <i class="el-icon-plus"></i></el-button>
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

    <el-dialog title="New menu" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="Menu name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Path">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="URL Path">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Icon">
          <el-select clearable v-model="form.icon" placeholder="Please select" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Sequence">
          <el-input v-model="form.sortNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
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
      options: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/menu", {
        params: {
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data
      })

      // request icons data
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    save() {
      this.request.post("/menu", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save successful")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save failed")
        }
      })
    },
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/menu/" + id).then(res => {
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
      this.request.post("/menu/del/batch", ids).then(res => {
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
    exp() {
      window.open("http://localhost:9090/role/export")
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
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>
