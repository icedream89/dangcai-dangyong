<template>
  <view class="profile-page">
    <!-- 用户信息卡片 -->
    <view class="user-card card">
      <view class="user-avatar">
        <text class="avatar-icon">👤</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo ? userInfo.name : '未登录' }}</text>
        <text class="user-type">{{ userInfo ? formatRole(userInfo.role) : '普通用户' }}</text>
      </view>
      <button class="login-btn" v-if="!userInfo" @click="handleLogin">立即登录</button>
    </view>

    <!-- 企业专属功能 -->
    <view class="menu-container card" v-if="isEnterpriseUser()">
      <view class="menu-header">
        <text class="menu-header-title">🏢 企业管理</text>
      </view>
      <view class="menu-item" @click="handleManageProducts">
        <view class="menu-left">
          <text class="menu-icon">📦</text>
          <text class="menu-title">产品管理</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleEditContact">
        <view class="menu-left">
          <text class="menu-icon">📞</text>
          <text class="menu-title">联系方式</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleEditEnterprise">
        <view class="menu-left">
          <text class="menu-icon">🏢</text>
          <text class="menu-title">企业信息</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-container card">
      <view class="menu-header">
        <text class="menu-header-title">📋 个人中心</text>
      </view>
      <view class="menu-item" @click="navigateTo('/pages/purchase/my-list')">
        <view class="menu-left">
          <text class="menu-icon">🛒</text>
          <text class="menu-title">我的需求</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="navigateTo('/pages/help/my-list')">
        <view class="menu-left">
          <text class="menu-icon">💬</text>
          <text class="menu-title">我的求助</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleCollect">
        <view class="menu-left">
          <text class="menu-icon">⭐</text>
          <text class="menu-title">我的收藏</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleAbout">
        <view class="menu-left">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-title">关于我们</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 退出登录 -->
    <button class="logout-btn" v-if="userInfo" @click="handleLogout">退出登录</button>
  </view>
</template>

<script>
import { userStore } from '@/store/user.js'

export default {
  data() {
    return {
      userInfo: null,
      isLoggedIn: false
    }
  },
  onLoad() {
    // 初始化用户状态
    userStore.init()
    this.userInfo = userStore.state.userInfo
    this.isLoggedIn = userStore.state.isLoggedIn
  },
  onShow() {
    // 每次显示页面时刷新用户状态
    this.userInfo = userStore.state.userInfo
    this.isLoggedIn = userStore.state.isLoggedIn
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    },

    handleLogin() {
      uni.navigateTo({
        url: '/pages/login/index'
      })
    },

    handleCollect() {
      uni.showToast({
        title: '收藏功能开发中',
        icon: 'none'
      })
    },

    handleAbout() {
      uni.showModal({
        title: '关于我们',
        content: '当才当用\n当阳市科技经济信息商务局企业服务平台\n\nVersion 1.0.0',
        showCancel: false
      })
    },

    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            userStore.logout()
            this.userInfo = null
            this.isLoggedIn = false
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    },

    // 格式化角色名称
    formatRole(role) {
      const roleMap = {
        'enterprise': '企业管理员',
        'employee': '企业员工',
        'normal': '普通用户'
      }
      return roleMap[role] || '未知角色'
    },

    // 判断是否为企业用户（管理员或员工）
    isEnterpriseUser() {
      return this.userInfo && (this.userInfo.role === 'enterprise' || this.userInfo.role === 'employee')
    },

    // 判断是否为普通用户
    isNormalUser() {
      return this.userInfo && this.userInfo.role === 'normal'
    },

    // 企业管理功能
    handleManageProducts() {
      uni.navigateTo({
        url: '/pages/enterprise/products'
      })
    },

    handleEditContact() {
      uni.navigateTo({
        url: '/pages/enterprise/contact-edit'
      })
    },

    handleEditEnterprise() {
      uni.showToast({
        title: '企业信息编辑功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped lang="scss">
.profile-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 32rpx 24rpx;
  margin-bottom: 20rpx;

  .user-avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;

    .avatar-icon {
      font-size: 60rpx;
      color: #fff;
    }
  }

  .user-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8rpx;

    .user-name {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
    }

    .user-type {
      font-size: 26rpx;
      color: #999;
    }
  }

  .login-btn {
    padding: 12rpx 32rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border: none;
    border-radius: 50rpx;
    font-size: 26rpx;
  }
}

.menu-container {
  .menu-header {
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    .menu-header-title {
      font-size: 28rpx;
      font-weight: bold;
      color: #667eea;
    }
  }

  .menu-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .menu-left {
      display: flex;
      align-items: center;
      gap: 20rpx;

      .menu-icon {
        font-size: 40rpx;
      }

      .menu-title {
        font-size: 30rpx;
        color: #333;
      }
    }

    .menu-arrow {
      font-size: 40rpx;
      color: #999;
    }
  }
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  background: #fff;
  color: #f5576c;
  border: none;
  border-radius: 50rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
}
</style>
