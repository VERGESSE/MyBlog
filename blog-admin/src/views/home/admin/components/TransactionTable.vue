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
      <el-table-column label="IP" min-width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="留言时间" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
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
      @prev-click="fetchData"
      @next-click="fetchData"
    />
  </div>
</template>

<script>
import { getMsg } from '@/api/message'

export default {
  filters: {
    formattTime(str) {
      return str.substring(0, 30)
    }
  },
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
