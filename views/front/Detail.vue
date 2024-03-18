<template>
  <el-card>
    <div style="font-size: 20px; font-weight: bold">{{ item.name }}  <span style="margin-left: 20px; margin-right: 30px; font-size: 13px; font-weight: normal">{{ item.status }}</span>
    <el-tag style="margin-left: 5px" v-if="item.tags" v-for="it in item.tags.split(',')">{{ it }}</el-tag>
    </div>
    <div style="font-size: 14px; color: #666; margin: 20px 0">
      <span><i class="el-icon-location"></i> {{ item.location }}</span>
      <span style="margin-left: 10px"><i class="el-icon-goods"></i> {{ item.experience }}</span>
      <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.edu }}</span>
      <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.price }}</span>
      <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.type }}</span>
      <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.ways }}</span>
      <span style="margin-left: 10px"><i class="el-icon-medal"></i> {{ item.time2 }}</span>
    </div>

    <div style="font-size: 18px; margin: 20px 0; color: #333">Position description</div>
    <div style="font-size: 14px; color: #666; line-height: 24px" v-html="item.descr"></div>

    <div style="font-size: 18px; margin: 20px 0;  color: #333">Publisher</div>
    <div style="font-size: 14px; margin: 20px 0;  color: #666">{{ item.manager }}</div>

    <div style="font-size: 18px; margin: 20px 0;  color: #333">Skills required</div>
    <div style="font-size: 14px; margin: 20px 0;  color: #666">{{ item.skills }}</div>

<!--    <div style="font-size: 18px; margin: 20px 0;  color: #333">Highlight of position</div>-->
<!--    <div style="font-size: 14px; margin: 20px 0;  color: #666">{{ item.highlights }}</div>-->

    <div style="font-size: 18px; margin: 20px 0;  color: #333">Package benefits</div>
    <div style="font-size: 14px; margin: 20px 0;  color: #666">{{ item.welfare }}</div>

    <div style="font-size: 18px; margin: 20px 0;  color: #333">Recommendation requirement</div>
    <div style="font-size: 14px; margin: 20px 0;  color: #666">{{ item.recommend }}</div>


    <el-button type="primary" plain>
      <router-link to="/front/home">Back</router-link>
    </el-button>
  </el-card>
</template>

<script>
export default {
  name: 'Detail',
  data() {
    return {
      item: {}
    }
  },
  created() {
    this.request.get('/job/' + this.$route.query.id).then(res => {
      this.item = res.data
    })
  }
}
</script>