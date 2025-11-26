<template>
  <section class="dashboard">
    <h2>系统概览</h2>
    <div class="tiles">
      <StatTile label="正在进行的考试" :value="stats.activeExams" accent="rgba(37,99,235,0.15)">
        <template #icon>🧾</template>
      </StatTile>
      <StatTile label="题库数量" :value="stats.problemCount" accent="rgba(16,185,129,0.15)">
        <template #icon>📚</template>
      </StatTile>
      <StatTile label="今日提交" :value="stats.todaySubmissions" accent="rgba(249,115,22,0.15)">
        <template #icon>🚀</template>
      </StatTile>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import StatTile from '@/components/widgets/StatTile.vue'
import { useStatisticsStore } from '@/store/statistics'

const statisticsStore = useStatisticsStore()

const stats = computed(() => statisticsStore.adminOverview)

onMounted(() => {
  statisticsStore.fetchAdminOverview()
})
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.tiles {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 18px;
}
</style>

