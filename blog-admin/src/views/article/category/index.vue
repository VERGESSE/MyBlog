<template>
  <div class="category-container shadow">
    <el-row>
      <el-col :span="12">
        <div class="grid-content">
          <div style="color: #303133;font-size: 20px;text-align: center;font-weight: bolder;margin-bottom: 10px">
            博文分类列表
          </div>
          <el-table :data="categoryList.list" border style="width: 100%">
            <el-table-column label="分类ID" width="66" align="center">
              <template slot-scope="scope">
                {{ scope.row.id }}
              </template>
            </el-table-column>
            <el-table-column label="分类名称" min-width="95" align="center">
              <template slot-scope="scope">
                {{ scope.row.name }}
              </template>
            </el-table-column>
            <el-table-column label="文章数量" width="80" align="center">
              <template slot-scope="scope">
                {{ scope.row.number }}
              </template>
            </el-table-column>
            <el-table-column label="分类图标" min-width="240" align="center">
              <template slot-scope="scope">
                <el-link
                  type="info"
                  :href="scope.row.pictureUrl"
                  style="font-size: 3px"
                  target="_blank"
                >
                  {{ scope.row.pictureUrl.split("/").slice(3).join("/") }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="158">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="info"
                  plain
                  round
                  @click="categoryEdit(scope.row)"
                >编辑</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  plain
                  round
                  @click="categoryDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pagination"
            hide-on-single-page="true"
            :current-page.sync="categoryList.pageNum"
            :page-size="categoryList.pageSize"
            layout="prev, pager, next"
            :total="categoryList.total"
            :page-count="categoryList.pages"
            @current-change="fetchDataC"
          />
          <!-- 修改对话框 -->
          <el-dialog title="分类信息修改" :visible.sync="editCategory">
            <el-form ref="updateCategory" :model="updateCategory" :rules="rules">
              <el-form-item label="分类名称" prop="name" label-width="100px">
                <el-input v-model="updateCategory.name" />
              </el-form-item>
              <el-form-item label="分类图标" prop="pictureUrl" label-width="100px">
                <el-input v-model="updateCategory.pictureUrl" />
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="editCategory = false">取 消</el-button>
              <el-button type="primary" @click="putNewCategory('updateCategory')">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content">
          <div style="color: #303133;font-size: 20px;text-align: center;font-weight: bolder;margin-bottom: 10px">
            技术栈展示列表
          </div>
          <el-table :data="technologyList.list" border style="width: 100%;margin-left: 10px">
            <el-table-column label="技术名称" min-width="80" align="center">
              <template slot-scope="scope">
                {{ scope.row.name }}
              </template>
            </el-table-column>
            <el-table-column label="图标" min-width="220" align="center">
              <template slot-scope="scope">
                <el-link
                  type="info"
                  :href="scope.row.pictureUrl"
                  style="font-size: 3px"
                  target="_blank"
                >
                  {{ scope.row.pictureUrl.split("/").slice(3).join("/") }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="158">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="info"
                  plain
                  round
                  @click="technologyEdit(scope.row)"
                >编辑</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  plain
                  round
                  @click="technologyDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pagination"
            hide-on-single-page="true"
            :current-page.sync="technologyList.pageNum"
            :page-size="technologyList.pageSize"
            layout="prev, pager, next"
            :total="technologyList.total"
            :page-count="technologyList.pages"
            @current-change="fetchDataT"
          />
          <!-- 修改对话框 -->
          <el-dialog title="技术栈信息修改" :visible.sync="editTechnology">
            <el-form ref="updateTechnology" :model="updateTechnology" :rules="rules">
              <el-form-item label="技术栈名称" prop="name" label-width="100px">
                <el-input v-model="updateTechnology.name" />
              </el-form-item>
              <el-form-item label="图标地址" prop="pictureUrl" label-width="100px">
                <el-input v-model="updateTechnology.pictureUrl" />
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="editTechnology = false">取 消</el-button>
              <el-button type="primary" @click="putNewTechnology('updateTechnology')">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </el-col>
    </el-row>
    <el-divider />
    <el-row>
      <el-col :span="12">
        <div class="grid-content">
          <div class="bottom-container shadow">
            <el-form ref="newCategory" :model="newCategory" :rules="rules" label-width="100px" style="margin: 6%">
              <span style="color: #909399;font-size: 25px;margin-left: 20px">新增分类</span>
              <el-divider style="margin-left: 20px" />
              <el-form-item label="分类名称" prop="name">
                <el-input v-model="newCategory.name" />
              </el-form-item>
              <el-form-item label="分类图标" prop="pictureUrl">
                <el-input v-model="newCategory.pictureUrl" />
              </el-form-item>
              <el-form-item>
                <el-button type="success" plain round @click="addCategory('newCategory')">创建分类</el-button>
                <el-button type="warning" plain round @click="resetForm('newCategory')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content">
          <div class="bottom-container shadow">
            <el-form ref="newTechnology" :model="newTechnology" :rules="rules" label-width="100px" style="margin: 6%">
              <span style="color: #909399;font-size: 25px;margin-left: 20px">新增技术栈</span>
              <el-divider style="margin-left: 20px" />
              <el-form-item label="技术栈名称" prop="name">
                <el-input v-model="newTechnology.name" />
              </el-form-item>
              <el-form-item label="技术栈图标" prop="pictureUrl">
                <el-input v-model="newTechnology.pictureUrl" />
              </el-form-item>
              <el-form-item>
                <el-button type="success" plain round @click="addTechnology('newTechnology')">新增技术栈</el-button>
                <el-button type="warning" plain round @click="resetForm('newTechnology')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { addCategory, addTechnology, getCategory, getTechnology,
  updateCategory, updateTechnology, deleteCategory, deleteTechnology } from '@/api/category'

export default {
  name: 'Category',
  data() {
    return {
      categorys: null,
      technologys: null,
      categoryList: {
        pageNum: 1,
        pageSize: 6,
        total: 0,
        list: []
      },
      technologyList: {
        pageNum: 1,
        pageSize: 6,
        total: 0,
        list: []
      },
      newCategory: {
        name: '',
        pictureUrl: ''
      },
      newTechnology: {
        name: '',
        pictureUrl: ''
      },
      updateTechnology: {
        name: '',
        pictureUrl: ''
      },
      updateCategory: {
        name: '',
        pictureUrl: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        pictureUrl: [
          { required: true, message: '请输入图标地址', trigger: 'blur' },
          {
            pattern: /^(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$/,
            message: '必须为可访问网址！',
            trigger: 'blur'
          }
        ]
      },
      editTechnology: false,
      editCategory: false
    }
  },
  created() {
    this.getCategory()
    this.getTechnology()
  },
  methods: {
    categoryEdit(row) {
      Object.assign(this.updateCategory, row)
      this.editCategory = true
    },
    putNewCategory(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.editCategory = false
          updateCategory(this.updateCategory).then(() => {
            this.$notify({
              title: '更新分类',
              message: '更新新增成功: ' + this.updateCategory.name,
              type: 'success',
              duration: 3000
            })
            this.getCategory()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '更新分类',
              message: this.updateCategory.name + ': 参数有误或该分类已存在',
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    categoryDelete(row) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCategory(row).then(() => {
          this.$notify({
            title: '删除分类',
            message: '删除分类成功: ' + row.name,
            type: 'success',
            duration: 3000
          })
          this.getCategory()
        })
      })
    },
    technologyEdit(row) {
      Object.assign(this.updateTechnology, row)
      this.editTechnology = true
    },
    putNewTechnology(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.editTechnology = false
          updateTechnology(this.updateTechnology).then(() => {
            this.$notify({
              title: '更新技术栈',
              message: '技术栈更新成功: ' + this.updateTechnology.name,
              type: 'success',
              duration: 3000
            })
            this.getTechnology()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '更新技术栈',
              message: this.updateTechnology.name + ': 参数有误或已存在',
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    technologyDelete(row) {
      this.$confirm('此操作将永久删除该技术栈信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTechnology(row).then(() => {
          this.$notify({
            title: '删除技术栈',
            message: '删除技术栈成功: ' + row.name,
            type: 'success',
            duration: 3000
          })
          this.getTechnology()
        })
      })
    },
    // 新增分类
    addCategory(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const categoryInfo = this.newCategory
          addCategory(categoryInfo).then(() => {
            this.$notify({
              title: '新增分类',
              message: '分类新增成功: ' + categoryInfo.name,
              type: 'success',
              duration: 3000
            })
            this.getCategory()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '新增分类',
              message: categoryInfo.name + ': 参数有误或该分类已存在',
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    addTechnology(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const technology = this.newTechnology
          addTechnology(technology).then(() => {
            this.$notify({
              title: '新增技术栈',
              message: '技术栈新增成功: ' + technology.name,
              type: 'success',
              duration: 3000
            })
            this.getTechnology()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '新增技术栈',
              message: technology.name + ': 参数有误或已存在',
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    getCategory() {
      this.categoryList.pageNum = 1
      getCategory().then(response => {
        this.categorys = response
        this.categoryList.total = response.length
        let size = this.categoryList.pageSize
        if (size >= response.length) {
          size = response.length
        }
        this.categoryList.list = response.slice(0, size)
      })
    },
    getTechnology() {
      this.technologyList.pageNum = 1
      getTechnology().then(response => {
        this.technologys = response
        this.technologyList.total = response.length
        let size = this.technologyList.pageSize
        if (size >= response.length) {
          size = response.length
        }
        this.technologyList.list = response.slice(0, size)
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    fetchDataC() {
      const page = this.categoryList.pageNum
      const size = this.categoryList.pageSize
      this.categoryList.list = this.categorys.slice((page - 1) * size, page * size)
    },
    fetchDataT() {
      const page = this.technologyList.pageNum
      const size = this.technologyList.pageSize
      this.technologyList.list = this.technologys.slice((page - 1) * size, page * size)
    }
  }
}
</script>

<style lang="scss" scoped>
  .category {
    &-container {
      position: relative;
      height: 100vh;
      margin: 15px;

      .top-container {
        width: 100%;
        height: 100%;
      }

      .bottom-container {
        width: 100%;
        height: 100%;
      }
      .pagination{
        margin-top: 7px;
        text-align: center;
      }
      .grid-content {
        border-radius: 4px;
        min-height: 430px;
      }
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
  .shadow{
    box-shadow: 0 0 6px rgba(0, 0, 0, .06);
    border-radius: 8px;
    padding: 1%
  }
</style>
