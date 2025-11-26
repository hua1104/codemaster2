<template>
  <AppCard title="数据统计概览">
    <div class="stats">
      <div v-for="item in chartData" :key="item.label" class="bagde">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </div>
    </div>
    <div class="hint">
      * 图表区域预留，用于集成 ECharts 或 AntV 等可视化组件
    </div>
  </AppCard>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import AppCard from '@/components/common/AppCard.vue'
import { useStatisticsStore } from '@/store/statistics'

const statisticsStore = useStatisticsStore()

const chartData = computed(() => statisticsStore.examPerformanceTrend)

onMounted(() => {
  statisticsStore.fetchAdminOverview()
})
</script>

<style scoped>
.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;
}

.bagde {
  padding: 16px;
  border-radius: 12px;
  background: rgba(37, 99, 235, 0.08);
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.hint {
  margin-top: 24px;
  color: #6b7280;
  font-size: 14px;
}
</style>

