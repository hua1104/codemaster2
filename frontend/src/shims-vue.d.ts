// frontend/src/shims-vue.d.ts

declare module '*.vue' {
  import type { DefineComponent } from 'vue';
  // VUE_COMP 定义用于告诉 TypeScript 导入的是一个 Vue 组件类型
  const component: DefineComponent<{}, {}, any>;
  export default component;
}