<template>
  <div class="goodlink-container">
    <div class="shadow">
      <div style="color: #303133;font-size: 26px;text-align: center;font-weight: bolder;margin-bottom: 16px">
        优质博文分享列表
      </div>
      <el-table :data="goodLinkInfos.list" border style="width: 100%">
        <el-table-column label="外链ID" min-width="140" align="center">
          <template slot-scope="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column label="外链标题" min-width="200" align="center">
          <template slot-scope="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>
        <el-table-column label="外链地址" min-width="220" align="center">
          <template slot-scope="scope">
            <el-link
              type="info"
              :href="scope.row.url"
              style="font-size: 15px"
              target="_blank"
            >
              {{ scope.row.url }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.createBy }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="info"
              round
              @click="goodLinkEdit(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              round
              @click="goodLinkDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        hide-on-single-page
        :current-page.sync="goodLinkInfos.pageNum"
        :page-size="goodLinkInfos.pageSize"
        layout="prev, pager, next"
        :total="goodLinkInfos.total"
        :page-count="goodLinkInfos.pages"
        @current-change="getGoodLink"
      />
      <!-- 修改对话框 -->
      <el-dialog title="外链信息修改" :visible.sync="editLink">
        <el-form ref="updateLink" :model="updateLink" :rules="rules">
          <el-form-item label="外链标题" prop="title" label-width="100px">
            <el-input v-model="updateLink.title" type="text" maxlength="60" show-word-limit />
          </el-form-item>
          <el-form-item label="外链地址" prop="url" label-width="100px">
            <el-input v-model="updateLink.url" type="textarea" maxlength="200" show-word-limit />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editLink = false">取 消</el-button>
          <el-button type="primary" @click="updateOldLink('updateLink')">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <el-row>
      <el-col :span="12">
        <div class="grid-content shadow">
          <el-form ref="newLink" :model="newLink" :rules="rules" label-width="100px">
            <span style="color: #909399;font-size: 23px;margin-left: 20px">添加外链</span>
            <el-divider style="margin-left: 20px" />
            <el-form-item label="外链标题" prop="title">
              <el-input v-model="newLink.title" type="text" maxlength="60" show-word-limit />
            </el-form-item>
            <el-form-item label="外链地址" prop="url">
              <el-input v-model="newLink.url" type="textarea" maxlength="200" show-word-limit />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" round @click="addLink('newLink')">新增外链</el-button>
              <el-button type="warning" round @click="resetForm('newLink')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content shadow">
          <span style="color: #909399;font-size: 23px;margin-left: 20px">待审核</span>
          <el-divider style="margin-left: 20px" />
          <el-table :data="notCheckInfos.list" border style="width: 100%">
            <el-table-column label="外链标题" min-width="200" align="center">
              <template slot-scope="scope">
                <span style="font-size: 14px">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column label="外链地址" width="80" align="center">
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  :href="scope.row.url"
                  target="_blank"
                >
                  点击查看
                </el-link>
              </template>
            </el-table-column>
            <el-table-column label="申请人邮箱" min-width="120" align="center">
              <template slot-scope="scope">
                {{ scope.row.email }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="150">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="success"
                  round
                  @click="accessNewLink(scope.row)"
                >通过</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  round
                  @click="goodLinkDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pagination"
            hide-on-single-page
            :current-page.sync="notCheckInfos.pageNum"
            :page-size="notCheckInfos.pageSize"
            layout="prev, pager, next"
            :total="notCheckInfos.total"
            :page-count="notCheckInfos.pages"
            @current-change="getLinkNotCheck"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getLink, addLink, updateLink, deleteLink, getLinkNotCheck } from '@/api/article'

export default {
  name: 'GoodLink',
  data() {
    return {
      goodLinkInfos: {
        pageNum: 1,
        pageSize: 8,
        total: 0,
        list: []
      },
      notCheckInfos: {
        pageNum: 1,
        pageSize: 4,
        total: 0,
        list: []
      },
      newLink: { id: '', title: '', url: '', createBy: '' },
      updateLink: { id: '', title: '', url: '', createBy: '' },
      editLink: false,
      rules: {
        title: [
          { required: true, message: '标题不能为空', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入正确URL', trigger: 'blur' },
          {
            pattern: /^(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$/,
            message: '必须为可访问网址！',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted() {
    this.getGoodLink()
    this.getLinkNotCheck()
  },
  methods: {
    goodLinkEdit(row) {
      Object.assign(this.updateLink, row)
      this.editLink = true
    },
    addLink(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const newLink = this.newLink
          addLink(newLink).then(() => {
            this.$notify({
              title: '新建博客外链',
              message: '外链新增成功: ' + newLink.title,
              type: 'success',
              duration: 3000
            })
            this.getGoodLink()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '新建博客外链',
              message: '外链新增失败: ' + newLink.title,
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    updateOldLink(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          updateLink(this.updateLink).then(() => {
            this.$notify({
              title: '更新外链信息',
              message: '外链更新成功: ' + this.updateLink.title,
              type: 'success',
              duration: 3000
            })
            this.getGoodLink()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '更新外链信息',
              message: '外链更新失败: ' + this.updateLink.title,
              duration: 3000
            })
            this.resetForm(formName)
          })
          this.editLink = false
        } else {
          return false
        }
      })
    },
    goodLinkDelete(row) {
      this.$confirm('此操作将永久删除该外链, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLink(row.id).then(() => {
          this.$notify({
            title: '删除外链',
            message: row.title + ' 已删除',
            type: 'success',
            duration: 3000
          })
          this.getGoodLink()
          this.getLinkNotCheck()
        })
      })
    },
    getLinkNotCheck() {
      getLinkNotCheck(this.notCheckInfos).then(response => {
        this.notCheckInfos = response
      })
    },
    getGoodLink() {
      getLink(this.goodLinkInfos).then(response => {
        this.goodLinkInfos = response
      })
    },
    accessNewLink(row) {
      this.$confirm('是否通过此外链申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        row.state = 1
        updateLink(row).then(() => {
          this.$notify({
            title: '通过外链成功',
            message: '标题: ' + row.title,
            type: 'success',
            duration: 3000
          })
          this.getGoodLink()
          this.getLinkNotCheck()
        })
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
  .goodlink {
    &-container {
      margin: 10px;

      .grid-content {
        border-radius: 4px;
        min-height: 36px;
      }
      .pagination{
        margin-top: 7px;
        text-align: center;
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
    margin: 10px;
    padding: 1%
  }
</style>
