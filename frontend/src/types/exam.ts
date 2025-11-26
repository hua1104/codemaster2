export interface Problem {
  id: number
  title: string
  difficulty: string
  createdAt?: string
}

export interface Exam {
  id: number
  title: string
  duration: string
  status: 'UPCOMING' | 'ONGOING' | 'FINISHED'
}

export interface ExamDetail extends Exam {
  description: string
  startTime: string
  problemCount: number
  problems: Problem[]
}

