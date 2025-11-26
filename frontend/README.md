## Code Examner 前端设计（Vue 3 + Vite）

### 项目简介

该目录提供了一个与现有 Spring Boot 后端配套的 Vue 3 前端脚手架，采用 Vite 构建工具与 TypeScript，围绕考试、题库、提交、统计等业务域进行分类组织，便于后续团队扩展与本地联调。

### 目录结构

```
frontend/
├── public/               # 静态资源
├── src/
│   ├── assets/           # 全局样式与静态资源
│   ├── components/       # 通用组件（common/forms/widgets）
│   ├── layouts/          # 管理端、学生端、认证页布局
│   ├── router/           # 按角色拆分的路由模块
│   ├── services/         # axios 客户端、接口定义、mock
│   ├── store/            # Pinia 状态管理（auth/exam/problem 等）
│   ├── types/            # TypeScript 类型声明
│   └── views/            # 业务页面（auth/admin/student/...）
└── vite.config.ts        # Vite 配置，含后端代理
```

### 本地运行

1. 安装依赖

   ```bash
   cd frontend
   npm install
   ```

2. 启动开发服务器（联调后端）

   ```bash
   npm run dev
   ```

   默认会将 `/api` 代理到 `http://localhost:8080`，无需修改后端。

3. 启动 mock 模式（后端未启动时）

   ```bash
   npm run dev -- --mode mock
   ```

   mock adapter 将返回示例数据，保障页面可演示和组件调试。

### 与后端接口的对应关系

- `auth`：对接 `/auth/login`、`/auth/register` 与 `/users/me`，覆盖 Jwt 登录注册与个人信息。
- `exam`：对接 `/exams` & `/exams/{id}`，学生端用于获取考试列表与详情；管理端扩展 `/admin/exams`。
- `problem`：对接 `/admin/problems`，用于题库管理功能。
- `submission`：对接 `/submissions` 与 `/submissions/{id}`，展示评测结果与测试用例明细。
- `statistics`：对接 `/admin/statistics` 与 `/statistics/student`，分别提供后台与学生数据概览。

开发时可在 `src/services/endpoints.ts` 维护统一的接口路径，避免硬编码。

### 后续扩展建议

- 将表格组件替换为 Ant Design Vue、Naive UI 等成熟组件库，增强交互。
- 在 `services/mocks` 中补充更多领域的示例数据，完善离线调试体验。
- 结合 `vue-request` 或 SWR 思路封装请求缓存、错误提示与重试策略。
- 为关键页面编写 E2E 测试（Playwright）与单元测试（Vitest + vue-test-utils）。

> 注意：本前端设计未修改任何后端文件，可直接放置在仓库根目录，与现有 Spring Boot 项目解耦。

