import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref('demo-token')
  const userInfo = ref({
    id: 1,
    username: 'admin',
    realName: '管理员',
    role: 'admin',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  })

  const isLogin = ref(true)

  function login(data: any) {
    token.value = 'demo-token'
    isLogin.value = true
    userInfo.value = data
  }

  function logout() {
    token.value = ''
    isLogin.value = false
  }

  return {
    token,
    userInfo,
    isLogin,
    login,
    logout
  }
})
