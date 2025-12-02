// frontend/src/main.ts

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'

import './assets/element-variables.scss' 

import App from './App.vue' 
import router from './router' 

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus) // 使用 Element Plus 插件
app.mount('#app')
