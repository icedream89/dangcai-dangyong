// 用户状态管理
const USER_KEY = 'user_info'

export const userStore = {
  state: {
    userInfo: null,
    isLoggedIn: false
  },

  // 初始化用户状态
  init() {
    try {
      const userInfo = uni.getStorageSync(USER_KEY)
      if (userInfo) {
        this.state.userInfo = JSON.parse(userInfo)
        this.state.isLoggedIn = true
      }
    } catch (e) {
      console.error('初始化用户状态失败', e)
    }
  },

  // 登录
  login(userInfo) {
    this.state.userInfo = userInfo
    this.state.isLoggedIn = true
    try {
      uni.setStorageSync(USER_KEY, JSON.stringify(userInfo))
    } catch (e) {
      console.error('保存用户信息失败', e)
    }
  },

  // 登出
  logout() {
    this.state.userInfo = null
    this.state.isLoggedIn = false
    try {
      uni.removeStorageSync(USER_KEY)
    } catch (e) {
      console.error('清除用户信息失败', e)
    }
  },

  // 更新用户信息
  updateUserInfo(newInfo) {
    if (this.state.userInfo) {
      this.state.userInfo = { ...this.state.userInfo, ...newInfo }
      try {
        uni.setStorageSync(USER_KEY, JSON.stringify(this.state.userInfo))
      } catch (e) {
        console.error('更新用户信息失败', e)
      }
    }
  },

  // 判断是否为企业用户
  isEnterpriseUser() {
    return this.state.userInfo && this.state.userInfo.role === 'enterprise'
  },

  // 判断是否为企业员工
  isEnterpriseEmployee() {
    return this.state.userInfo && this.state.userInfo.role === 'employee'
  },

  // 判断是否可以编辑企业信息
  canEditEnterprise() {
    return this.isEnterpriseUser() || this.isEnterpriseEmployee()
  },

  // 获取当前用户所属企业ID
  getEnterpriseId() {
    return this.state.userInfo ? this.state.userInfo.enterpriseId : null
  }
}

// Mock 数据 - 用户账户
export const mockUsers = [
  {
    id: 1,
    name: '企业管理员',
    phone: '13800138001',
    role: 'enterprise', // 企业管理员
    enterpriseId: 1,
    enterpriseName: '当阳市科技有限公司'
  },
  {
    id: 2,
    name: '企业员工',
    phone: '13800138002',
    role: 'employee', // 企业员工
    enterpriseId: 1,
    enterpriseName: '当阳市科技有限公司'
  },
  {
    id: 3,
    name: '普通用户',
    phone: '13800138003',
    role: 'normal' // 普通用户
  }
]
