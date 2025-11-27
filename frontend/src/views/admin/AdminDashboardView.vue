<template>
  <div class="admin-dashboard-view">
    
    <el-card shadow="hover" class="welcome-card">
      <div class="welcome-content">
        <el-icon :size="50" style="margin-right: 20px;"><Monitor /></el-icon>
        <div class="text-content">
          <h1 class="welcome-title">欢迎回来，系统管理员！</h1>
          <p class="welcome-subtitle">这是您的系统核心概览，请注意查看待处理事项。</p>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="data-row">
      <el-col :span="6">
        <StatisticCard icon-type="Files" title="总考试数量" :value="stats.totalExams" type="primary" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="CollectionTag" title="总题目数量" :value="stats.totalProblems" type="success" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="UserFilled" title="活跃用户数" :value="stats.activeUsers" type="warning" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="ChatLineRound" title="待批阅提交" :value="stats.pendingSubmissions" type="danger" />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-title">
              <el-icon><DataAnalysis /></el-icon>
              <span>考试状态分布</span>
            </div>
          </template>
          <div class="chart-placeholder">
            [占位符：这里通常嵌入一个 ECharts/G2 Pie Chart]
            <el-tag type="info" size="large" style="margin-top: 50px;">
              总数: {{ stats.totalExams }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-title">
              <el-icon><List /></el-icon>
              <span>待办事项 / 最近操作</span>
            </div>
          </template>
          
          <el-table :data="recentActivities" :show-header="true" stripe :style="{ width: '100%' }">
            <el-table-column prop="time" label="时间" width="100" />
            <el-table-column prop="user" label="用户" width="100" />
            <el-table-column prop="action" label="操作内容" show-overflow-tooltip />
          </el-table>
          
          <el-button link type="primary" style="margin-top: 15px; float: right;">查看所有日志</el-button>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineComponent } from 'vue';
import { 
  Monitor, Files, CollectionTag, UserFilled, ChatLineRound, DataAnalysis, List
} from '@element-plus/icons-vue';

// --- 辅助组件：统计卡片 (沿用 StudentDashboard 的样式) ---
const StatisticCard = defineComponent({
    props: {
        iconType: String,
        title: String,
        value: [String, Number],
        type: String, // primary, success, warning, danger
    },
    template: `
        <el-card shadow="hover" class="statistic-card" :class="'card-' + type">
            <div class="card-content">
                <el-icon :size="40">
                    <component :is="iconType" />
                </el-icon>
                <div class="text-area">
                    <p class="card-value">{{ value }}</p>
                    <p class="card-title">{{ title }}</p>
                </div>
            </div>
        </el-card>
    `,
    setup() {
        return {
             Monitor, Files, CollectionTag, UserFilled, ChatLineRound, DataAnalysis, List
        }
    }
});

// --- 模拟数据类型 ---
interface Activity {
    time: string;
    user: string;
    action: string;
}

// --- 状态管理 ---
const stats = ref({
    totalExams: 45,
    totalProblems: 288,
    activeUsers: 78,
    pendingSubmissions: 12,
});

const recentActivities = ref<Activity[]>([]);

// --- 数据获取（Mock实现） ---
const fetchDashboardData = async () => {
    // 模拟获取数据
    await new Promise(resolve => setTimeout(resolve, 300));

    // 模拟最近活动
    recentActivities.value = [
        { time: '10:45', user: 'AdminA', action: '创建了新考试：Web期末' },
        { time: '10:30', user: 'System', action: '考试 ID 102 自动开始' },
        { time: '09:15', user: 'UserB', action: '注册了新账号' },
        { time: '昨天', user: 'AdminC', action: '更新了算法题目 5001' },
    ];
};


onMounted(() => {
    fetchDashboardData();
});
</script>

<style scoped>
/* 欢迎卡片 */
.welcome-card {
  margin-bottom: 20px;
}
.welcome-content {
  display: flex;
  align-items: center;
}
.welcome-title {
  font-size: 24px;
  margin: 0;
  color: var(--el-color-primary);
}
.welcome-subtitle {
  color: var(--el-text-color-secondary);
  margin-top: 5px;
}

/* 统计卡片样式 */
.statistic-card {
  height: 100%;
}
.card-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 0;
}
.text-area {
  text-align: right;
}
.card-value {
  font-size: 26px;
  font-weight: bold;
  margin: 0;
}
.card-title {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-top: 5px;
}
.card-primary .card-value { color: var(--el-color-primary); }
.card-success .card-value { color: var(--el-color-success); }
.card-warning .card-value { color: var(--el-color-warning); }
.card-danger .card-value { color: var(--el-color-danger); }

/* 图表/列表布局 */
.data-row {
  margin-bottom: 20px;
}
.chart-row {
  margin-bottom: 20px;
}
.chart-card {
  height: 400px;
}
.card-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}
.card-title .el-icon {
  margin-right: 8px;
  color: var(--el-color-info);
}
.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
}
</style>

