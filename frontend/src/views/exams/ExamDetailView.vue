<template>
  <section class="page" v-if="exam">
    <header>
      <h2>{{ exam.title }}</h2>
      <p class="meta">
        <span>开始时间：{{ exam.startTime }}</span>
        <span>时长：{{ exam.duration }}</span>
        <span>总题目：{{ exam.problemCount }}</span>
      </p>
    </header>

    <AppCard title="考试说明">
      <p>{{ exam.description }}</p>
    </AppCard>

    <AppCard title="题目列表">
      <ol class="problem-list">
        <li v-for="problem in exam.problems" :key="problem.id">
          <div>
            <strong>{{ problem.title }}</strong>
            <span>难度：{{ problem.difficulty }}</span>
          </div>
          <RouterLink
            :to="`/dashboard/submissions?examId=${exam.id}&problemId=${problem.id}`"
            class="primary"
          >
            开始答题
          </RouterLink>
        </li>
      </ol>
    </AppCard>
  </section>
  <section v-else class="empty">正在加载考试详情...</section>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import AppCard from '@/components/common/AppCard.vue'
import { useExamStore } from '@/store/exam'

const route = useRoute()
const examStore = useExamStore()

const examId = route.params.id as string

const exam = computed(() => examStore.currentExam)

onMounted(() => {
  examStore.fetchExamDetail(examId)
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
  gap: 18px;
  flex-wrap: wrap;
  color: #6b7280;
}

.problem-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.problem-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.primary {
  color: #ffffff;
  background: var(--color-primary);
  padding: 8px 14px;
  border-radius: 8px;
}

.empty {
  padding: 60px 16px;
  text-align: center;
  color: #6b7280;
}
</style>

