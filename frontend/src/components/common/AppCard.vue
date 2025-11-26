<template>
  <section class="card">
    <header v-if="title" class="card__header">
      <h3>{{ title }}</h3>
      <slot name="actions" />
    </header>
    <div class="card__body">
      <slot />
    </div>
    <footer v-if="$slots.footer" class="card__footer">
      <slot name="footer" />
    </footer>
  </section>
</template>

<script setup lang="ts">
defineProps<{
  title?: string
}>()
</script>

<style scoped>
.card {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 12px 40px rgba(15, 23, 42, 0.08);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card__header h3 {
  margin: 0;
  font-size: 18px;
}

.card__footer {
  border-top: 1px solid #e2e8f0;
  padding-top: 16px;
}
</style>

