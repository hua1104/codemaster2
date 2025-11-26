export const endpoints = {
  auth: {
    login: '/auth/login',
    register: '/auth/register',
    profile: '/users/me'
  },
  admin: {
    problems: '/admin/problems',
    exams: '/admin/exams',
    statistics: '/admin/statistics'
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

