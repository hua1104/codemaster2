import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './assets/main.scss'

async function bootstrap() {
  if (import.meta.env.MODE === 'mock') {
    const { bootstrapMocks } = await import('./services/mocks/mockAdapter')
    bootstrapMocks()
  }

  const app = createApp(App)

  app.use(createPinia())
  app.use(router)

  app.mount('#app')
}

bootstrap()

