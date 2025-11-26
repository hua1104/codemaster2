<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">管理后台</div>
      <nav>
        <RouterLink to="/admin/dashboard">总览</RouterLink>
        <RouterLink to="/admin/problems">题库管理</RouterLink>
        <RouterLink to="/admin/exams">考试管理</RouterLink>
        <RouterLink to="/admin/statistics">统计分析</RouterLink>
      </nav>
    </aside>
    <div class="main">
      <header class="topbar">
        <slot name="header">
          <h2>后台控制台</h2>
        </slot>
      </header>
      <main>
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.layout {
  display: grid;
  grid-template-columns: 240px 1fr;
  min-height: 100vh;
}

.sidebar {
  background: #0f172a;
  color: #e2e8f0;
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.brand {
  font-size: 20px;
  font-weight: 600;
}

nav {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

nav a {
  color: inherit;
  padding: 8px 12px;
  border-radius: 8px;
}

nav a.router-link-active {
  background: rgba(37, 99, 235, 0.2);
}

.main {
  background: #f1f5f9;
  display: flex;
  flex-direction: column;
}

.topbar {
  background: #ffffff;
  padding: 16px 24px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
}

main {
  padding: 24px;
  flex: 1;
}
</style>

