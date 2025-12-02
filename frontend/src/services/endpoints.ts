export const endpoints = {
  auth: {
    login: '/auth/login',
    register: '/auth/register',
    profile: '/users/me'
  },
  admin: {
    problems: '/admin/problems',
    // 管理员 / 老师自己的考试列表，对应后端 ExamController.getMyExams: GET /api/exams/my
    exams: '/exams/my',
    statistics: '/admin/statistics',
    users: '/admin/users'
  },
  exams: {
    list: '/exams',
    detail: (id: string | number) => `/exams/${id}`
  },
  submissions: {
    list: '/submissions',
    detail: (id: string | number) => `/submissions/${id}`
  },
  statistics: {
    student: '/statistics/student'
  }
}

