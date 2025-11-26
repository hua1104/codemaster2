<template>
  <AuthForm submit-label="登录" @submit="onSubmit">
    <TextField v-model="credentials.username" label="用户名" placeholder="请输入用户名" />
    <TextField
      v-model="credentials.password"
      label="密码"
      type="password"
      placeholder="请输入密码"
    />
    <RouterLink class="link" to="/auth/register">没有账号？点击注册</RouterLink>
  </AuthForm>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useAuthStore } from '@/store/auth'
import AuthForm from '@/components/forms/AuthForm.vue'
import TextField from '@/components/forms/TextField.vue'

const authStore = useAuthStore()

const credentials = reactive({
  username: '',
  password: ''
})

async function onSubmit() {
  await authStore.login(credentials)
}
</script>

<style scoped>
.link {
  color: var(--color-primary);
  text-align: right;
  font-size: 14px;
}
</style>

