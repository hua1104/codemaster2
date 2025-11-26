<template>
  <div class="tile">
    <div class="tile__icon" :style="{ background: accent }">
      <slot name="icon">📊</slot>
    </div>
    <div class="tile__content">
      <span class="tile__label">{{ label }}</span>
      <strong class="tile__value">{{ value }}</strong>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string
  value: string | number
  accent?: string
}>()
</script>

<style scoped>
.tile {
  background: #ffffff;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 8px 30px rgba(15, 23, 42, 0.06);
}

.tile__icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  font-size: 24px;
  background: rgba(37, 99, 235, 0.12);
}

.tile__label {
  color: #6b7280;
  font-size: 14px;
}

.tile__value {
  font-size: 24px;
  margin-top: 4px;
}
</style>

