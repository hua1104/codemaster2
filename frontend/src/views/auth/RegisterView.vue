<template>
  <AuthForm submit-label="注册" @submit="onSubmit">
    <TextField v-model="form.username" label="用户名" placeholder="设置登录名" />
    <TextField v-model="form.password" label="密码" type="password" placeholder="设置密码" />
    <TextField v-model="form.email" label="邮箱" type="email" placeholder="接收通知的邮箱" />
    <TextField v-model="form.studentId" label="学号" placeholder="学生用户必填" />
    <TextField v-model="form.realName" label="真实姓名" placeholder="学生用户必填" />
    <TextField v-model="form.className" label="班级" placeholder="学生用户必填" />
    <div class="role-switch">
      <label>
        <input v-model="form.role" type="radio" value="STUDENT" />
        学生
      </label>
      <label>
        <input v-model="form.role" type="radio" value="ADMIN" />
        管理员
      </label>
    </div>
  </AuthForm>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import AuthForm from '@/components/forms/AuthForm.vue'
import TextField from '@/components/forms/TextField.vue'
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: '',
  email: '',
  studentId: '',
  realName: '',
  className: '',
  role: 'STUDENT'
})

async function onSubmit() {
  await authStore.register(form)
}
</script>

<style scoped>
.role-switch {
  display: flex;
  gap: 24px;
  justify-content: center;
}
</style>

