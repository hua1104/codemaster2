import { defineStore } from 'pinia'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { Problem } from '@/types/exam'

interface ProblemState {
  problems: Problem[]
  loading: boolean
}

export const useProblemStore = defineStore('problem', {
  state: (): ProblemState => ({
    problems: [],
    loading: false
  }),
  actions: {
    async fetchProblems() {
      if (this.loading) return
      this.loading = true
      try {
        const { data } = await apiClient.get<Problem[]>(endpoints.admin.problems)
        this.problems = data
      } finally {
        this.loading = false
      }
    }
  }
})

