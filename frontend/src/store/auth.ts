import { defineStore } from 'pinia'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { LoginPayload, RegisterPayload, UserProfile } from '@/types/auth'

interface AuthState {
  token: string | null
  profile: UserProfile | null
  loading: boolean
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token'),
    profile: null,
    loading: false
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    role: (state) => state.profile?.role
  },
  actions: {
    async login(payload: LoginPayload) {
      this.loading = true
      try {
        const { data } = await apiClient.post(endpoints.auth.login, payload)
        this.token = data.token
        localStorage.setItem('token', this.token!)
        await this.fetchProfile()
      } finally {
        this.loading = false
      }
    },
    async register(payload: RegisterPayload) {
      this.loading = true
      try {
        const { data } = await apiClient.post(endpoints.auth.register, payload)
        this.token = data.token
        localStorage.setItem('token', this.token!)
        await this.fetchProfile()
      } finally {
        this.loading = false
      }
    },
    async fetchProfile() {
      if (!this.token) {
        this.profile = null
        return
      }
      const { data } = await apiClient.get<UserProfile>(endpoints.auth.profile)
      this.profile = data
    },
    logout() {
      this.token = null
      this.profile = null
      localStorage.removeItem('token')
    }
  }
})

