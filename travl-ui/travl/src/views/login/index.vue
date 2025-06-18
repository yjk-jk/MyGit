<template>
  <div class="login-container">
    <h1 class="title">欢迎登录旅游管理系统</h1>
    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label for="username">用户名</label>
        <input id="username" v-model="username" type="text" required placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input id="password" v-model="password" type="password" required placeholder="请输入密码" />
      </div>
      <button type="submit" class="login-btn">登录</button>
    </form>
    <p class="register-link">还没有账号？<router-link to="/register">立即注册</router-link></p>
  </div>
  <Footer />
</template>

<script>
import { UserLogin } from '@/api/user'
import { ElMessage } from 'element-plus' // 导入 ElMessage 组件
import Footer from '@/display/footer/index.vue'
export default {
  name: 'LoginView',
  data() {
    return {
      username: '',
      password: '',
    }
  },
  components: {
    Footer,
  },
  methods: {
    async handleLogin() {
      try {
        const response = await UserLogin({
          username: this.username,
          password: this.password,
        })

        // 存储token和用户信息
        this.$store.commit('SET_USER', {
          token: response.data.data.token,
          username: this.username,
          userId: response.data.data.user.id,
        })

        // 跳转到首页或原请求页面
        ElMessage.success('登录成功') // 使用 ElMessage.success
        const redirect = this.$route.query.redirect || '/'
        this.$router.push(redirect)
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败') // 使用 ElMessage.error
      }
    },
  },
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background: var(--color-background-soft);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.title {
  color: var(--color-heading);
  text-align: center;
  margin-bottom: 2rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  color: var(--color-text);
  font-size: 0.9rem;
}

.form-group input {
  padding: 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: hsla(160, 100%, 37%, 1);
}

.login-btn {
  padding: 0.75rem;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: hsla(160, 100%, 37%, 0.8);
}

.register-link {
  text-align: center;
  margin-top: 1.5rem;
  color: var(--color-text);
}

.register-link a {
  color: hsla(160, 100%, 37%, 1);
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
