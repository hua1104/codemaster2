<template>
  <div class="student-dashboard-view">
    
    <el-card shadow="hover" class="welcome-card">
      <div class="welcome-content">
        <el-avatar :size="60" :icon="UserFilled" style="margin-right: 20px;" />
        <div class="text-content">
          <h1 class="welcome-title">你好，{{ userName }}！</h1>
          <p class="welcome-subtitle">欢迎回到 Code Examner 在线考试系统。请查看您的待办事项。</p>
        </div>
        <el-button type="primary" plain :icon="Setting" @click="goToProfile">
          前往个人设置
        </el-button>
      </div>
    </el-card>

    <el-row :gutter="20" class="data-row">
      <el-col :span="6">
        <StatisticCard icon-type="Clock" title="待考场次" :value="stats.pendingExams" type="warning" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="DocumentChecked" title="已完成考试" :value="stats.finishedExams" type="success" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="MessageBox" title="已提交题目" :value="stats.totalSubmissions" type="info" />
      </el-col>
      <el-col :span="6">
        <StatisticCard icon-type="TrophyBase" title="最高得分率" :value="stats.maxScoreRate" type="primary" :is-percent="true" />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="list-row">
      
      <el-col :span="12">
        <el-card shadow="never" class="list-card">
          <template #header>
            <div class="card-title">
              <el-icon><Calendar /></el-icon>
              <span>即将开始的考试 ({{ upcomingExams.length }})</span>
              <el-button link type="primary" @click="goToExams">更多</el-button>
            </div>
          </template>
          
          <el-table :data="upcomingExams" :show-header="false" stripe :style="{ width: '100%' }">
            <el-table-column prop="name" label="考试名称" show-overflow-tooltip min-width="150" />
            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column label="操作" width="80" fixed="right">
              <template #default="scope">
                <el-button link type="warning" size="small" @click="handleViewExam(scope.row.id)">查看</el-button>
              </template>
            </el-table-column><template #empty><div style="padding: 20px; color: var(--el-text-color-secondary);">暂无即将开始的考试</div></template>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="never" class="list-card">
          <template #header>
            <div class="card-title">
              <el-icon><Finished /></el-icon>
              <span>最近的提交记录 ({{ recentSubmissions.length }})</span>
              <el-button link type="primary" @click="goToSubmissions">更多</el-button>
            </div>
          </template>
          
          <el-table :data="recentSubmissions" :show-header="false" stripe :style="{ width: '100%' }">
            <el-table-column prop="problemTitle" label="题目" show-overflow-tooltip min-width="150" />
            <el-table-column prop="examName" label="来自考试" show-overflow-tooltip min-width="150" />
            <el-table-column prop="score" label="得分" width="80" />
            <el-table-column label="操作" width="80" fixed="right">
              <template #default="scope">
                <el-button link type="success" size="small" @click="handleViewSubmission(scope.row.id)">详情</el-button>
              </template>
            </el-table-column><template #empty><div style="padding: 20px; color: var(--el-text-color-secondary);">暂无提交记录</div></template>
          </el-table>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
// 导入 StatisticCard 组件 (请确保它存在于 '@/components/common/StatisticCard.vue')
import StatisticCard from '@/components/common/StatisticCard.vue'; 
import { 
  UserFilled, Setting, Calendar, Finished, Clock, DocumentChecked, MessageBox, TrophyBase 
} from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userName = ref('普通学生'); 

// --- 模拟数据类型 ---
interface UpcomingExam {
    id: number;
    name: string;
    startTime: string;
}
interface RecentSubmission {
    id: number;
    problemTitle: string;
    examName: string;
    score: number;
}


// --- 状态管理 ---
const stats = ref({
    pendingExams: 3,
    finishedExams: 15,
    totalSubmissions: 55,
    maxScoreRate: 95.5
});

const upcomingExams = ref<UpcomingExam[]>([]);
const recentSubmissions = ref<RecentSubmission[]>([]);

// --- 数据获取（Mock实现） ---
const fetchDashboardData = async () => {
    // 模拟获取统计数据
    await new Promise(resolve => setTimeout(resolve, 300));

    // 模拟即将开始的考试
    upcomingExams.value = [
        { id: 105, name: 'Web安全基础测试', startTime: '2025-12-10 09:00' },
        { id: 106, name: '高级数据结构期末考', startTime: '2025-12-20 14:00' },
        { id: 107, name: '操作系统原理补考', startTime: '2026-01-05 10:00' },
    ];
    
    // 模拟最近的提交记录
    recentSubmissions.value = [
        { id: 201, problemTitle: '冒泡排序优化', examName: 'Java算法设计', score: 95 },
        { id: 202, problemTitle: '数据库事务隔离级别', examName: '数据库系统基础', score: 80 },
        { id: 203, problemTitle: 'Vue组件生命周期', examName: 'Web开发期中测试', score: 100 },
    ];
};

// --- 操作方法 ---
const goToProfile = () => {
    router.push({ name: 'StudentProfile' });
};

const goToExams = () => {
    router.push({ name: 'StudentExamList' });
};

const goToSubmissions = () => {
    router.push({ name: 'StudentSubmissionList' });
};

const handleViewExam = (id: number) => {
    router.push({ name: 'StudentExamDetail', params: { id: id.toString() } }); 
};

const handleViewSubmission = (id: number) => {
    // 假设您没有 StudentSubmissionDetail 路由，先跳转到列表
    router.push({ name: 'StudentSubmissionList' });
};


onMounted(() => {
    fetchDashboardData();
});
</script>

<style scoped>
/* 使用标准空格和缩进，解决不可见字符问题 */
.welcome-card {
  margin-bottom: 20px;
}
.welcome-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.welcome-title {
  font-size: 22px;
  margin: 0;
  color: var(--el-color-primary);
}
.welcome-subtitle {
  color: var(--el-text-color-secondary);
  margin-top: 5px;
}

.data-row {
  margin-bottom: 20px;
}

/* --- List Card Styles --- */
.list-row {
  margin-bottom: 20px;
}
.list-card {
  height: 100%;
}
.card-title {
  display: flex;
  align-items: center;
  font-size: 16px;
}
.card-title .el-icon {
  margin-right: 8px;
  color: var(--el-color-primary);
}
.card-title span {
  flex-grow: 1;
  font-weight: bold;
}
</style>