<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '360px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
      // 动态改变X轴数据
      this.chart.on('legendselectchanged', obj => {
        const name = obj.name
        const option = this.chart.getOption()
        if (name === 'Today visitors') {
          option.xAxis[0].data = this.getNowX()
        } else if (name === 'Year visitors') {
          option.xAxis[0].data = this.getMouthX()
        }
        this.chart.setOption(option)
      })
    },
    setOptions({ todayVisits, yearVisits } = {}) {
      const newX = this.getNowX()
      this.chart.setOption({
        xAxis: {
          data: newX,
          boundaryGap: true,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 20,
          bottom: 15,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['Today visitors', 'Year visitors'],
          selectedMode: 'single'
        },
        series: [{
          name: 'Today visitors', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: todayVisits,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'Year visitors',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: yearVisits,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    // 根据当前时间获取今日统计表的下标
    getNowX() {
      const date = new Date()
      const nowX = []
      let hour = date.getHours()
      for (let i = 0; i < 12; i++) {
        hour += 2
        if (hour >= 24) {
          hour -= 24
        }
        nowX.push(hour + ':00')
      }
      return nowX
    },
    getMouthX() {
      const months = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'Jun.', 'Jul.', 'Aug.', 'Sep.', 'Oct.', 'Nov.', 'Dec.']
      const date = new Date()
      const nowX = []
      // js时间从0月开始, months下标同样从零开始
      let month = date.getMonth()
      for (let i = 0; i < 12; i++) {
        month += 1
        if (month >= 12) {
          month -= 12
        }
        nowX.push(months[month])
      }
      return nowX
    }
  }
}
</script>
