import { defineStore } from 'pinia'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { AdminOverview, ExamPerformanceTrend, StudentSummary } from '@/types/statistics'

interface StatisticsState {
  adminOverview: AdminOverview
  examPerformanceTrend: ExamPerformanceTrend[]
  studentSummary: StudentSummary
}

const defaultOverview: AdminOverview = {
  activeExams: 0,
  problemCount: 0,
  todaySubmissions: 0
}

const defaultStudentSummary: StudentSummary = {
  upcoming: 0,
  submitted: 0,
  averageScore: 0
}

export const useStatisticsStore = defineStore('statistics', {
  state: (): StatisticsState => ({
    adminOverview: defaultOverview,
    examPerformanceTrend: [],
    studentSummary: defaultStudentSummary
  }),
  actions: {
    async fetchAdminOverview() {
      const { data } = await apiClient.get<{
        overview: AdminOverview
        trend: ExamPerformanceTrend[]
      }>(endpoints.admin.statistics)
      this.adminOverview = data.overview
      this.examPerformanceTrend = data.trend
    },
    async fetchStudentSummary() {
      const { data } = await apiClient.get<StudentSummary>(endpoints.statistics.student)
      this.studentSummary = data
    }
  }
})

