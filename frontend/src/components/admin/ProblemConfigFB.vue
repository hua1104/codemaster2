<template>
  <div class="fb-config">
    <el-alert
      title="填空题配置说明"
      type="info"
      description="请配置题目中所有需要填空的区域及其对应的所有正确答案。"
      :closable="false"
      class="mb-4"
    />

    <el-form-item
      v-for="(blank, index) in currentBlanks"
      :key="blank.id"
      :label="`填空 ${index + 1}`"
      class="blank-item"
    >
      <el-card shadow="never" class="w-full">
        <el-input 
          v-model="blank.content" 
          :rows="2"
          type="textarea"
          placeholder="请输入该填空区域的提示或上下文 (可选)" 
          class="mb-3"
        />

        <el-divider content-position="left">正确答案列表</el-divider>

        <div v-for="(answer, ansIndex) in blank.answers" :key="ansIndex" class="answer-row">
          <el-input 
            v-model="blank.answers[ansIndex]" 
            placeholder="请输入一个正确答案"
            class="answer-input"
          >
            <template #prepend>{{ ansIndex + 1 }}.</template>
            <template #append>
                <el-button 
                    :icon="Remove" 
                    @click="handleRemoveAnswer(blank.id, ansIndex)"
                    :disabled="blank.answers.length === 1"
                />
            </template>
          </el-input>
        </div>

        <el-button 
            type="success" 
            plain 
            :icon="Plus" 
            @click="handleAddAnswer(blank.id)" 
            class="mt-3"
        >
            添加备选答案
        </el-button>

        <el-divider />
        <el-button 
          type="danger" 
          :icon="Delete" 
          @click="handleRemoveBlank(blank.id)"
          :disabled="currentBlanks.length === 1"
        >
          删除此填空项
        </el-button>
      </el-card>
    </el-form-item>

    <el-button type="primary" :icon="Plus" @click="handleAddBlank" class="mt-4">
      添加新的填空项
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { Plus, Delete, Remove } from '@element-plus/icons-vue';

// --- 类型定义 ---
// Blank 配置结构
interface Blank {
  id: string;
  content: string; // 填空前的提示内容
  answers: string[]; // 所有正确答案
}

// 填空题完整的配置对象
interface FillBlankConfig {
  blanks: Blank[];
}

// 用于生成唯一的 Blank ID
const blankIdCounter = ref(1);

// --- Props & Emits ---
const props = defineProps<{
  modelValue: FillBlankConfig; // 接收整个聚合对象
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: FillBlankConfig): void;
}>();

// --- Computed 双向绑定 (核心逻辑) ---

// currentBlanks 负责将 modelValue.blanks 数组与模板进行双向同步
const currentBlanks = computed({
  get: () => props.modelValue.blanks,
  set: (newBlanks) => {
    // 发射 update:modelValue 事件，将整个 FillBlankConfig 对象返回给父组件
    emit('update:modelValue', {
      ...props.modelValue,
      blanks: newBlanks,
    });
  },
});

// --- 操作逻辑 ---

// 辅助函数：根据 ID 查找 Blank
const findBlankById = (id: string) => {
    return currentBlanks.value.find(b => b.id === id);
}

// 1. 添加新的填空项
const handleAddBlank = () => {
  const newId = `blank-${blankIdCounter.value++}`;
  const newBlank: Blank = {
    id: newId,
    content: '',
    answers: [''], // 默认有一个空答案
  };
  currentBlanks.value = [...currentBlanks.value, newBlank];
};

// 2. 删除指定的填空项
const handleRemoveBlank = (idToRemove: string) => {
  // 确保至少保留一项
  if (currentBlanks.value.length === 1) return;
  
  const newBlanks = currentBlanks.value.filter(b => b.id !== idToRemove);
  currentBlanks.value = newBlanks;
};

// 3. 给指定填空项添加答案
const handleAddAnswer = (blankId: string) => {
    const blank = findBlankById(blankId);
    if (blank) {
        // 使用 map 创建新的 blanks 数组，以保持响应性
        currentBlanks.value = currentBlanks.value.map(b => 
            b.id === blankId ? { ...b, answers: [...b.answers, ''] } : b
        );
    }
};

// 4. 删除指定填空项的某个答案
const handleRemoveAnswer = (blankId: string, ansIndexToRemove: number) => {
    const blank = findBlankById(blankId);
    if (blank && blank.answers.length > 1) {
        // 使用 splice 的副作用，但最好保持不可变性
        const newAnswers = blank.answers.filter((_, index) => index !== ansIndexToRemove);
        
        // 使用 map 创建新的 blanks 数组
        currentBlanks.value = currentBlanks.value.map(b => 
            b.id === blankId ? { ...b, answers: newAnswers } : b
        );
    }
};

// 确保初始状态至少有一个填空项
if (currentBlanks.value.length === 0) {
    handleAddBlank();
}

</script>

<style scoped>
.blank-item {
  margin-bottom: 20px;
}
.answer-row {
    margin-bottom: 10px;
}
.answer-input {
    width: 100%;
}
</style>