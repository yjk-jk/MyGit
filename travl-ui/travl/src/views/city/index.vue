<template>
  <div class="city-container">
    <div class="city-list">
      <h2>城市列表</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else>
        <div
          v-for="city in cities"
          :key="city.id"
          class="city-item"
          @click="selectCity(city.id)"
          :class="{ active: selectedCityId === city.id }"
        >
          <span class="city-name">{{ city.name }}</span>
          <span class="city-country">{{ city.country }}</span>
        </div>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="city-detail" v-if="selectedCity">
          <h2>{{ selectedCity.name }} 详情</h2>
          <div class="detail-content">
            <div class="detail-section">
              <h3>基本信息</h3>
              <div class="detail-row">
                <span class="detail-label">国家:</span>
                <span>{{ selectedCity.country }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">最佳访问时间:</span>
                <span>{{ selectedCity.bestTimeToVisit }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">创建时间:</span>
                <span>{{ formatDate(selectedCity.createdAt) }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">更新时间:</span>
                <span>{{ formatDate(selectedCity.updatedAt) }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h3>城市描述</h3>
              <p class="city-description">{{ selectedCity.description }}</p>
            </div>

            <div class="detail-section" v-if="selectedCity.imageUrl">
              <h3>城市图片</h3>
              <img
                :src="selectedCity.imageUrl"
                alt="城市图片"
                class="city-image"
                @click="downloadImage(selectedCity.id)"
              />
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="city-weather">
          <h2>城市天气</h2>
          <el-button
            type="primary"
            @click="queryWeather"
            :disabled="!selectedCity"
            style="margin-bottom: 20px"
          >
            {{ selectedCity ? `查询${selectedCity.name}天气` : '请先选择城市' }}
          </el-button>

          <div v-if="weatherData" class="weather-content">
            <el-card shadow="hover">
              <template v-slot:header>
                <div class="weather-header">
                  <span>{{ weatherData.city }}</span>
                </div>
              </template>
              <div class="weather-current">
                <div class="weather-main">
                  <div class="weather-icon">
                    <i :class="getWeatherIcon(weatherData.data[0].weather)"></i>
                  </div>
                  <div class="weather-temp">
                    <span class="temp">{{ weatherData.data[0].temperature.split(' / ')[0] }}</span>
                    <span class="weather-desc">{{ weatherData.data[0].weather }}</span>
                  </div>
                </div>
                <el-divider></el-divider>
                <div class="weather-details">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <div class="detail-item">
                        <span class="label">风速</span>
                        <span class="value">{{ weatherData.data[0].wind_level }}</span>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="detail-item">
                        <span class="label">空气质量</span>
                        <span class="value">{{ weatherData.data[0].air_quality }}</span>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-card>

            <el-card shadow="hover" style="margin-top: 20px">
              <template v-slot:header>
                <div class="weather-header">
                  <span>未来几天预报</span>
                </div>
              </template>
              <div class="weather-forecast">
                <el-row :gutter="20">
                  <el-col :span="8" v-for="(day, index) in weatherData.data.slice(1)" :key="index">
                    <div class="forecast-day">
                      <div class="day-name">{{ day.date }}</div>
                      <div class="day-weather">
                        <i :class="getWeatherIcon(day.weather)"></i>
                        <span>{{ day.weather }}</span>
                      </div>
                      <div class="day-temp">{{ day.temperature }}</div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <Footer />
</template>

<script>
import { getAllCities, getCityById, downloadCityImage } from '@/api/cities'
import Footer from '@/display/footer/index.vue'
export default {
  name: 'CityView',
  data() {
    return {
      cities: [],
      selectedCityId: null,
      selectedCity: null,
      loading: false,
      error: null,
      weatherData: null,
    }
  },
  components: {
    Footer,
  },
  async created() {
    await this.fetchCities()
  },
  methods: {
    async fetchCities() {
      this.loading = true
      try {
        const response = await getAllCities()
        this.cities = response.data.data
      } catch (error) {
        this.error = error.message
        console.error('获取城市列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async selectCity(cityId) {
      this.selectedCityId = cityId
      this.loading = true
      try {
        const response = await getCityById(cityId)
        this.selectedCity = response.data.data
      } catch (error) {
        this.error = error.message
        console.error('获取城市详情失败:', error)
      } finally {
        this.loading = false
      }
    },
    async downloadImage(cityId) {
      try {
        const response = await downloadCityImage(cityId)
        console.log(response)
        // 检查响应头是否存在 'content-disposition'
        const contentDisposition = response.headers['content-disposition']
        const contentType = response.headers['content-type']
        // 创建Blob对象
        const blob = new Blob([response.data], { type: contentType })

        // 创建下载链接
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = contentDisposition
        document.body.appendChild(link)
        link.click()

        // 清理资源
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
      } catch (error) {
        console.error('下载图片失败:', error)
        this.error = '下载图片失败，请稍后再试'
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
    },

    getWeatherIcon(weather) {
      if (weather.includes('晴')) return 'el-icon-sunny'
      if (weather.includes('云')) return 'el-icon-cloudy'
      if (weather.includes('雨')) return 'el-icon-heavy-rain'
      if (weather.includes('雪')) return 'el-icon-snowy'
      return 'el-icon-partly-cloudy'
    },

    async queryWeather() {
      if (!this.selectedCity) {
        this.$message.warning('请先选择城市')
        return
      }
      try {
        this.loading = true
        const response = await fetch(
          `https://api.pearktrue.cn/api/weather/?city=${this.selectedCity.name}&id=1`
        )
        const data = await response.json()
        if (data.code === 200) {
          this.weatherData = data
        } else {
          this.$message.error(data.msg || '获取天气信息失败')
        }
      } catch (error) {
        console.error('查询天气失败:', error)
        this.$message.error('查询天气失败，请稍后再试')
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.city-container {
  display: flex;
  gap: 30px;
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.city-list {
  width: 480px;
  border-right: 1px solid #eee;
  padding-right: 20px;
}

.city-item {
  padding: 12px 15px;
  cursor: pointer;
  border-radius: 6px;
  margin-bottom: 8px;
  display: flex;
  flex-direction: column;
  transition: all 0.2s;
}

.city-item:hover {
  background-color: #f5f5f5;
}

.city-item.active {
  background-color: #e0e0e0;
  font-weight: bold;
}

.city-name {
  font-size: 16px;
  font-weight: 500;
}

.city-country {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.city-detail {
  width: 700px;
  flex: 1;
  min-width: 0;
}

.city-weather {
  padding-left: 20px;
  width: 500px;
}

.weather-content {
  margin-top: 10px;
}

.weather-header {
  font-weight: bold;
}

.weather-current {
  padding: 10px;
}

.weather-main {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.weather-icon {
  font-size: 48px;
  margin-right: 20px;
  color: #409eff;
}

.weather-temp {
  display: flex;
  flex-direction: column;
}

.temp {
  font-size: 24px;
  font-weight: bold;
}

.weather-desc {
  font-size: 14px;
  color: #909399;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 5px 0;
}

.label {
  color: #909399;
}

.value {
  font-weight: 500;
}

.forecast-day {
  text-align: center;
  padding: 10px;
}

.day-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.day-weather {
  margin: 10px 0;
}

.day-weather i {
  font-size: 24px;
  color: #409eff;
  display: block;
  margin-bottom: 5px;
}

.day-temp {
  color: #606266;
}

.detail-content {
  margin-top: 20px;
}

.detail-section {
  margin-bottom: 30px;
}

.detail-section h3 {
  margin-bottom: 15px;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.detail-row {
  display: flex;
  margin-bottom: 10px;
  line-height: 1.6;
}

.detail-label {
  font-weight: bold;
  min-width: 100px;
  color: #666;
}

.city-description {
  line-height: 1.8;
  color: #444;
}

.city-image {
  max-width: 100%;
  max-height: 400px;
  border-radius: 8px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: transform 0.3s;
}

.city-image:hover {
  transform: scale(1.02);
}

.download-btn {
  margin-top: 10px;
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.download-btn:hover {
  background-color: #66b1ff;
}

.loading {
  padding: 30px;
  text-align: center;
  color: #666;
  font-size: 16px;
}
</style>
