import type { RouteRecordRaw } from 'vue-router'

const studentRoutes: RouteRecordRaw[] = [
  {
    path: '/dashboard',
    component: () => import('@/layouts/StudentLayout.vue'),
    children: [
      {
        path: '',
        name: 'StudentDashboard',
        component: () => import('@/views/student/StudentDashboardView.vue')
      },
      {
        path: 'exams',
        name: 'StudentExamList',
        component: () => import('@/views/exams/ExamListView.vue')
      },
      {
        path: 'exams/:id',
        name: 'StudentExamDetail',
        component: () => import('@/views/exams/ExamDetailView.vue'),
        props: true
      },
      {
        path: 'submissions',
        name: 'StudentSubmissionList',
        component: () => import('@/views/submissions/SubmissionListView.vue')
      },
      {
        path: 'submissions/:id',
        name: 'StudentSubmissionDetail',
        component: () => import('@/views/submissions/SubmissionDetailView.vue'),
        props: true
      },
      {
        path: 'profile',
        name: 'StudentProfile',
        component: () => import('@/views/profile/UserProfileView.vue')
      }
    ]
  }
]

export default studentRoutes

