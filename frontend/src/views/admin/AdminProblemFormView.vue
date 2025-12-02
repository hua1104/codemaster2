<template>
    <div class="admin-problem-form-view">
        <el-card shadow="never">
            <template #header>
                <div class="card-header">
                    <h2>{{ isEditMode ? `编辑题目 ID: ${problemId}` : '创建新题目' }}</h2>
                    <el-button @click="router.back()" link type="primary">
                        <el-icon><Back /></el-icon>
                        返回列表
                    </el-button>
                </div>
            </template>

            <el-form
                :model="form"
                :rules="rules"
                ref="problemFormRef"
                label-width="100px"
                class="problem-form"
            >
                <el-row :gutter="40">
                    
                    <el-col :span="10">
                        <el-divider content-position="left">基础信息</el-divider>

                        <el-form-item label="题目名称" prop="title">
                            <el-input v-model="form.title" placeholder="请输入题目名称" />
                        </el-form-item>
                        
                        <el-form-item label="题型" prop="problemType">
                            <el-select 
                                v-model="form.problemType" 
                                placeholder="请选择题型" 
                                style="width: 100%;"
                                :disabled="isEditMode"
                            >
                                <el-option label="单选题" value="SINGLE_CHOICE" />
                                <el-option label="填空题" value="FILL_IN_BLANK" />
                                <el-option label="编程题" value="PROGRAMMING" />
                            </el-select>
                        </el-form-item>

                        <el-form-item label="难度" prop="difficulty">
                            <el-select v-model="form.difficulty" placeholder="请选择难度" style="width: 100%;">
                                <el-option label="简单 (EASY)" value="EASY" />
                                <el-option label="中等 (MEDIUM)" value="MEDIUM" />
                                <el-option label="困难 (HARD)" value="HARD" />
                            </el-select>
                        </el-form-item>
                        
                        <el-form-item label="标签" prop="tags">
                            <el-select 
                                v-model="form.tags" 
                                multiple 
                                filterable 
                                allow-create 
                                default-first-option 
                                placeholder="添加相关标签"
                                style="width: 100%;"
                            >
                                <el-option label="算法" value="Algorithm" />
                                <el-option label="数据库" value="Database" />
                                <el-option label="前端" value="Frontend" />
                            </el-select>
                        </el-form-item>
                        
                        <el-form-item label="分数" prop="score">
                            <el-input-number 
                                v-model="form.score" 
                                :min="1" 
                                :max="100" 
                                controls-position="right"
                                style="width: 100%"
                            />
                        </el-form-item>
                        
                    </el-col>

                    <el-col :span="14">
                        <el-divider content-position="left">题目内容 (富文本编辑器)</el-divider>
                        <el-form-item prop="content" label-width="0">
                            <el-input 
                                v-model="form.content" 
                                type="textarea" 
                                :rows="8" 
                                placeholder="在这里输入题目描述、图片等内容" 
                            />
                        </el-form-item>

                        <el-divider content-position="left">{{ getProblemTypeName(form.problemType) }} 配置</el-divider>
                        
                        <div v-if="form.problemType === 'SINGLE_CHOICE'">
                            <ProblemConfigSC v-model="form.singleChoiceConfig" />
                        </div>

                        <div v-else-if="form.problemType === 'FILL_IN_BLANK'">
                            <ProblemConfigFB v-model="form.fillInBlankConfig" />
                        </div>

                        <div v-else-if="form.problemType === 'PROGRAMMING'">
                            <ProblemConfigPG v-model="form.programmingConfig" />
                        </div>
                        
                    </el-col>
                </el-row>
                
                <el-divider />
                <el-form-item class="submit-footer">
                    <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
                        {{ isEditMode ? '保存修改' : '创建题目' }}
                    </el-button>
                    <el-button @click="resetForm">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { Back } from '@element-plus/icons-vue';

// 导入外部组件 (假设这些文件存在且语法正确)
import ProblemConfigSC from '@/components/admin/ProblemConfigSC.vue';
import ProblemConfigFB from '@/components/admin/ProblemConfigFB.vue';
import ProblemConfigPG from '@/components/admin/ProblemConfigPG.vue';

const router = useRouter();

const props = defineProps<{
    id?: string;
}>();

// --- 统一的类型定义 ---
type ProblemType = 'SINGLE_CHOICE' | 'FILL_IN_BLANK' | 'PROGRAMMING';
type Difficulty = 'EASY' | 'MEDIUM' | 'HARD';

// 单选题
interface Option { key: string; content: string; }
interface SingleChoiceConfig { options: Option[]; correctOptionKey: string; }

// 编程题
interface TestCase { input: string; expectedOutput: string; isExample: boolean; }
interface ProgrammingConfig { testCases: TestCase[]; }

// 填空题 (已修复)
interface Blank {
    id: string;
    content: string;
    answers: string[]; 
}
interface FillInBlankConfig { 
    blanks: Blank[]; 
}

interface ProblemForm {
    title: string;
    problemType: ProblemType;
    difficulty: Difficulty;
    content: string;    
    tags: string[];
    score: number;
    
    singleChoiceConfig: SingleChoiceConfig;
    fillInBlankConfig: FillInBlankConfig;
    programmingConfig: ProgrammingConfig;
}

// --- 状态管理 ---
const problemFormRef = ref<FormInstance>();
// loading 初始值改为 false，避免遮挡
const loading = ref(false);     
const submitLoading = ref(false);

const problemId = ref(props.id);
const isEditMode = ref(!!props.id);

// 确保初始状态结构正确且符合类型
const initialFormState: ProblemForm = {
    title: '',
    problemType: 'SINGLE_CHOICE' as ProblemType,    
    difficulty: 'MEDIUM' as Difficulty,
    content: '',
    tags: [],
    score: 10,
    
    singleChoiceConfig: { 
        options: [{ key: 'A', content: '选项 A' }, { key: 'B', content: '选项 B' }],
        correctOptionKey: '',
    },
    fillInBlankConfig: {
        blanks: [
            { 
                id: 'init-1', 
                content: '', 
                answers: [''], 
            }
        ],
    },
    programmingConfig: {
        testCases: [],
    },
};

const form = reactive<ProblemForm>({ ...initialFormState });

// --- 辅助函数 ---
const getProblemTypeName = (type: ProblemType) => {
    const map = {
        'SINGLE_CHOICE': '单选题',
        'FILL_IN_BLANK': '填空题',
        'PROGRAMMING': '编程题',
    };
    return map[type] || '题目类型';
};

// --- 表单验证规则 ---
const rules = reactive<FormRules>({
    title: [ { required: true, message: '请输入题目名称', trigger: 'blur' } ],
    problemType: [ { required: true, message: '请选择题型', trigger: 'change' } ],
    difficulty: [ { required: true, message: '请选择难度', trigger: 'change' } ],
    score: [ { required: true, message: '请设置分数', trigger: 'change' } ],
    content: [ { required: true, message: '请输入题目内容', trigger: 'blur' } ],
});


// --- 数据获取（编辑模式） ---
const fetchProblemDetail = async (id: string) => {
    loading.value = true;
    try {
        await new Promise(resolve => setTimeout(resolve, 800));
        
        // 模拟 API 返回的扁平数据。
        const mockApiData = {
            title: `编辑中的编程题 ID: ${id}`,
            problemType: 'PROGRAMMING' as ProblemType, 
            difficulty: 'HARD' as Difficulty,      
            content: '请实现一个高效的排序算法。',
            tags: ['算法', '编程'],
            score: 25,
            
            testCases: [ 
                { input: '1 3 2', expectedOutput: '1 2 3', isExample: true },
                { input: '9 5 8 1 3', expectedOutput: '1 3 5 8 9', isExample: false },
            ],
            // 如果需要加载填空题数据，结构应为：
            // fillInBlankData: [{ id: 'mock-1', content: '第一个空：', answers: ['答案1'] }],
        };
        
        const loadedForm: ProblemForm = {
            ...initialFormState, 
            title: mockApiData.title,
            problemType: mockApiData.problemType,
            difficulty: mockApiData.difficulty,
            content: mockApiData.content,
            tags: mockApiData.tags,
            score: mockApiData.score,
            
            singleChoiceConfig: initialFormState.singleChoiceConfig,
            fillInBlankConfig: initialFormState.fillInBlankConfig,
            
            programmingConfig: { 
                testCases: mockApiData.testCases 
            },
        };
        
        Object.assign(form, loadedForm);

    } catch (error) {
        // ElMessage.error(`加载题目详情失败：${error}`);
        // router.back();
        // 仅用于调试，避免页面跳转
        console.error('加载题目详情失败：', error);
    } finally {
        loading.value = false;
    }
}; 

// --- 提交表单 ---
const handleSubmit = async () => {
    if (!problemFormRef.value) return;

    await problemFormRef.value.validate(async (valid) => {
        if (valid) {
            submitLoading.value = true;
            try {
                // ... (提交逻辑保持不变)
                const { singleChoiceConfig, fillInBlankConfig, programmingConfig, ...baseForm } = form;
                
                let finalConfigData: any = {};
                if (form.problemType === 'SINGLE_CHOICE') {
                    finalConfigData = singleChoiceConfig;
                } else if (form.problemType === 'FILL_IN_BLANK') {
                    finalConfigData = fillInBlankConfig;
                } else if (form.problemType === 'PROGRAMMING') {
                    finalConfigData = programmingConfig;
                }

                const payload = {
                    ...baseForm,
                    ...finalConfigData, 
                };
                
                console.log('Final Payload:', payload);

                await new Promise(resolve => setTimeout(resolve, 1000));
                
                ElMessage.success(`${isEditMode.value ? '保存' : '创建'}成功！`);
                // router.push({ name: 'AdminProblemList' });

            } catch (error) {
                ElMessage.error(`${isEditMode.value ? '保存' : '创建'}失败，请重试。`);
            } finally {
                submitLoading.value = false;
            }
        } else {
            ElMessage.warning('表单验证失败，请检查输入项！');
        }
    });
};

// --- 其他操作 ---
const resetForm = () => {
    if (problemFormRef.value) {
        problemFormRef.value.resetFields();
        Object.assign(form, initialFormState);
    }
};

// --- 切换题型时，重置动态配置项 ---
watch(() => form.problemType, (newType) => {
    if (newType === 'SINGLE_CHOICE') {
        Object.assign(form.singleChoiceConfig, initialFormState.singleChoiceConfig);
    } else if (newType === 'FILL_IN_BLANK') {
        Object.assign(form.fillInBlankConfig, initialFormState.fillInBlankConfig);
    } else if (newType === 'PROGRAMMING') {
        Object.assign(form.programmingConfig, initialFormState.programmingConfig);
    }
});


onMounted(() => {
    if (props.id) {
        isEditMode.value = true;
        fetchProblemDetail(props.id);
    } else {
        isEditMode.value = false;
    }
});
</script>

<style scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.card-header h2 {
    font-size: 20px;
    margin: 0;
    color: var(--el-text-color-primary);
}

.problem-form {
    padding-right: 20px;
}

.el-divider {
    margin-top: 25px;
    margin-bottom: 25px;
}
</style>