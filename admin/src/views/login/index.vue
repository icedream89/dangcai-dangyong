<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1 class="title">当才当用</h1>
        <p class="subtitle">当阳市科技经济信息商务局 - 企业服务平台</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p>演示账号：admin / 123456</p>
      </div>
    </div>

    <div class="login-bg">
      <div class="bg-overlay"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: '123456',
  remember: true
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true

  setTimeout(() => {
    // 模拟登录
    userStore.login({
      id: 1,
      username: loginForm.username,
      realName: '管理员',
      role: 'admin',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    })

    ElMessage.success('登录成功')
    router.push('/')
    loading.value = false
  }, 1000)
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  position: relative;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: 0;

  .bg-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('https://images.unsplash.com/photo-1497366216548-37526070297c?w=1920');
    background-size: cover;
    background-position: center;
    opacity: 0.15;
  }
}

.login-box {
  position: relative;
  z-index: 1;
  width: 450px;
  padding: 50px 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .title {
    font-size: 36px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
  }

  .subtitle {
    font-size: 14px;
    color: #666;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }

  .login-btn {
    width: 100%;
    height: 45px;
    font-size: 16px;
    font-weight: 500;
  }
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #999;
  font-size: 13px;
}
</style>
