<template>
  <div>
    <el-table :data="pageInfo.list" style="width: 100%;padding-top: 15px;">
      <el-table-column label="昵称" min-width="100">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="email" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>
      </el-table-column>
      <el-table-column label="留言信息" min-width="500" align="center">
        <template slot-scope="scope">
          {{ scope.row.message }}
        </template>
      </el-table-column>
      <el-table-column label="IP" min-width="130" align="center">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="留言时间" min-width="155" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            :type="scope.row.isShow === 1 ? 'success' : 'warning'"
            @click="handleEdit(scope.$index, scope.row)"
          >
            {{ scope.row.isShow === 1 ? '展示中' : '未展示' }}
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      background="true"
      hide-on-single-page="true"
      :current-page.sync="pageInfo.pageNum"
      :page-size="pageInfo.pageSize"
      layout="prev, pager, next"
      :total="pageInfo.total"
      :page-count="pageInfo.pages"
      @current-change="fetchData"
    />
  </div>
</template>

<script>
import { getMsg, changeShow, deleteMsg } from '@/api/message'

export default {
  data() {
    return {
      pageInfo: {
        pageNum: 1,
        pageSize: 7,
        total: 0,
        list: []
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getMsg(this.pageInfo).then(response => {
        this.pageInfo = response
      })
    },
    handleEdit(index, message) {
      changeShow(message).then(() => {
        if (this.pageInfo.list[index].isShow === 1) {
          this.pageInfo.list[index].isShow = 0
        } else {
          this.pageInfo.list[index].isShow = 1
        }
        this.$notify({
          title: '更新成功',
          message: '成功修改 ' + message.name + ' : ' + message.message.substring(0, 40) + ' 的展示状态',
          type: 'success',
          duration: 3000
        })
      })
    },
    handleDelete(message) {
      deleteMsg(message).then(() => {
        this.fetchData()
        this.$notify({
          title: '删除成功',
          message: '成功删除 ' + message.name + ' : ' + message.message,
          type: 'success',
          duration: 3000
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .pagination{
    margin-top: 15px;
    text-align: center;
  }
</style>
