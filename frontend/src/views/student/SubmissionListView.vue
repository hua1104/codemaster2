<template>
  <div class="submission-list-view">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>我的提交历史</span>
        </div>
      </template>

      <div class="filter-area">
        <el-input
          v-model="searchQuery"
          placeholder="搜索考试或题目名称"
          clearable
          style="width: 250px; margin-right: 15px;"
          @change="fetchSubmissions"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select
          v-model="filter.result"
          placeholder="筛选提交结果"
          clearable
          style="width: 150px; margin-right: 15px;"
          @change="fetchSubmissions"
        >
          <el-option label="通过 (AC)" value="ACCEPTED" />
          <el-option label="错误 (WA)" value="WRONG_ANSWER" />
          <el-option label="待评分" value="PENDING" />
          <el-option label="编译失败" value="COMPILE_ERROR" />
        </el-select>

        <el-button :icon="Refresh" @click="fetchSubmissions" plain>
          刷新
        </el-button>
      </div>

      <el-table 
        :data="submissionList" 
        v-loading="loading" 
        :style="{ width: '100%', marginTop: '20px' }"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        
        <el-table-column prop="problemTitle" label="题目名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="examName" label="所属考试" min-width="180" show-overflow-tooltip />
        
        <el-table-column label="结果" width="120">
          <template #default="scope">
            <el-tag :type="getResultType(scope.row.result)" effect="dark" size="small">
              {{ formatResult(scope.row.result) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="score" label="得分" width="100">
          <template #default="scope">
            <span v-if="scope.row.result === 'ACCEPTED'">100</span>
            <span v-else-if="scope.row.score !== undefined">{{ scope.row.score }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="submitTime" label="提交时间" width="180" />

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleViewDetail(scope.row.id)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Search, Refresh } from '@element-plus/icons-vue';

const router = useRouter();

// --- 类型定义 ---
interface Submission {
  id: number;
  problemTitle: string;
  examName: string;
  result: 'ACCEPTED' | 'WRONG_ANSWER' | 'PENDING' | 'COMPILE_ERROR';
  score?: number; 
  submitTime: string;
}

// --- 状态管理 ---
const loading = ref(false);
const searchQuery = ref('');
const submissionList = ref<Submission[]>([]);
const filter = ref({
  result: '',
});
const pagination = ref({
  page: 1,
  limit: 10,
  total: 0,
});

// --- 模拟数据 ---
const mockData: Submission[] = [
    { id: 5001, problemTitle: '实现二分查找', examName: 'Java算法设计', result: 'ACCEPTED', score: 100, submitTime: '2025-11-20 10:30:00' },
    { id: 5002, problemTitle: '数据库事务隔离级别', examName: '数据库系统基础', result: 'WRONG_ANSWER', score: 75, submitTime: '2025-11-18 14:05:00' },
    { id: 5003, problemTitle: 'Vue组件生命周期', examName: 'Web开发期中测试', result: 'PENDING', submitTime: '2025-11-15 09:55:00' },
    { id: 5004, problemTitle: '内存管理机制', examName: '操作系统原理', result: 'COMPILE_ERROR', score: 0, submitTime: '2025-11-10 11:20:00' },
];
for (let i = 5; i <= 35; i++) {
    mockData.push({
        id: 5000 + i,
        problemTitle: `编程题 ${i} - 字符串操作`,
        examName: `模拟考试 ${i % 3 + 1}`,
        result: i % 4 === 0 ? 'ACCEPTED' : (i % 4 === 1 ? 'WRONG_ANSWER' : (i % 4 === 2 ? 'PENDING' : 'COMPILE_ERROR')),
        score: i % 4 === 0 ? 100 : (i % 4 === 1 ? Math.floor(Math.random() * 50) + 50 : undefined),
        submitTime: `2025-11-${i % 25 + 1} 10:00:00`,
    });
}


// --- 数据获取（Mock实现） ---
const fetchSubmissions = async () => {
  loading.value = true;
  await new Promise(resolve => setTimeout(resolve, 500)); 
  
  let filteredData = mockData.filter(sub => {
    const searchMatch = sub.problemTitle.includes(searchQuery.value) || 
                        sub.examName.includes(searchQuery.value) ||
                        sub.id.toString().includes(searchQuery.value);
    const resultMatch = !filter.value.result || sub.result === filter.value.result;
    return searchMatch && resultMatch;
  });
  
  pagination.value.total = filteredData.length;
  const start = (pagination.value.page - 1) * pagination.value.limit;
  const end = start + pagination.value.limit;
  
  submissionList.value = filteredData.slice(start, end);
  loading.value = false;
};

// --- 分页事件处理 ---
const handleSizeChange = (val: number) => {
  pagination.value.limit = val;
  pagination.value.page = 1;
  fetchSubmissions();
};

const handleCurrentChange = (val: number) => {
  pagination.value.page = val;
  fetchSubmissions();
};

// --- 操作方法 ---
const handleViewDetail = (id: number) => {
    // 实际项目中：跳转到提交详情页，展示代码、测试用例结果等
    ElMessage.info(`跳转到提交记录 ID: ${id} 的详情页`);
    // router.push({ name: 'SubmissionDetail', params: { id: id.toString() } });
};


// --- 辅助函数：格式化显示 ---

const formatResult = (result: Submission['result']) => {
    const map = {
        'ACCEPTED': '通过 (AC)',
        'WRONG_ANSWER': '错误 (WA)',
        'PENDING': '待评分',
        'COMPILE_ERROR': '编译失败',
    };
    return map[result] || '未知';
}
const getResultType = (result: Submission['result']) => {
    const map = {
        'ACCEPTED': 'success',
        'WRONG_ANSWER': 'danger',
        'PENDING': 'warning',
        'COMPILE_ERROR': 'info',
    };
    // 确保返回的类型在 ElTag 支持的范围内
    return (map[result] as 'success' | 'danger' | 'warning' | 'info' | undefined) || 'info'; 
}


onMounted(() => {
  fetchSubmissions();
});
</script>

<style scoped>
.filter-area {
  display: flex;
  align-items: center;
  gap: 15px; 
  margin-bottom: 15px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>