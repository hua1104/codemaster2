<template>
  <section class="dashboard">
    <h2>考试进度</h2>
    <div class="grid">
      <StatTile label="待参加考试" :value="stats.upcoming" accent="rgba(37,99,235,0.12)">
        <template #icon>⏳</template>
      </StatTile>
      <StatTile label="已提交" :value="stats.submitted" accent="rgba(16,185,129,0.12)">
        <template #icon>✅</template>
      </StatTile>
      <StatTile label="平均得分" :value="stats.averageScore" accent="rgba(249,115,22,0.12)">
        <template #icon>🎯</template>
      </StatTile>
    </div>
    <AppCard title="近期考试提醒">
      <ul class="exam-list">
        <li v-for="exam in upcomingExams" :key="exam.id">
          <div>
            <strong>{{ exam.title }}</strong>
            <span>{{ exam.startTime }}</span>
          </div>
          <RouterLink :to="`/dashboard/exams/${exam.id}`">进入考试</RouterLink>
        </li>
      </ul>
    </AppCard>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useExamStore } from '@/store/exam'
import { useStatisticsStore } from '@/store/statistics'
import AppCard from '@/components/common/AppCard.vue'
import StatTile from '@/components/widgets/StatTile.vue'

const examStore = useExamStore()
const statisticsStore = useStatisticsStore()

const stats = computed(() => statisticsStore.studentSummary)
const upcomingExams = computed(() => examStore.upcomingExams.slice(0, 5))

onMounted(() => {
  examStore.fetchExams()
  statisticsStore.fetchStudentSummary()
})
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 18px;
}

.exam-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

li span {
  color: #6b7280;
}
</style>

