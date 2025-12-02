<template>
  <div class="sc-config">
    <el-alert
      title="选项配置"
      type="info"
      description="请至少提供两个选项，并在下方设置唯一正确答案。"
      :closable="false"
      class="mb-4"
    />

    <div v-for="(option, index) in currentOptions" :key="option.key" class="option-row">
      <el-form-item :label="`选项 ${option.key}`"> 
        <div class="input-group">
          <el-input v-model="option.content" placeholder="请输入选项内容" style="flex-grow: 1;" />
          <el-button 
            type="danger" 
            :icon="Delete" 
            circle 
            :disabled="currentOptions.length <= 2"
            @click="handleRemoveOption(option.key)" 
            class="ml-2"
          />
        </div>
      </el-form-item>
    </div>

    <el-button type="primary" plain @click="handleAddOption" :icon="Plus" class="mb-4">
      添加选项
    </el-button>

    <el-divider content-position="left">正确答案设置</el-divider>
    
    <el-form-item label="正确选项"> 
      <el-select v-model="currentCorrectKey" placeholder="请选择正确答案" style="width: 100%;">
        <el-option 
          v-for="option in currentOptions" 
          :key="option.key" 
          :label="`选项 ${option.key}`" 
          :value="option.key"
        />
      </el-select>
    </el-form-item>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { Plus, Delete } from '@element-plus/icons-vue';
// Element Plus 的类型导入 (如果需要)
// import type { FormRules } from 'element-plus'; 

// --- 类型定义 (必须与父组件 AdminProblemFormView.vue 保持一致) ---
interface Option { key: string; content: string; }
interface SingleChoiceConfig { 
  options: Option[]; 
  correctOptionKey: string; 
}

// --- Props & Emits ---
const props = defineProps<{
  modelValue: SingleChoiceConfig; // 接收整个聚合对象
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: SingleChoiceConfig): void;
}>();


// --- Computed 双向绑定 (将聚合对象解构给模板使用) ---

// 1. 处理 options 数组的双向绑定
const currentOptions = computed({
  get: () => props.modelValue.options,
  set: (newOptions) => {
    // 发射 update:modelValue 事件，将整个对象返回给父组件
    emit('update:modelValue', {
      ...props.modelValue,
      options: newOptions,
    });
  },
});

// 2. 处理 correctOptionKey 的双向绑定
const currentCorrectKey = computed({
  get: () => props.modelValue.correctOptionKey,
  set: (newKey) => {
    // 发射 update:modelValue 事件，将整个对象返回给父组件
    emit('update:modelValue', {
      ...props.modelValue,
      correctOptionKey: newKey,
    });
  },
});

// --- 操作逻辑 ---

// 生成下一个选项的 Key (A, B, C, ...)
// ProblemConfigSC.vue

// 生成下一个选项的 Key (A, B, C, ...)
const generateNextKey = (options: Option[]): string => {
    // 1. 优先检查空数组情况
    if (options.length === 0) return 'A';

    // 2. 安全获取最后一个选项
    const lastOption = options[options.length - 1];

    // **关键修复：检查 lastOption 是否为 undefined (尽管根据上面的 check 不应该，但为了 TS 健壮性)**
    if (!lastOption) return 'A'; 
    
    const lastKey = lastOption.key;

    // 3. 确保最后一个 key 是大写字母
    if (lastKey.length !== 1 || lastKey < 'A' || lastKey > 'Z') return 'A'; 
    
    // 4. 返回下一个字母
    return String.fromCharCode(lastKey.charCodeAt(0) + 1);
};
const handleAddOption = () => {
    const nextKey = generateNextKey(currentOptions.value);
    const newOptions = [...currentOptions.value, { key: nextKey, content: `选项 ${nextKey}` }];
    currentOptions.value = newOptions; // 触发 computed.set
};

const handleRemoveOption = (keyToRemove: string) => {
    const newOptions = currentOptions.value.filter(opt => opt.key !== keyToRemove);
    currentOptions.value = newOptions; // 触发 computed.set
    
    // 如果移除的是正确答案，则重置正确答案
    if (currentCorrectKey.value === keyToRemove) {
        currentCorrectKey.value = '';
    }
};

// 验证规则（如果需要，可以在这里定义，但通常简单题目的必填验证在父组件处理）
/*
const optionContentRules = reactive<FormRules>({
  // ...
});
*/
</script>

<style scoped>
.option-row {
  /* 确保选项之间有间距 */
  margin-bottom: 20px;
}
.input-group {
    display: flex;
    align-items: center;
}
</style>