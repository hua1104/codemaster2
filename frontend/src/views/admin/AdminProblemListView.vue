<template>
  <AppCard title="题库管理">
    <template #actions>
      <button class="primary" type="button">新建题目</button>
    </template>
    <table class="table">
      <thead>
        <tr>
          <th>标题</th>
          <th>难度</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="problem in problems" :key="problem.id">
          <td>{{ problem.title }}</td>
          <td>{{ problem.difficulty }}</td>
          <td>{{ problem.createdAt }}</td>
          <td>
            <button type="button">编辑</button>
            <button type="button">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
  </AppCard>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useProblemStore } from '@/store/problem'
import AppCard from '@/components/common/AppCard.vue'

const problemStore = useProblemStore()

const problems = computed(() => problemStore.problems)

onMounted(() => {
  if (!problems.length) {
    problemStore.fetchProblems()
  }
})
</script>

<style scoped>
.primary {
  padding: 8px 12px;
  background: var(--color-primary);
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

