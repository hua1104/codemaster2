<template>
  <div class="auth-layout">
    <section class="panel">
      <header class="header">
        <h1>Code Examner</h1>
        <p>在线编程考试系统</p>
      </header>
      <main class="content">
        <RouterView />
      </main>
    </section>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.auth-layout {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.15), rgba(16, 185, 129, 0.15));
}

.panel {
  width: min(420px, 94%);
  background: #ffffff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.header {
  text-align: center;
  margin-bottom: 24px;
}

.header h1 {
  margin: 0;
  font-size: 28px;
  color: var(--color-primary);
}

.header p {
  margin: 8px 0 0;
  color: #4b5563;
}
</style>

