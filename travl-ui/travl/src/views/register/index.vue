<template>
  <div class="register-container">
    <h1 class="title">注册新账号</h1>
    <el-form
      :model="registerForm"
      :rules="rules"
      ref="registerFormRef"
      class="register-form"
      @submit.prevent="handleRegister"
    >
      <el-form-item prop="username">
        <el-input
          v-model="registerForm.username"
          placeholder="请输入用户名"
          prefix-icon="el-icon-user"
        />
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          placeholder="请输入密码"
          prefix-icon="el-icon-lock"
          show-password
        />
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          placeholder="请确认密码"
          prefix-icon="el-icon-lock"
          show-password
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" native-type="submit" class="register-btn" :loading="loading">
          注册
        </el-button>
      </el-form-item>
    </el-form>
    <p class="login-link">已有账号？<router-link to="/login">立即登录</router-link></p>
  </div>
  <Footer />
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { UserRegister } from '@/api/user'
import Footer from '@/display/footer/index.vue'
const router = useRouter()
const loading = ref(false)
const registerFormRef = ref(null)

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
})

const validatePassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' },
  ],
}

const handleRegister = () => {
  registerFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const res = await UserRegister({
        username: registerForm.value.username,
        password: registerForm.value.password,
      })
      let code = res.data.code
      if (code === 200) {
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } else {
        ElMessage.error(res.data.msg)
      }
    } catch (error) {
      console.error('注册失败:', error)
      ElMessage.error('注册失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.register-container {
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

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.register-btn {
  width: 100%;
  padding: 0.75rem;
}

.login-link {
  text-align: center;
  margin-top: 1.5rem;
  color: var(--color-text);
}

.login-link a {
  color: hsla(160, 100%, 37%, 1);
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
