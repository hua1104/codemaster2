import MockAdapter from 'axios-mock-adapter'
import apiClient from '@/services/apiClient'
import { endpoints } from '@/services/endpoints'
import type { ExamDetail } from '@/types/exam'

export function bootstrapMocks() {
  const mock = new MockAdapter(apiClient, { delayResponse: 600 })

  const demoExam: ExamDetail = {
    id: 1,
    title: 'Java 基础测试',
    status: 'UPCOMING',
    duration: '90 分钟',
    description: '覆盖 Java 基础语法、集合、并发等知识点。',
    startTime: '2025-11-30 09:00',
    problemCount: 3,
    problems: [
      { id: 11, title: '反转链表', difficulty: 'MEDIUM' },
      { id: 12, title: 'LRU 缓存', difficulty: 'HARD' },
      { id: 13, title: '统计字符串', difficulty: 'EASY' }
    ]
  }

  mock.onGet(endpoints.exams.list).reply(200, [
    demoExam,
    {
      id: 2,
      title: '算法综合练习',
      status: 'ONGOING',
      duration: '120 分钟'
    }
  ])

  mock.onGet(endpoints.exams.detail('1')).reply(200, demoExam)
}

