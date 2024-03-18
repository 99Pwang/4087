<template>
  <div style="margin-top: 10px ;">
    <el-card>
      <b>Hello！{{ user.nickname }}</b>，Welcome to XXXX System
    </el-card>

    <el-card style="margin-top: 10px">
      <el-row >
        <el-col :span="24">
          <div id="pie" style="width: 100%; height: 400px"></div>
        </el-col>
      </el-row>
    </el-card>

  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  mounted() {  // 页面元素渲染之后再触发
    // 饼图
    const pieOption = {
      title: {
        text: 'Statistics of Applications for Positions',
        left: 'center'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b} : {c} ({d}%)'
      },
      series: [
        {
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          label: {
            show: true,
            formatter: '{b}: {c}'
          },
          labelLine: {
            show: true
          }
        },
      ],

    };

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/data").then(res => {
      pieOption.series[0].data = res.data
      pieChart.setOption(pieOption)
    })
  }
}
</script>

<style scoped>

</style>
