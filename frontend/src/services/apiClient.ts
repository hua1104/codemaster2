import axios from 'axios'

// 在开发环境下统一走 Vite 代理：/api -> http://localhost:8081/api
// 避免直接请求 8081 导致浏览器 CORS 报错
const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000
})

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      void import('@/store/auth').then(({ useAuthStore }) => {
        const authStore = useAuthStore()
        authStore.logout()
      })
    }
    return Promise.reject(error)
  }
)

export default apiClient

