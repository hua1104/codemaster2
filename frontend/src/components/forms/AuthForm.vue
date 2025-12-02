<template>
  <form class="auth-form" @submit.prevent="handleSubmit">
    <slot />
    <button class="submit" type="submit">
      {{ submitLabel }}
    </button>
  </form>
</template>

<script setup lang="ts">
const props = defineProps<{
  submitLabel: string
}>()

const emit = defineEmits<{
  submit: [void]
}>()

function handleSubmit() {
  emit('submit')
}
</script>

<style scoped>
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.submit {
  padding: 12px;
  border-radius: 12px;
  border: none;
  background: var(--color-primary);
  color: #ffffff;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
}

.submit:hover {
  background: var(--color-primary-dark);
}
</style>

