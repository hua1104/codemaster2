export interface Submission {
  id: number
  problemTitle: string
  status: string
  score: number
  submittedAt: string
}

export interface SubmissionDetail extends Submission {
  details: Array<{
    testCaseId: number
    status: string
    executionTime: number
  }>
  sourceCode: string
}

