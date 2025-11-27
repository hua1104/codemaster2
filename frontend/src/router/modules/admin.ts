import type { RouteRecordRaw } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'

const adminRoutes: RouteRecordRaw[] = [
    {
        path: '/admin',
        component: AdminLayout,
        redirect: '/admin/dashboard',
        meta: { requiresAuth: true, role: 'ADMIN' }, // 添加权限要求
        children: [
            // 仪表盘
            {
                path: 'dashboard',
                name: 'AdminDashboard',
                component: () => import('@/views/admin/AdminDashboardView.vue'),
                meta: { title: '仪表盘' }
            },

            // ===================================
            // 🚨 补充：考试管理路由 🚨
            // ===================================
            {
                path: 'exams', // 路径最终为 /admin/exams
                name: 'AdminExamList',
                component: () => import('@/views/admin/AdminExamListView.vue'), // 确保文件名和路径匹配
                meta: { title: '考试列表' }
            },
            {
                path: 'exam/create',
                name: 'AdminExamCreate',
                component: () => import('@/views/admin/AdminExamFormView.vue'),
                meta: { title: '创建考试' }
            },
            {
                path: 'exam/:id', // 路径最终为 /admin/exam/:id
                name: 'AdminExamEdit',
                component: () => import('@/views/admin/AdminExamFormView.vue'),
                props: true,
                meta: { title: '编辑考试' }
            },
            
            // ===================================
            // 题库管理 (您已有的部分)
            // ===================================
            {
                path: 'problems',
                name: 'AdminProblemList',
                component: () => import('@/views/admin/AdminProblemListView.vue'),
                meta: { title: '题目列表' }
            },
            {
                path: 'problem/create',
                name: 'AdminProblemCreate',
                component: () => import('@/views/admin/AdminProblemFormView.vue'),
                meta: { title: '创建题目' }
            },
            {
                path: 'problem/:id', 
                name: 'AdminProblemEdit',
                component: () => import('@/views/admin/AdminProblemFormView.vue'),
                props: true,
                meta: { title: '编辑题目' }
            },
            
            // ===================================
            // 用户管理 (您已有的部分)
            // ===================================
            {
                path: 'users',
                name: 'AdminUserList',
                component: () => import('@/views/admin/AdminUserListView.vue'),
                meta: { title: '用户列表' }
            }
        ]
    }
]

export default adminRoutes
