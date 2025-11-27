<template>
  <div class="user-profile-view">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>个人资料与设置</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="profile-tabs">
        
        <el-tab-pane label="基本信息" name="info">
          <div class="info-content">
            <el-form :model="userInfo" label-width="100px" class="user-info-form">
              <el-form-item label="头像">
                <el-upload
                  class="avatar-uploader"
                  action="#"
                  :show-file-list="false"
                  :auto-upload="false"
                  :on-change="handleAvatarChange"
                >
                  <el-avatar :size="80" :src="userInfo.avatarUrl || ''" :icon="UserFilled" class="avatar-display" />
                  <div class="upload-mask">修改头像</div>
                </el-upload>
              </el-form-item>

              <el-form-item label="用户名">
                <el-input v-model="userInfo.username" disabled />
                <span class="info-tip">用户名不可修改</span>
              </el-form-item>
              
              <el-form-item label="邮箱">
                <el-input v-model="userInfo.email" disabled />
                <span class="info-tip">邮箱用于找回密码，不可修改</span>
              </el-form-item>
              
              <el-form-item label="注册时间">
                <el-input v-model="userInfo.registerDate" disabled />
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="修改密码" name="password">
          <div class="password-content">
            <el-form
              :model="passwordForm"
              :rules="passwordRules"
              ref="passwordFormRef"
              label-width="120px"
              class="password-form"
            >
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password />
              </el-form-item>
              
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password />
              </el-form-item>
              
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
              </el-form-item>

              <el-form-item>
                <el-button 
                  type="danger" 
                  :loading="passwordLoading" 
                  @click="handleChangePassword"
                >
                  确认修改
                </el-button>
                <el-button @click="resetPasswordForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import type { FormInstance, FormRules, UploadProps } from 'element-plus';
import { ElMessage } from 'element-plus';
import { UserFilled } from '@element-plus/icons-vue';

// --- 状态与数据 ---
const activeTab = ref('info');
const passwordLoading = ref(false);

const userInfo = reactive({
  username: 'StudentUser123',
  email: 'student@example.com',
  registerDate: '2024-01-01',
  avatarUrl: 'https://cube.elemecdn.com/0/88/03b0d4073289053916962f9fdcf70png.png', // 默认头像
});

// 密码表单
const passwordFormRef = ref<FormInstance>();
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// --- 表单验证规则 ---

// 确认新密码验证器
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'));
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的新密码不一致!'));
  } else {
    callback();
  }
};

const passwordRules = reactive<FormRules>({
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '新密码长度至少为 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
});

// --- 操作方法 ---

// 模拟获取用户信息 (实际应调用 API)
const fetchUserInfo = async () => {
    // 假设通过 API 获取用户信息
    await new Promise(resolve => setTimeout(resolve, 300));
    // userInfo.username = 'FetchedUser';
    // userInfo.email = 'fetched@email.com';
};

// 处理头像上传
const handleAvatarChange: UploadProps['onChange'] = (uploadFile) => {
    if (uploadFile.raw) {
        // 实际：调用 API 上传文件，获取新的 URL
        const newUrl = URL.createObjectURL(uploadFile.raw);
        userInfo.avatarUrl = newUrl;
        ElMessage.success('头像上传成功 (仅前端模拟)');
    }
};

// 修改密码逻辑
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return;

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true;
      try {
        // 实际项目中：调用 API 提交密码修改表单
        // const response = await axios.post('/api/user/changePassword', passwordForm);
        await new Promise(resolve => setTimeout(resolve, 1000)); // 模拟 API 延迟

        ElMessage.success('密码修改成功，请重新登录！');
        
        // 成功后清空表单
        resetPasswordForm(); 
        
        // 实际应用中：应引导用户登出并跳转到登录页
        // router.push('/auth/login'); 

      } catch (error) {
        // 假设 API 返回错误信息
        ElMessage.error('密码修改失败：旧密码错误或服务器错误。');
      } finally {
        passwordLoading.value = false;
      }
    }
  });
};

// 重置密码表单
const resetPasswordForm = () => {
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields();
  }
};

onMounted(() => {
    fetchUserInfo();
});
</script>

<style scoped>
.profile-tabs {
  margin-top: 10px;
}

.user-info-form {
  max-width: 500px;
  padding: 10px 0;
}

.password-form {
  max-width: 450px;
  padding: 10px 0;
}

.info-tip {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-left: 10px;
}

/* 头像上传样式 */
.avatar-uploader {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  cursor: pointer;
}
.avatar-display {
  display: block;
}
.upload-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s;
}
.avatar-uploader:hover .upload-mask {
  opacity: 1;
}
</style>