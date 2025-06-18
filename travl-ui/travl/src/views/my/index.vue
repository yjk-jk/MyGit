<template>
  <div class="my-container">
    <div class="user-info">
      <div class="avatar">
        <img src="@/assets/画画.png" alt="用户头像" />
      </div>
      <div class="info">
        <h2>{{ username }}</h2>
        <p>ID: {{ userId }}</p>
      </div>
    </div>

    <div class="menu-list">
      <div class="menu-item" @click="router.push('collection')">
        <span>我的收藏</span>
        <i class="el-icon-arrow-right"></i>
      </div>
      <div class="menu-item" @click="router.push('setting')">
        <span>设置</span>
        <i class="el-icon-arrow-right"></i>
      </div>
      <div class="menu-item" @click="router.push('aboutme')">
        <span>关于我们</span>
        <i class="el-icon-arrow-right"></i>
      </div>
    </div>

    <button class="logout-btn" @click="handleLogout">退出登录</button>
  </div>
  <Footer />
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { UserLoginOut } from '@/api/user'
import Footer from '@/display/footer/index.vue'
const store = useStore()
const router = useRouter()

const username = computed(() => store.state.user.username)
const userId = computed(() => store.state.user.userId)

const handleLogout = async () => {
  try {
    await UserLoginOut({ username: store.state.user.username, userId: store.state.user.userId })
    store.dispatch('logout')
    router.push('/login')
  } catch (error) {
    console.error('登出失败:', error)
  }
}
</script>

<style scoped>
.my-container {
  padding: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.info p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #999;
}

.menu-list {
  background-color: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item span {
  font-size: 16px;
  color: #333;
}

.logout-btn {
  width: 100%;
  margin-top: 20px;
  padding: 12px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #f78989;
}
</style>
