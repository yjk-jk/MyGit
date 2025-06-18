import { createStore } from 'vuex'
import router from '../router'

// 验证token有效性
const validateToken = (token) => {
  if (!token) return false
  
  // 检查token格式（示例：JWT包含3部分）
  const parts = token.split('.')
  if (parts.length !== 3) return false
  
  // 可以添加更多验证逻辑，如：
  // 1. 检查过期时间（如果是JWT）
  // 2. 必要时调用API验证
  
  return true
}

// 从localStorage初始化状态
const initState = () => {
  const token = localStorage.getItem('token') || null
  const username = localStorage.getItem('username') || ''
  const userId = localStorage.getItem('userId') || null
  const role = localStorage.getItem('role') || ''
  
  return {
    user: {
      token,
      username,
      userId,
      role,
      isAuthenticated: validateToken(token)
    }
  }
}

export default createStore({
  state: initState(),
  mutations: {
    SET_USER(state, userData) {
      if (!userData.token || !validateToken(userData.token)) {
        // 清除用户状态
        state.user = {
          token: null,
          username: '',
          userId: null,
          isAuthenticated: false,
          role: ''
        }
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        // 跳转到登录页面
        router.push('/login')
        return
      }

      state.user = {
        ...state.user,
        ...userData,
        isAuthenticated: true
      }
      // 持久化到localStorage
      localStorage.setItem('token', userData.token)
      localStorage.setItem('userId', userData.userId)
      localStorage.setItem('username', userData.username)
    },
    CLEAR_USER(state) {
      state.user = {
        token: null,
        username: '',
        userId: null,
        role: '',
        isAuthenticated: false
      }
      // 清除localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
    }
  },
  actions: {
    login({ commit }, userData) {
      commit('SET_USER', userData)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  },
  getters: {
    currentUser: state => state.user,
    isAuthenticated: state => state.user.isAuthenticated
  }
})
