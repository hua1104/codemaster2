<template>
  <div class="admin-exam-form-view">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <h2>{{ isEditMode ? '编辑考试' : '创建新考试' }}</h2>
          <el-button @click="router.back()" link type="primary">
            <el-icon><Back /></el-icon>
            返回列表
          </el-button>
        </div>
      </template>

      <el-form
        :model="form"
        :rules="rules"
        ref="examFormRef"
        label-width="120px"
        v-loading="loading"
        class="exam-form"
      >
        <el-divider content-position="left">基础配置</el-divider>
        <el-form-item label="考试名称" prop="name">
          <el-input v-model="form.name" placeholder="例如：2025年软件工程期末考试" />
        </el-form-item>
        
        <el-form-item label="考试描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入考试的简要说明和注意事项"
          />
        </el-form-item>

        <el-divider content-position="left">时间设置</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="选择考试开始的日期和时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考试时长(分)" prop="duration">
              <el-input-number 
                v-model="form.duration" 
                :min="10" 
                :max="300" 
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">考试规则</el-divider>
        
        <el-form-item label="考试状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="DRAFT">草稿 (暂不可见)</el-radio>
            <el-radio label="SCHEDULED">已排期 (学生可见)</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="允许中途退出">
              <el-switch
                v-model="form.allowExit"
                active-text="是"
                inactive-text="否"
              />
              <el-tooltip content="设置为“否”将强制考生停留在考试页面">
                <el-icon style="margin-left: 10px;"><InfoFilled /></el-icon>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示答案">
              <el-switch
                v-model="form.showAnswerAfter"
                active-text="结束后显示"
                inactive-text="不显示"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item style="margin-top: 30px;">
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ isEditMode ? '保存修改' : '创建考试' }}
          </el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-alert 
        v-if="isEditMode" 
        title="温馨提示" 
        type="info" 
        description="编辑已排期的考试时，请注意时间变更可能会影响正在进行的考试或考生的准备。" 
        :closable="false"
        style="margin-top: 20px;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { Back, InfoFilled } from '@element-plus/icons-vue';

// 接收路由参数 (通过 props: true 配置)
const props = defineProps<{
    id?: string;
}>();

const router = useRouter();
const route = useRoute();

// --- 类型定义 ---
interface ExamForm {
  name: string;
  description: string;
  startTime: string | null;
  duration: number; // 分钟
  status: 'DRAFT' | 'SCHEDULED';
  allowExit: boolean; // 是否允许中途退出
  showAnswerAfter: boolean; // 结束后是否显示答案
}

// --- 状态管理 ---
const examFormRef = ref<FormInstance>();
const loading = ref(false); // 用于加载考试详情
const submitLoading = ref(false);
const isEditMode = ref(false);

const initialFormState: ExamForm = {
  name: '',
  description: '',
  startTime: null,
  duration: 60,
  status: 'DRAFT',
  allowExit: false,
  showAnswerAfter: false,
};

// 使用 reactive 创建响应式表单数据
const form = reactive<ExamForm>({ ...initialFormState });

// --- 表单验证规则 ---
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入考试名称', trigger: 'blur' },
    { min: 3, max: 100, message: '长度应在 3 到 100 个字符', trigger: 'blur' },
  ],
  startTime: [
    { required: true, message: '请选择考试开始时间', trigger: 'change' },
  ],
  duration: [
    { required: true, message: '请设置考试时长', trigger: 'change' },
  ],
  status: [
    { required: true, message: '请选择考试状态', trigger: 'change' },
  ],
});

// --- 数据获取（编辑模式） ---
const fetchExamDetail = async (id: string) => {
  loading.value = true;
  try {
    // 实际项目中: const { data } = await axios.get(`/api/admin/exams/${id}`);
    
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 800));
    const mockData = {
        name: `编辑中的考试: ${id}`,
        description: '这是从后端加载的考试描述，现在可以修改了。',
        startTime: '2025-12-05 15:30:00',
        duration: 90,
        status: 'SCHEDULED' as const,
        allowExit: true,
        showAnswerAfter: true,
    };
    
    // 填充表单
    Object.assign(form, mockData);

  } catch (error) {
    ElMessage.error(`加载考试详情失败：${error}`);
    router.back();
  } finally {
    loading.value = false;
  }
};

// --- 提交表单 ---
const handleSubmit = async () => {
  if (!examFormRef.value) return;

  await examFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true;
      try {
        const payload = { ...form }; // 提交的数据

        if (isEditMode.value) {
          // 编辑模式: 调用 PUT/PATCH API
          // await axios.patch(`/api/admin/exams/${props.id}`, payload);
          await new Promise(resolve => setTimeout(resolve, 1000));
          ElMessage.success(`考试 ${props.id} 保存成功！`);
        } else {
          // 创建模式: 调用 POST API
          // await axios.post('/api/admin/exams', payload);
          await new Promise(resolve => setTimeout(resolve, 1000));
          ElMessage.success('新考试创建成功！');
        }
        
        // 成功后跳转回列表页
        router.push({ name: 'AdminExamList' });

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
  if (examFormRef.value) {
    examFormRef.value.resetFields();
    // 深度重置为初始状态
    Object.assign(form, initialFormState);
  }
};

// --- 生命周期 ---
onMounted(() => {
  // 检查路由中是否有 ID
  if (props.id) {
    isEditMode.value = true;
    fetchExamDetail(props.id);
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

.exam-form {
  max-width: 900px;
  padding-right: 20px;
}

.el-divider {
    margin-top: 30px;
    margin-bottom: 30px;
}

.el-icon {
    vertical-align: middle;
}
</style>