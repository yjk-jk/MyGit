import axios from 'axios'
import { ElMessage } from 'element-plus' // 导入 ElMessage 组件
import router from '@/router' // 假设你的router文件路径是'@/router'
import store from '@/store'

// 创建axios实例
const service = axios.create({
  baseURL: 'api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  withCredentials: true // 确保跨域请求携带凭据
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 添加token认证信息
    const token = store.state.user?.token
    
    if (token) {
      // 确保headers存在
      config.headers = config.headers || {}
      // 特殊处理FormData请求
      if (config.data instanceof FormData) {
        config.headers['Content-Type'] = 'multipart/form-data'
      }
      // 添加Authorization头
      config.headers['Authorization'] = `Bearer ${token}`
    } else {
      console.warn('No token found in store')
    }
    return config
  },
  (error) => {
    ElMessage.error('请求发送失败')
    console.error('请求发送失败', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response
    if(res.code ==="401"){
      ElMessage.error("用户未登录，请先登录！")
      router.push('/login') // 跳转到登录页面
      return Promise.reject(new Error("用户未登录，请先登录！"))
    }
   
    return res
  },
  (error) => {
    console.log('err', error) // 修正这里，使用逗号分隔字符串
    console.error('响应错误', error.response.data.message)
    let { message } = error.response.data.message
    if (error.response) {
      // 服务器响应错误
      router.push('/login') // 跳转到登录页面
      message = `${error.response.data.code} ${error.response.data.message}`
    } else if (error.request) {
      // 请求发送但没有收到响应
      message = '请求超时或没有收到服务器响应'
    } else {
      // 其他错误
      message = error.message
    }
    if (message.includes('timeout')) {
      message = '请求超时'
    } else if (message.includes('Network Error')) {
      message = '网络错误'
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)


export default service
