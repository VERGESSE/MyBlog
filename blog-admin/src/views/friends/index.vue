<template>
  <div class="friends-container">
    <div class="shadow">
      <div style="color: #303133;font-size: 26px;text-align: center;font-weight: bolder;margin-bottom: 16px">
        友情链接列表
      </div>
      <el-table :data="friendInfos.list" border style="width: 100%">
        <el-table-column label="友链ID" min-width="140" align="center">
          <template slot-scope="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column label="好友昵称" min-width="130" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="友链介绍" min-width="250" align="center">
          <template slot-scope="scope">
            {{ scope.row.detail }}
          </template>
        </el-table-column>
        <el-table-column label="友链地址" min-width="200" align="center">
          <template slot-scope="scope">
            <el-link
              type="info"
              :href="scope.row.url"
              style="font-size: 3px"
              target="_blank"
            >
              {{ scope.row.url }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="好友头像" min-width="260" align="center">
          <template slot-scope="scope">
            <el-link
              type="info"
              :href="scope.row.photo"
              style="font-size: 3px"
              target="_blank"
            >
              {{ scope.row.photo.split("/").slice(3).join("/") }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="160" align="center">
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
              @click="friendEdit(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              round
              @click="friendDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        hide-on-single-page
        :current-page.sync="friendInfos.pageNum"
        :page-size="friendInfos.pageSize"
        layout="prev, pager, next"
        :total="friendInfos.total"
        :page-count="friendInfos.pages"
        @current-change="pageNumChange"
      />
      <!-- 修改对话框 -->
      <el-dialog title="友链信息修改" :visible.sync="editFriend">
        <el-form ref="updateFriend" :model="updateFriend" :rules="rules">
          <el-form-item label="好友昵称" prop="name" label-width="100px">
            <el-input v-model="updateFriend.name" type="text" maxlength="50" show-word-limit />
          </el-form-item>
          <el-form-item label="友链介绍" prop="detail" label-width="100px">
            <el-input v-model="updateFriend.detail" type="textarea" maxlength="200" show-word-limit />
          </el-form-item>
          <el-form-item label="友链地址" prop="url" label-width="100px">
            <el-input v-model="updateFriend.url" />
          </el-form-item>
          <el-form-item label="好友头像" prop="photo" label-width="100px">
            <el-input v-model="updateFriend.photo" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editFriend = false">取 消</el-button>
          <el-button type="primary" @click="updateOldFriend('updateFriend')">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <el-row>
      <el-col :span="12">
        <div class="grid-content shadow">
          <el-form ref="newFriend" :model="newFriend" :rules="rules" label-width="100px">
            <span style="color: #909399;font-size: 23px;margin-left: 20px">新增友链</span>
            <el-divider style="margin-left: 20px" />
            <el-form-item label="好友昵称" prop="name">
              <el-input v-model="newFriend.name" type="text" maxlength="50" show-word-limit />
            </el-form-item>
            <el-form-item label="友链介绍" prop="detail">
              <el-input v-model="newFriend.detail" type="textarea" maxlength="200" show-word-limit />
            </el-form-item>
            <el-form-item label="友链地址" prop="url">
              <el-input v-model="newFriend.url" />
            </el-form-item>
            <el-form-item label="好友头像" prop="photo">
              <el-input v-model="newFriend.photo" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" round @click="addFriend('newFriend')">新增友链</el-button>
              <el-button type="warning" round @click="resetForm('newFriend')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content shadow">
          <span style="color: #909399;font-size: 23px;margin-left: 20px">待审核</span>
          <el-divider style="margin-left: 20px" />
          <el-table :data="notCheckInfos.list" border style="width: 100%">
            <el-table-column label="好友昵称" min-width="110" align="center">
              <template slot-scope="scope">
                {{ scope.row.name }}
              </template>
            </el-table-column>
            <el-table-column label="友链介绍" min-width="200" align="center">
              <template slot-scope="scope">
                {{ scope.row.detail }}
              </template>
            </el-table-column>
            <el-table-column label="友链地址" min-width="80" align="center">
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  :href="scope.row.url"
                  style="font-size: 3px"
                  target="_blank"
                >
                  点击访问
                </el-link>
              </template>
            </el-table-column>
            <el-table-column label="申请人邮箱" min-width="150" align="center">
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
                  @click="accessNewFriends(scope.row)"
                >通过</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  round
                  @click="friendDelete(scope.row)"
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
            @current-change="pageNumChange2"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getFriend, addFriend, updateFriend, deleteFriend, getFriendNotCheck } from '@/api/friend'

export default {
  name: 'Friends',
  data() {
    return {
      friendsList: null,
      friendInfos: {
        pageNum: 1,
        pageSize: 8,
        total: 0,
        list: []
      },
      notCheckList: null,
      notCheckInfos: {
        pageNum: 1,
        pageSize: 4,
        total: 0,
        list: []
      },
      newFriend: { id: '', name: '', detail: '', url: '', photo: '', createBy: '' },
      updateFriend: { id: '', name: '', detail: '', url: '', photo: '', createBy: '' },
      editFriend: false,
      rules: {
        name: [
          { required: true, message: '请输入好友昵称', trigger: 'blur' }
        ],
        detail: [
          { required: true, message: '介绍不能为空', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入正确URL', trigger: 'blur' },
          {
            pattern: /^(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$/,
            message: '必须为可访问网址！',
            trigger: 'blur'
          }
        ],
        photo: [
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
    this.getFriend()
    this.getFriendNotCheck()
  },
  methods: {
    friendEdit(row) {
      Object.assign(this.updateFriend, row)
      this.editFriend = true
    },
    addFriend(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const newFriend = this.newFriend
          addFriend(newFriend).then(() => {
            this.$notify({
              title: '新建友链',
              message: '友链新增成功: ' + newFriend.url,
              type: 'success',
              duration: 3000
            })
            this.getFriend()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '新建友链',
              message: '友链新增失败: ' + newFriend.url,
              duration: 3000
            })
            this.resetForm(formName)
          })
        } else {
          return false
        }
      })
    },
    updateOldFriend(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          updateFriend(this.updateFriend).then(() => {
            this.$notify({
              title: '更新友链信息',
              message: '更新友链成功: ' + this.updateFriend.url,
              type: 'success',
              duration: 3000
            })
            this.getFriend()
            this.resetForm(formName)
          }).catch(() => {
            this.$notify.error({
              title: '更新友链信息',
              message: '更新友链失败: ' + this.updateFriend.url,
              duration: 3000
            })
            this.resetForm(formName)
          })
          this.editFriend = false
        } else {
          return false
        }
      })
    },
    friendDelete(row) {
      this.$confirm('此操作将永久删除该友链, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFriend(row.id).then(() => {
          this.$notify({
            title: '删除友链',
            message: row.url + ' 已删除',
            type: 'success',
            duration: 3000
          })
          this.getFriend()
          this.getFriendNotCheck()
        })
      })
    },
    getFriendNotCheck() {
      getFriendNotCheck().then(response => {
        this.notCheckList = response
        this.notCheckInfos.total = response.length
        let size = this.notCheckInfos.pageSize
        if (size >= response.length) {
          size = response.length
        }
        this.notCheckInfos.list = this.notCheckList.slice(0, size)
        this.notCheckInfos.pageNum = 1
      })
    },
    getFriend() {
      getFriend().then(response => {
        this.friendsList = response
        this.friendsList.sort((a, b) => {
          return a.createBy >= b.createBy ? -1 : 1
        })
        this.friendInfos.total = response.length
        let size = this.friendInfos.pageSize
        if (size >= response.length) {
          size = response.length
        }
        this.friendInfos.list = this.friendsList.slice(0, size)
        this.friendInfos.pageNum = 1
      })
    },
    accessNewFriends(row) {
      this.$confirm('是否通过此友链申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        row.state = 1
        updateFriend(row).then(() => {
          this.$notify({
            title: '通过友链成功',
            message: '地址: ' + row.url,
            type: 'success',
            duration: 3000
          })
          this.getFriend()
          this.getFriendNotCheck()
        })
      })
    },
    pageNumChange() {
      const size = this.friendInfos.pageSize
      const start = (this.friendInfos.pageNum - 1) * size
      let end = (this.friendInfos.pageNum) * size
      if (end > this.friendsList.length) {
        end = this.friendsList.length
      }
      this.friendInfos.list = this.friendsList.slice(start, end)
      console.log(start, end)
    },
    pageNumChange2() {
      const size = this.notCheckInfos.pageSize
      const start = (this.notCheckInfos.pageNum - 1) * size
      let end = (this.notCheckInfos.pageNum) * size
      if (end > this.notCheckList.length) {
        end = this.notCheckList.length
      }
      this.notCheckInfos.list = this.notCheckList.slice(start, end)
      console.log(start, end)
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
  .friends {
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
