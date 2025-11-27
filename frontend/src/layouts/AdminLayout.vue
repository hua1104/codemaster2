<template>
  <el-container class="admin-layout-container">
    <el-aside width="220px" class="layout-aside">
      <div class="logo-container">
        <el-icon :size="28" style="margin-right: 8px;"><Tickets /></el-icon>
        <span class="system-title">Code Examner Admin</span>
      </div>
      
      <el-menu
        :default-active="route.path"
        class="layout-menu"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        router 
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><Monitor /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        
        <el-sub-menu index="exams">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>考试管理</span>
          </template>
          <el-menu-item index="/admin/exams">考试列表</el-menu-item>
          <el-menu-item index="/admin/exam/create">新建考试</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="problems">
          <template #title>
            <el-icon><MessageBox /></el-icon>
            <span>题库管理</span>
          </template>
          <el-menu-item index="/admin/problems">题目列表</el-menu-item>
          <el-menu-item index="/admin/problem/create">新增题目</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/admin/users">
          <el-icon><Setting /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-breadcrumb">
          <span>{{ currentRouteName }}</span>
        </div>
        
        <div class="header-right">
          <el-tag type="success" size="large">管理员: {{ userName }}</el-tag>
          <el-button 
            type="danger" 
            :icon="SwitchButton" 
            link 
            style="margin-left: 20px;"
            @click="handleLogout"
          >
            退出登录
          </el-button>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { 
  Tickets, Monitor, Document, MessageBox, Setting, SwitchButton 
} from '@element-plus/icons-vue'; 
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();

const userName = computed(() => 'Admin User'); 

const currentRouteName = computed(() => {
  return route.meta.title || route.path.split('/').pop() || 'Dashboard';
});

const handleLogout = () => {
  // 实际项目中：调用 Pinia/Auth store 的 logout action
  ElMessage.success('退出成功！');
  router.push('/auth/login');
};
</script>

<style scoped>
.admin-layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #545c64;
  height: 100%;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #434a50;
}

.layout-menu {
  height: calc(100vh - 60px); 
  border-right: none;
}

.layout-header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  border-bottom: 1px solid var(--el-border-color-light);
  padding: 0 20px;
}

.header-breadcrumb {
  font-size: 16px;
  color: #606266;
}

.header-right {
  display: flex;
  align-items: center;
}

.layout-main {
  background-color: var(--el-bg-color-page);
  padding: 20px;
  overflow-y: auto; 
}
</style>