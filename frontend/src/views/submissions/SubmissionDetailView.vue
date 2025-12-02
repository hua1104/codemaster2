<template>
  <section v-if="submission" class="page">
    <h2>{{ submission.problemTitle }}</h2>
    <p class="meta">
      <span>状态：{{ submission.status }}</span>
      <span>得分：{{ submission.score }}</span>
      <span>时间：{{ submission.submittedAt }}</span>
    </p>
    <AppCard title="评测详情">
      <ul class="result">
        <li v-for="detail in submission.details" :key="detail.testCaseId">
          <span>测试用例 {{ detail.testCaseId }}</span>
          <span>{{ detail.status }}</span>
          <span>{{ detail.executionTime }} ms</span>
        </li>
      </ul>
    </AppCard>
    <AppCard title="源代码">
      <pre>{{ submission.sourceCode }}</pre>
    </AppCard>
  </section>
  <section v-else class="empty">正在加载提交详情...</section>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import AppCard from '@/components/common/AppCard.vue'
import { useSubmissionStore } from '@/store/submission'

const route = useRoute()
const submissionStore = useSubmissionStore()

const submissionId = route.params.id as string

const submission = computed(() => submissionStore.currentSubmission)

onMounted(() => {
  submissionStore.fetchSubmissionDetail(submissionId)
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.meta {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  color: #6b7280;
}

.result {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.result li {
  display: flex;
  justify-content: space-between;
  background: #f8fafc;
  padding: 12px 16px;
  border-radius: 8px;
}

pre {
  background: #0f172a;
  color: #e2e8f0;
  padding: 16px;
  border-radius: 12px;
  overflow-x: auto;
}

.empty {
  padding: 60px 16px;
  text-align: center;
  color: #6b7280;
}
</style>

