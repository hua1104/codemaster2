<template>
  <el-container class="student-layout-container">
    <el-header class="student-layout-header">
      <div class="logo-text">Code Examner</div>
      
      <div class="header-nav">
        <el-menu mode="horizontal" :default-active="route.path" router class="main-menu">
          <el-menu-item index="/dashboard">首页</el-menu-item>
          <el-menu-item index="/dashboard/exams">我的考试</el-menu-item>
          <el-menu-item index="/dashboard/submissions">我的提交</el-menu-item>
        </el-menu>
        
        <el-button link type="danger" style="margin-left: 20px;" @click="handleLogout">退出</el-button>
      </div>
    </el-header>

    <el-main class="student-layout-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();

const handleLogout = () => {
    ElMessage.success('退出成功！');
    router.push('/auth/login');
};
</script>

<style scoped>
.student-layout-container {
  height: 100vh;
}

.student-layout-header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--el-border-color-light);
  height: 60px; /* 默认高度 */
}

.logo-text {
  font-size: 1.5em;
  font-weight: bold;
  color: var(--el-color-primary);
}

.header-nav {
  display: flex;
  align-items: center;
}

.main-menu {
  border-bottom: none !important;
  height: 60px;
}

.student-layout-main {
  padding: 0; /* 移除默认 padding，让子视图自己控制布局 */
  height: calc(100vh - 60px); 
  overflow: hidden; 
}
</style>

