<template>
  <AppCard title="考试安排">
    <template #actions>
      <button class="primary" type="button">创建考试</button>
    </template>
    <table class="table">
      <thead>
        <tr>
          <th>考试名称</th>
          <th>时间</th>
          <th>状态</th>
          <th>考生人数</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="exam in exams" :key="exam.id">
          <td>{{ exam.title }}</td>
          <td>{{ exam.duration }}</td>
          <td>{{ exam.status }}</td>
          <td>{{ exam.registeredCount }}</td>
        </tr>
      </tbody>
    </table>
  </AppCard>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useExamStore } from '@/store/exam'
import AppCard from '@/components/common/AppCard.vue'

const examStore = useExamStore()

const exams = computed(() => examStore.exams)

onMounted(() => {
  if (!exams.length) {
    examStore.fetchExams()
  }
})
</script>

<style scoped>
.primary {
  padding: 8px 12px;
  background: var(--color-accent);
  border: none;
  border-radius: 8px;
  color: #ffffff;
  cursor: pointer;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 12px;
  text-align: left;
}

tbody tr:nth-child(even) {
  background: #f8fafc;
}
</style>

