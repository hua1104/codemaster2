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
            <div ref="examStatusChartRef" class="exam-status-chart"></div>
            <el-tag type="info" size="large" style="margin-top: 16px;">
              总考试数: {{ stats.totalExams }}
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
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { 
  Monitor, Files, CollectionTag, UserFilled, ChatLineRound, DataAnalysis, List
} from '@element-plus/icons-vue';
import * as echarts from 'echarts';
import StatisticCard from '@/components/common/StatisticCard.vue';
import apiClient from '@/services/apiClient';
import { endpoints } from '@/services/endpoints';

// --- 最近活动类型 ---
interface Activity {
    time: string;
    user: string;
    action: string;
}

// --- 状态管理：仪表盘统计 ---
const stats = ref({
    totalExams: 0,
    totalProblems: 0,
    activeUsers: 0,
    pendingSubmissions: 0,
    scheduledExams: 0,
    ongoingExams: 0,
    finishedExams: 0,
    cancelledExams: 0
});

// 图表实例引用
const examStatusChartRef = ref<HTMLDivElement | null>(null);
let examStatusChart: echarts.ECharts | null = null;
const recentActivities = ref<Activity[]>([]);

// --- 初始化 / 更新考试状态分布饼图 ---
const renderExamStatusChart = () => {
    if (!examStatusChartRef.value) return;

    const data = [
        { name: '未开始', value: stats.value.scheduledExams },
        { name: '进行中', value: stats.value.ongoingExams },
        { name: '已结束', value: stats.value.finishedExams },
        { name: '草稿', value: stats.value.draftExams ?? 0 },
        { name: '已取消', value: stats.value.cancelledExams }
    ].filter(item => item.value && item.value > 0);

    if (!examStatusChart) {
        examStatusChart = echarts.init(examStatusChartRef.value);
    }

    examStatusChart.setOption({
        tooltip: {
            trigger: 'item'
        },
        legend: {
            bottom: 0
        },
        series: [
            {
                name: '考试状态',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 8,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: true,
                    formatter: '{b}: {c} ({d}%)'
                },
                data
            }
        ]
    });
};

// --- 数据获取：从后端统计接口加载数据 ---
const fetchDashboardData = async () => {
    // 1) 获取系统统计数据
    const { data } = await apiClient.get(endpoints.admin.statistics);

    stats.value.totalExams = data.totalExams ?? 0;
    stats.value.totalProblems = data.totalProblems ?? 0;
    stats.value.activeUsers = data.totalUsers ?? 0;
    stats.value.pendingSubmissions = (data.totalSubmissions ?? 0) - (data.acceptedSubmissions ?? 0);

    stats.value.scheduledExams = data.scheduledExams ?? 0;
    stats.value.ongoingExams = data.ongoingExams ?? 0;
    stats.value.finishedExams = data.finishedExams ?? 0;
    stats.value.cancelledExams = data.cancelledExams ?? 0;

    // 2) 渲染考试状态分布图
    renderExamStatusChart();

    // 3) 最近活动（暂时仍为本地模拟）
    recentActivities.value = [
        { time: '10:45', user: 'AdminA', action: '创建了新考试：Web期末' },
        { time: '10:30', user: 'System', action: '考试 ID 102 自动开始' },
        { time: '09:15', user: 'UserB', action: '注册了新账号' },
        { time: '昨天', user: 'AdminC', action: '更新了算法题目 5001' },
    ];
};

onMounted(() => {
    fetchDashboardData().catch((err) => {
        console.error('加载管理端仪表盘数据失败', err);
    });
});

onBeforeUnmount(() => {
    if (examStatusChart) {
        examStatusChart.dispose();
        examStatusChart = null;
    }
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

.exam-status-chart {
  width: 100%;
  height: 240px;
}
</style>

