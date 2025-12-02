<template>
  <div class="not-found">
    <h2>页面走丢了</h2>
    <p>请检查链接，或返回首页继续操作。</p>
    <RouterLink to="/">返回首页</RouterLink>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.not-found {
  text-align: center;
  padding: 80px 16px;
}

a {
  display: inline-block;
  margin-top: 16px;
  color: var(--color-primary);
  font-weight: 600;
}
</style>

