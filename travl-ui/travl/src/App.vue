<script setup>
import { ref } from 'vue'
import { RouterView, RouterLink } from 'vue-router'

const activeIndex = ref(0)
</script>

<template>
  <div class="app-container">
    <main class="main-content">
      <RouterView />
    </main>

    <nav class="bottom-nav">
      <RouterLink to="/home" class="nav-item" @click="activeIndex = 0">
        <span>首页</span>
      </RouterLink>
      <RouterLink to="/city" class="nav-item" @click="activeIndex = 1">
        <span>城市</span>
      </RouterLink>
      <RouterLink to="/city-manage" class="nav-item" @click="activeIndex = 2">
        <span>城市管理</span>
      </RouterLink>
      <RouterLink to="/my" class="nav-item" @click="activeIndex = 3">
        <span>我的</span>
      </RouterLink>
    </nav>
  </div>
</template>

<style scoped lang="less">
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #666;
  text-decoration: none;
  padding: 8px 0;
  flex: 1;
}

.nav-item .el-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.nav-item span {
  font-size: 12px;
}

.router-link-active {
  color: #409eff;
}

.router-link-active .el-icon {
  color: #409eff;
}
</style>
