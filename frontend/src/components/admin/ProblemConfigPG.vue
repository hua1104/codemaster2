<template>
    <div class="problem-config-pg">
        <div v-for="(testCase, index) in localTestCases" :key="index" class="test-case-card">
            <el-card shadow="hover">
                <template #header>
                    <div class="card-header">
                        <span>测试用例 #{{ index + 1 }}</span>
                        <div class="actions">
                            <el-switch
                                v-model="testCase.isExample"
                                active-text="示例"
                                inactive-text="隐藏"
                                size="small"
                                style="margin-right: 15px;"
                            />
                            <el-button 
                                type="danger" 
                                :icon="Delete" 
                                link 
                                @click="deleteTestCase(index)"
                            >
                                删除
                            </el-button>
                        </div>
                    </div>
                </template>

                <el-form-item label="输入 (Input)">
                    <el-input
                        v-model="testCase.input"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入测试输入数据（如命令行输入）"
                    />
                </el-form-item>

                <el-form-item label="期望输出 (Output)">
                    <el-input
                        v-model="testCase.expectedOutput"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入程序运行的期望输出结果"
                    />
                </el-form-item>
            </el-card>
        </div>

        <el-button 
            type="primary" 
            plain 
            :icon="Plus" 
            @click="addTestCase" 
            style="width: 100%; margin-top: 20px;"
        >
            添加测试用例
        </el-button>
        
        <el-alert 
            title="提示：示例用例 (isExample: true) 会向用户展示。" 
            type="info" 
            show-icon 
            :closable="false" 
            style="margin-top: 20px;"
        />
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { Plus, Delete } from '@element-plus/icons-vue';

// --- 类型定义 ---
interface TestCase {
    input: string;
    expectedOutput: string;
    isExample: boolean;
}

interface ProgrammingConfig {
    testCases: TestCase[];
}

// --- Props 和 Emits ---
const props = defineProps<{
    modelValue: ProgrammingConfig;
}>();

const emit = defineEmits<{
    (e: 'update:modelValue', value: ProgrammingConfig): void;
}>();


// --- 核心逻辑：数据同步 ---

// 用于创建一个新的空测试用例
const createNewTestCase = (): TestCase => ({
    input: '',
    expectedOutput: '',
    isExample: false,
});

// 使用 computed 属性实现 v-model 的双向绑定
// 这样在组件内部修改 localTestCases 数组时，会通过 setter 自动更新父组件的 modelValue
const localTestCases = computed({
    get: () => props.modelValue.testCases,
    set: (newTestCases) => {
        emit('update:modelValue', { 
            ...props.modelValue, 
            testCases: newTestCases 
        });
    }
});


// --- 操作函数 ---

/**
 * 添加一个新的空测试用例
 */
const addTestCase = () => {
    // 通过复制当前数组并添加新项来实现响应式更新
    localTestCases.value = [...localTestCases.value, createNewTestCase()];
};

/**
 * 删除指定索引的测试用例
 * @param index 要删除用例的索引
 */
const deleteTestCase = (index: number) => {
    if (localTestCases.value.length === 0) return;
    
    const newTestCases = [...localTestCases.value];
    newTestCases.splice(index, 1);
    localTestCases.value = newTestCases;
};

</script>

<style scoped>
.problem-config-pg {
    /* 保持与父组件的配置区域一致性 */
}

.test-case-card {
    margin-bottom: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    color: var(--el-color-primary);
}
</style>