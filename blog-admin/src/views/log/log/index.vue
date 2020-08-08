<template>
  <div class="log-container shadow">
    <el-table :data="pageInfo.list" stripe border style="width: 100%;padding-top: 15px;">
      <el-table-column label="IP" width="140">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="操作者描述" min-width="280" align="center">
        <template slot-scope="scope">
          <el-link type="primary" @click="changeDetail(scope.row)">
            {{ scope.row.detail }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="操作内容" min-width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column label="url" width="210" align="center">
        <template slot-scope="scope">
          {{ scope.row.operateUrl.split("/").slice(3).join("/") }}
        </template>
      </el-table-column>
      <el-table-column label="操作者地址" min-width="320" align="center">
        <template slot-scope="scope">
          {{ scope.row.addr }}
        </template>
      </el-table-column>
      <el-table-column label="时间" width="160" align="center">
        <template slot-scope="scope">
          {{ scope.row.createBy }}
        </template>
      </el-table-column>
      <el-table-column label="设备" min-width="300" align="center">
        <template slot-scope="scope">
          {{ scope.row.operateBy }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      background
      hide-on-single-page
      :current-page.sync="pageInfo.pageNum"
      :page-size="pageInfo.pageSize"
      layout="prev, pager, next, total"
      :total="pageInfo.total"
      :page-count="pageInfo.pages"
      @current-change="fetchData"
    />

  </div>
</template>

<script>
import { getLogs, updateIpDetail } from '@/api/log'

export default {
  name: 'LogTable',
  data() {
    return {
      search: '',
      pageInfo: {
        pageNum: 1,
        pageSize: 15,
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
      getLogs(this.pageInfo).then(response => {
        this.pageInfo = response
      })
    },
    changeDetail(row) {
      const sysLog = {
        ip: row.ip,
        detail: ''
      }
      this.$prompt('请输入新的描述', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        sysLog.detail = value
        // 更新描述
        updateIpDetail(sysLog).then(() => {
          this.$notify({
            title: '更新成功',
            message: '成功更新' + sysLog.ip + '为: ' + value,
            type: 'success',
            duration: 3000
          })
        })
        for (let i = 0; i < this.pageInfo.list.length; i++) {
          if (this.pageInfo.list[i].ip === sysLog.ip) {
            this.pageInfo.list[i].detail = sysLog.detail
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.log {
  &-container {
    margin: 14px;
    .pagination{
      margin-top: 11px;
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
  padding: 0.5%
}
</style>
