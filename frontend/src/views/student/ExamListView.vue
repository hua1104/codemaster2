<template>
  <div class="student-exam-list-view">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>我的考试</span>
        </div>
      </template>

      <div class="filter-area">
        <el-input
          v-model="searchQuery"
          placeholder="搜索考试名称或ID"
          clearable
          style="width: 300px; margin-right: 15px;"
          @change="fetchExams"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-button :icon="Refresh" @click="fetchExams" plain>
          刷新列表
        </el-button>
      </div>

      <el-tabs v-model="activeTab" class="exam-tabs" @tab-change="fetchExams">
        
        <el-tab-pane label="进行中/待开始" name="active">
          <ExamTable :data="activeExams" :loading="loading" @view-exam="handleViewExam" />
        </el-tab-pane>
        
        <el-tab-pane label="已结束/已评分" name="finished">
          <ExamTable :data="finishedExams" :loading="loading" @view-exam="handleViewExam" />
        </el-tab-pane>
      
      </el-tabs>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50]"
          :page-size="pagination.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background
        />
      </div>

    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineComponent } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Timer, Finished } from '@element-plus/icons-vue';

const router = useRouter();

// --- 类型定义 ---
interface Exam {
  id: number;
  name: string;
  status: 'SCHEDULED' | 'RUNNING' | 'FINISHED' | 'GRADED';
  startTime: string;
  duration: number; // 分钟
  score?: number; // 仅对 FINISHED/GRADED 有效
  submissionId?: number; // 提交记录ID
}

// --- 辅助组件：考试表格 (简化模板) ---
const ExamTable = defineComponent({
    props: {
        data: {
            type: Array as () => Exam[],
            required: true,
        },
        loading: Boolean,
    },
    emits: ['view-exam'],
    setup(props, { emit }) {
        // 辅助函数：根据状态获取标签类型
        const getStatusType = (status: Exam['status']) => {
            switch (status) {
                case 'RUNNING': return 'success';
                case 'SCHEDULED': return 'warning';
                case 'GRADED': return 'primary';
                case 'FINISHED': return 'info';
                default: return 'info';
            }
        };

        // 辅助函数：格式化状态文本
        const formatStatus = (status: Exam['status']) => {
            const map = {
                'RUNNING': '进行中',
                'SCHEDULED': '待开始',
                'FINISHED': '已结束',
                'GRADED': '已评分',
            };
            return map[status] || '未知';
        };

        const handleAction = (exam: Exam) => {
            emit('view-exam', exam.id);
        };

        return {
            getStatusType,
            formatStatus,
            handleAction,
            Timer,
            Finished
        };
    },
    template: `
        <el-table 
            :data="data" 
            v-loading="loading" 
            :style="{ width: '100%' }"
            stripe
        >
            <el-table-column prop="name" label="考试名称" min-width="200" show-overflow-tooltip />
            
            <el-table-column label="状态" width="100">
                <template #default="scope">
                    <el-tag :type="getStatusType(scope.row.status)" effect="light" size="small">
                        {{ formatStatus(scope.row.status) }}
                    </el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column prop="duration" label="时长(分钟)" width="120" />
            
            <el-table-column label="得分" width="100">
                <template #default="scope">
                    <span v-if="scope.row.status === 'GRADED'">
                        <el-tag type="danger" size="small">{{ scope.row.score }}</el-tag>
                    </span>
                    <span v-else>-</span>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="100" fixed="right">
                <template #default="scope">
                    <el-button 
                        link 
                        :type="scope.row.status === 'FINISHED' || scope.row.status === 'GRADED' ? 'success' : 'primary'"
                        size="small" 
                        @click="handleAction(scope.row)"
                    >
                        {{ scope.row.status === 'RUNNING' ? '进入考试' : (scope.row.status === 'SCHEDULED' ? '查看详情' : '查看结果') }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    `
});

// --- 状态管理 ---
const activeTab = ref('active');
const loading = ref(false);
const searchQuery = ref('');
const activeExams = ref<Exam[]>([]);
const finishedExams = ref<Exam[]>([]);
const pagination = ref({
  page: 1,
  limit: 10,
  total: 0, // 总数需要动态计算，这里简化
});

// --- 模拟数据 ---
const mockData: Exam[] = [
    { id: 1001, name: 'Web开发期中测试', status: 'RUNNING', startTime: '2025-11-26 10:00:00', duration: 90 },
    { id: 1002, name: 'Java算法设计', status: 'SCHEDULED', startTime: '2025-12-15 14:30:00', duration: 120 },
    { id: 1003, name: '软件工程原理', status: 'GRADED', startTime: '2025-10-01 09:00:00', duration: 60, score: 92 },
    { id: 1004, name: '数据库系统基础', status: 'FINISHED', startTime: '2025-10-10 13:00:00', duration: 75, submissionId: 401 },
    { id: 1005, name: '操作系统原理', status: 'SCHEDULED', startTime: '2025-12-25 10:00:00', duration: 180 },
    { id: 1006, name: '计算机网络', status: 'GRADED', startTime: '2025-09-05 15:00:00', duration: 60, score: 78 },
];
for (let i = 7; i <= 25; i++) {
    mockData.push({
        id: 1000 + i,
        name: `模拟考试 ${i}`,
        status: i % 2 === 0 ? 'SCHEDULED' : 'FINISHED',
        startTime: `2025-11-${i % 30 + 1} 10:00:00`,
        duration: 60,
    });
}


// --- 数据获取（Mock实现） ---
const fetchExams = async () => {
  loading.value = true;
  await new Promise(resolve => setTimeout(resolve, 500)); 
  
  let filteredData = mockData.filter(exam => 
    exam.name.includes(searchQuery.value) || exam.id.toString().includes(searchQuery.value)
  );

  activeExams.value = filteredData.filter(e => e.status === 'SCHEDULED' || e.status === 'RUNNING');
  finishedExams.value = filteredData.filter(e => e.status === 'FINISHED' || e.status === 'GRADED');
  
  // 简单处理分页（实际应在 API 层面处理分页）
  pagination.value.total = activeTab.value === 'active' ? activeExams.value.length : finishedExams.value.length;

  loading.value = false;
};

// --- 分页事件处理 ---
// 实际分页逻辑需要根据当前 activeTab 再次调用 API，并传递 page/limit 参数
const handleSizeChange = (val: number) => {
    pagination.value.limit = val;
    pagination.value.page = 1;
    ElMessage.info(`模拟 API 调用: ${activeTab.value} 列表，每页 ${val} 条`);
    // fetchExams(); // 在实际项目中调用
};

const handleCurrentChange = (val: number) => {
    pagination.value.page = val;
    ElMessage.info(`模拟 API 调用: ${activeTab.value} 列表，第 ${val} 页`);
    // fetchExams(); // 在实际项目中调用
};

// --- 操作方法 ---

const handleViewExam = (id: number) => {
    // 跳转到考试详情页 (或直接进入考试页)
    router.push({ name: 'StudentExamDetail', params: { id: id.toString() } });
};

onMounted(() => {
  fetchExams();
});
</script>

<style scoped>
.filter-area {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.exam-tabs {
  margin-top: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>