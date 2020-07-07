<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <div class="blog-message">访客统计</div>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <el-divider />
    <div class="blog-message">最新留言</div>
    <el-row :gutter="24">
      <el-col :xs="{span: 24}" style="padding-right:8px;margin-bottom:30px;">
        <transaction-table />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import TransactionTable from './components/TransactionTable'
import { todayVisitors, yearVisitors } from '@/api/home'

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart,
    TransactionTable
  },
  data() {
    return {
      lineChartData: {
        todayVisits: [],
        yearVisits: []
      }
    }
  },
  mounted() {
    todayVisitors().then(response => {
      this.lineChartData.todayVisits = response
    })
    yearVisitors().then(response => {
      this.lineChartData.yearVisits = response
    })
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 26px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 15px;
  }
  .blog-message{
    margin-bottom: 8px;
    font-size: 21px;
    color: #909399;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
