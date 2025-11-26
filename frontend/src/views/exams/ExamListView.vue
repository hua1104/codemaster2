<template>
  <section class="page">
    <h2>考试列表</h2>
    <div class="list">
      <AppCard v-for="exam in exams" :key="exam.id" :title="exam.title">
        <p>发布时间：{{ exam.startTime }}</p>
        <p>时长：{{ exam.duration }}</p>
        <p>状态：{{ exam.status }}</p>
        <template #footer>
          <RouterLink :to="`/dashboard/exams/${exam.id}`" class="primary">查看详情</RouterLink>
        </template>
      </AppCard>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import AppCard from '@/components/common/AppCard.vue'
import { useExamStore } from '@/store/exam'

const examStore = useExamStore()

const exams = computed(() => examStore.exams)

onMounted(() => {
  examStore.fetchExams()
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
}

.primary {
  color: #ffffff;
  background: var(--color-primary);
  padding: 8px 16px;
  border-radius: 8px;
}
</style>

