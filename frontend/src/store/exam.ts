import { defineStore } from 'pinia'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { Exam, ExamDetail } from '@/types/exam'

interface ExamState {
  exams: Exam[]
  currentExam: ExamDetail | null
  loading: boolean
}

export const useExamStore = defineStore('exam', {
  state: (): ExamState => ({
    exams: [],
    currentExam: null,
    loading: false
  }),
  getters: {
    upcomingExams: (state) => state.exams.filter((exam) => exam.status === 'UPCOMING')
  },
  actions: {
    async fetchExams() {
      if (this.loading) return
      this.loading = true
      try {
        const { data } = await apiClient.get<Exam[]>(endpoints.exams.list)
        this.exams = data
      } finally {
        this.loading = false
      }
    },
    async fetchExamDetail(id: string) {
      this.loading = true
      try {
        const { data } = await apiClient.get<ExamDetail>(endpoints.exams.detail(id))
        this.currentExam = data
      } finally {
        this.loading = false
      }
    }
  }
})

