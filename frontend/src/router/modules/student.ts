// frontend/src/router/modules/student.ts (修正后的代码)

import type { RouteRecordRaw } from 'vue-router'
import StudentLayout from '@/layouts/StudentLayout.vue'

const studentRoutes: RouteRecordRaw[] = [
  {
    path: '/dashboard', // 🚨 修复点 1: 父路由路径改为 /dashboard
    component: StudentLayout,
    redirect: { name: 'StudentDashboard' }, // 重定向到子路由名
    meta: { requiresAuth: true, role: 'STUDENT' }, 
    children: [
      {
        path: '', // 🚨 修复点 2: 仪表盘路径改为空字符串，继承父路径
        name: 'StudentDashboard',
        component: () => import('@/views/student/StudentDashboardView.vue'),
        meta: { title: '学生仪表盘' }
      },
      // 考试相关
      {
        path: 'exams', // 完整路径: /dashboard/exams
        name: 'StudentExamList',
        component: () => import('@/views/student/ExamListView.vue'), 
        meta: { title: '我的考试' }
      },
      {
        path: 'exams/:id', // 完整路径: /dashboard/exams/:id
        name: 'StudentExamDetail',
        component: () => import('@/views/student/ExamDetailView.vue'), 
        props: true,
        meta: { title: '考试详情' }
      },
      // 提交历史
      {
        path: 'submissions', // 完整路径: /dashboard/submissions
        name: 'StudentSubmissionList',
        component: () => import('@/views/student/SubmissionListView.vue'), 
        meta: { title: '提交历史' }
      },
      // 个人资料
      {
        path: 'profile', // 完整路径: /dashboard/profile
        name: 'StudentProfile',
        component: () => import('@/views/student/UserProfileView.vue'), 
        meta: { title: '个人资料' }
      }
    ]
  }
]

export default studentRoutes
