<template>
  <label class="field">
    <span class="field__label">{{ label }}</span>
    <input
      v-bind="$attrs"
      :type="type"
      :placeholder="placeholder"
      :value="modelValue"
      @input="onInput"
    />
    <small v-if="hint" class="field__hint">{{ hint }}</small>
  </label>
</template>

<script setup lang="ts">
const props = defineProps<{
  label: string
  hint?: string
  placeholder?: string
  type?: string
  modelValue?: string | number
}>()

const emit = defineEmits<{
  'update:modelValue': [string]
}>()

function onInput(event: Event) {
  const target = event.target as HTMLInputElement
  emit('update:modelValue', target.value)
}
</script>

<style scoped>
.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
}

.field__label {
  font-weight: 600;
  color: #1f2937;
}

input {
  padding: 12px;
  border-radius: 10px;
  border: 1px solid #d1d5db;
  font-size: 14px;
}

.field__hint {
  color: #6b7280;
}
</style>

