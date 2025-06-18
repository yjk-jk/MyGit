<template>
  <div class="home-page">
    <!-- 顶部导航栏 -->
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
    >
      <el-menu-item index="1">首页</el-menu-item>
      <el-menu-item index="2">国内游</el-menu-item>
      <el-menu-item index="3">国外游</el-menu-item>
      <el-menu-item index="4">特价游</el-menu-item>
    </el-menu>

    <!-- 轮播图 -->
    <el-carousel :interval="4000" type="card" height="400px">
      <el-carousel-item v-for="item in destinations" :key="item.id">
        <img :src="item.imageUrl" alt="carousel image" class="carousel-image" />
      </el-carousel-item>
    </el-carousel>

    <!-- 推荐旅游地 -->
    <h2 class="section-title">热门推荐</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="destination in destinations" :key="destination.id">
        <el-card class="destination-card">
          <img :src="destination.imageUrl" class="destination-image" />
          <div class="destination-info">
            <span>{{ destination.name }}</span>
            <p>{{ destination.description }}</p>
            <el-button type="primary" @click="router.push('/city')">了解更多</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <Footer />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getAllCities } from '@/api/cities'
import { useRouter } from 'vue-router'
import Footer from '@/display/footer/index.vue'

// 顶部导航栏激活索引
const activeIndex = ref('1')

// 推荐旅游地列表
const destinations = ref([])

// 路由
const router = useRouter()
onMounted(() => {
  getAllCities().then((res) => {
    destinations.value = res.data.data
    console
  })
})

// 导航栏选择事件处理函数
const handleSelect = (key) => {
  console.log(`选择了第 ${key} 项`)
}
</script>

<style scoped>
.home-page {
  width: 1200px;
  padding: 20px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.section-title {
  margin-top: 40px;
  font-size: 24px;
  color: #333;
}

.destination-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.destination-info {
  padding: 14px;
}

.footer {
  margin-top: 40px;
  text-align: center;
  color: #888;
}
</style>
