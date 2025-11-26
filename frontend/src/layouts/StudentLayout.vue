<template>
  <div class="layout">
    <header class="header">
      <h1>Code Examner 学生端</h1>
      <div class="actions">
        <RouterLink to="/dashboard">首页</RouterLink>
        <RouterLink to="/dashboard/exams">考试列表</RouterLink>
        <RouterLink to="/dashboard/submissions">我的提交</RouterLink>
        <RouterLink to="/dashboard/profile">个人中心</RouterLink>
      </div>
    </header>
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #ffffff;
  box-shadow: 0 6px 20px rgba(15, 23, 42, 0.08);
}

.header h1 {
  font-size: 20px;
  margin: 0;
}

.actions {
  display: flex;
  gap: 16px;
}

.actions a {
  color: var(--color-primary);
  font-weight: 500;
}

.actions a.router-link-active {
  border-bottom: 2px solid var(--color-primary);
  padding-bottom: 4px;
}

.content {
  flex: 1;
  padding: 24px;
}
</style>

