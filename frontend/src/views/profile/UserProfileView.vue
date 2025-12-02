<template>
  <section class="page">
    <h2>个人信息</h2>
    <AppCard>
      <ul class="info">
        <li>
          <span>用户名</span>
          <strong>{{ profile.username }}</strong>
        </li>
        <li>
          <span>邮箱</span>
          <strong>{{ profile.email }}</strong>
        </li>
        <li>
          <span>角色</span>
          <strong>{{ profile.role }}</strong>
        </li>
        <li v-if="profile.studentId">
          <span>学号</span>
          <strong>{{ profile.studentId }}</strong>
        </li>
      </ul>
    </AppCard>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import AppCard from '@/components/common/AppCard.vue'
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()

const profile = computed(() => authStore.profile)

onMounted(() => {
  authStore.fetchProfile()
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info li {
  display: flex;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: 10px;
  background: #f8fafc;
}
</style>

