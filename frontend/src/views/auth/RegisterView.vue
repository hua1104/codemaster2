<template>
  <div class="register-view-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>Code Examner 注册</h2>
          <p class="subtitle">创建您的考试系统账号</p>
        </div>
      </template>

      <el-form
        :model="registerForm"
        :rules="registerRules"
        ref="registerFormRef"
        @keyup.enter="handleRegister"
        label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="registerForm.email" 
            placeholder="请输入邮箱"
            :prefix-icon="Message"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password"
            placeholder="请输入密码（至少6位）"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password"
            placeholder="请再次输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button 
            type="success" 
            :loading="loading" 
            @click="handleRegister"
            style="width: 100%; margin-top: 10px;"
            size="large"
          >
            {{ loading ? '注册中...' : '注册并登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="footer-links">
        <el-button type="info" link @click="goToLogin">
          已有账号？返回登录
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { User, Lock, Message } from '@element-plus/icons-vue';

const router = useRouter();

// --- 状态与表单 ---
const loading = ref(false);
const registerFormRef = ref<FormInstance>();

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

// 确认密码验证器
const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致!'));
  } else {
    callback();
  }
};

const registerRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度应在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
});

// --- 注册逻辑 ---
const handleRegister = async () => {
  if (!registerFormRef.value) return;

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        // 实际项目中：
        // 1. 调用 Auth Store 的 register Action (axios.post('/api/auth/register', { username, email, password }))
        await new Promise(resolve => setTimeout(resolve, 800)); // 模拟 API 延迟

        ElMessage.success('注册成功！已自动为您登录。');
        
        // 注册成功后，自动跳转到学生仪表盘
        router.push('/dashboard'); 

      } catch (error) {
        ElMessage.error('注册失败，请稍后重试。');
      } finally {
        loading.value = false;
      }
    }
  });
};

// --- 路由跳转 ---
const goToLogin = () => {
  router.push('/auth/login');
};
</script>

<style scoped>
.register-view-container {
  /* 继承自 AuthLayout.vue 的居中样式 */
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.register-card {
  width: 100%;
  max-width: 400px; /* 限制卡片最大宽度 */
  padding: 10px 20px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
  margin-bottom: 20px;
}

.card-header h2 {
  font-size: 24px;
  color: var(--el-color-success);
  margin: 0;
}

.subtitle {
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-top: 5px;
}

.footer-links {
  text-align: center;
  margin-top: 15px;
}
</style>
