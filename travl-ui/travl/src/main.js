import './assets/main.css'
import 'element-plus/dist/index.css'
import Vuex from 'vuex'
import store from './store'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router).use(ElementPlus)

app.use(Vuex)
app.use(store)

// 初始化时检查token有效性
const initApp = async () => {
  // 检查store中的token有效性
  if (!store.state.user.isAuthenticated) {
    // 无效token则跳转登录
    router.push('/login')
  }
  app.mount('#app')
}

initApp()