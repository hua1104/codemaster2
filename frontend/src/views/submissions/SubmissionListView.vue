<template>
  <section class="page">
    <h2>我的提交</h2>
    <AppCard>
      <table class="table">
        <thead>
          <tr>
            <th>题目</th>
            <th>状态</th>
            <th>得分</th>
            <th>提交时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="submission in submissions" :key="submission.id">
            <td>{{ submission.problemTitle }}</td>
            <td>{{ submission.status }}</td>
            <td>{{ submission.score }}</td>
            <td>{{ submission.submittedAt }}</td>
            <td>
              <RouterLink :to="`/dashboard/submissions/${submission.id}`">查看详情</RouterLink>
            </td>
          </tr>
        </tbody>
      </table>
    </AppCard>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import AppCard from '@/components/common/AppCard.vue'
import { useSubmissionStore } from '@/store/submission'

const submissionStore = useSubmissionStore()

const submissions = computed(() => submissionStore.submissions)

onMounted(() => {
  submissionStore.fetchSubmissions()
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 24px;
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

