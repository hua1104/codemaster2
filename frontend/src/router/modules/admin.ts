import type { RouteRecordRaw } from 'vue-router'

const adminRoutes: RouteRecordRaw[] = [
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/AdminDashboardView.vue')
      },
      {
        path: 'problems',
        name: 'AdminProblemList',
        component: () => import('@/views/admin/AdminProblemListView.vue')
      },
      {
        path: 'exams',
        name: 'AdminExamList',
        component: () => import('@/views/admin/AdminExamListView.vue')
      },
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/statistics/AdminStatisticsView.vue')
      }
    ]
  }
]

export default adminRoutes

