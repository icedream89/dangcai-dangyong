<template>
  <view class="login-page">
    <view class="login-container">
      <!-- Logo -->
      <view class="logo-section">
        <view class="logo-icon">🏢</view>
        <text class="app-name">当才当用</text>
        <text class="app-desc">企业服务平台</text>
      </view>

      <!-- 角色选择 -->
      <view class="role-section">
        <text class="section-title">选择登录角色</text>

        <view
          class="role-card"
          :class="{ active: selectedRole === 'enterprise' }"
          @click="selectRole('enterprise')"
        >
          <view class="role-icon">💼</view>
          <view class="role-info">
            <text class="role-name">企业管理员</text>
            <text class="role-desc">可发布产品和编辑企业信息</text>
          </view>
          <view class="role-check" v-if="selectedRole === 'enterprise'">✓</view>
        </view>

        <view
          class="role-card"
          :class="{ active: selectedRole === 'employee' }"
          @click="selectRole('employee')"
        >
          <view class="role-icon">👨‍💼</view>
          <view class="role-info">
            <text class="role-name">企业员工</text>
            <text class="role-desc">可发布产品和编辑企业信息</text>
          </view>
          <view class="role-check" v-if="selectedRole === 'employee'">✓</view>
        </view>

        <view
          class="role-card"
          :class="{ active: selectedRole === 'normal' }"
          @click="selectRole('normal')"
        >
          <view class="role-icon">👤</view>
          <view class="role-info">
            <text class="role-name">普通用户</text>
            <text class="role-desc">浏览信息，联系企业</text>
          </view>
          <view class="role-check" v-if="selectedRole === 'normal'">✓</view>
        </view>
      </view>

      <!-- 登录按钮 -->
      <button class="login-btn" @click="handleLogin" :disabled="!selectedRole || logging">
        {{ logging ? '登录中...' : '微信授权登录' }}
      </button>

      <!-- 说明文字 -->
      <text class="tips">登录即表示同意《用户协议》和《隐私政策》</text>
    </view>
  </view>
</template>

<script>
import { userStore, mockUsers } from '@/store/user.js'

export default {
  data() {
    return {
      selectedRole: '',
      logging: false
    }
  },
  methods: {
    selectRole(role) {
      this.selectedRole = role
    },

    async handleLogin() {
      if (!this.selectedRole) {
        return uni.showToast({
          title: '请选择登录角色',
          icon: 'none'
        })
      }

      this.logging = true

      // 模拟微信登录
      setTimeout(() => {
        // 根据选择的角色获取对应的用户信息
        const user = mockUsers.find(u => u.role === this.selectedRole)

        if (user) {
          userStore.login(user)

          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })

          setTimeout(() => {
            // 返回上一页或跳转到首页
            const pages = getCurrentPages()
            if (pages.length > 1) {
              uni.navigateBack()
            } else {
              uni.switchTab({
                url: '/pages/index/index'
              })
            }
          }, 1500)
        }

        this.logging = false
      }, 1000)
    }
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-container {
  width: 100%;
}

.logo-section {
  text-align: center;
  margin-bottom: 60rpx;

  .logo-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
  }

  .app-name {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 12rpx;
  }

  .app-desc {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.role-section {
  .section-title {
    display: block;
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
    margin-bottom: 30rpx;
  }

  .role-card {
    display: flex;
    align-items: center;
    gap: 20rpx;
    background: #fff;
    border-radius: 20rpx;
    padding: 32rpx 24rpx;
    margin-bottom: 20rpx;
    position: relative;
    transition: all 0.3s;

    &.active {
      background: #f0f0f0;
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
    }

    .role-icon {
      font-size: 60rpx;
      flex-shrink: 0;
    }

    .role-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 8rpx;

      .role-name {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
      }

      .role-desc {
        font-size: 24rpx;
        color: #999;
      }
    }

    .role-check {
      width: 48rpx;
      height: 48rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
      font-weight: bold;
      flex-shrink: 0;
    }
  }
}

.login-btn {
  width: 100%;
  height: 96rpx;
  background: #fff;
  color: #667eea;
  border: none;
  border-radius: 50rpx;
  font-size: 32rpx;
  font-weight: bold;
  margin-top: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);

  &[disabled] {
    opacity: 0.6;
  }
}

.tips {
  display: block;
  text-align: center;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 40rpx;
}
</style>
