import { defineStore } from 'pinia'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { Submission, SubmissionDetail } from '@/types/submission'

interface SubmissionState {
  submissions: Submission[]
  currentSubmission: SubmissionDetail | null
  loading: boolean
}

export const useSubmissionStore = defineStore('submission', {
  state: (): SubmissionState => ({
    submissions: [],
    currentSubmission: null,
    loading: false
  }),
  actions: {
    async fetchSubmissions() {
      this.loading = true
      try {
        const { data } = await apiClient.get<Submission[]>(endpoints.submissions.list)
        this.submissions = data
      } finally {
        this.loading = false
      }
    },
    async fetchSubmissionDetail(id: string) {
      this.loading = true
      try {
        const { data } = await apiClient.get<SubmissionDetail>(endpoints.submissions.detail(id))
        this.currentSubmission = data
      } finally {
        this.loading = false
      }
    }
  }
})

