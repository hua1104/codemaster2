export interface AdminOverview {
  activeExams: number
  problemCount: number
  todaySubmissions: number
}

export interface ExamPerformanceTrend {
  date: string
  averageScore: number
  submissionCount: number
}

export interface StudentSummary {
  upcoming: number
  submitted: number
  averageScore: number
}

