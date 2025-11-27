<template>
  <div class="admin-problem-list-view">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>题库列表管理</span>
        </div>
      </template>

      <div class="filter-and-action">
        
        <el-input
          v-model="searchQuery"
          placeholder="搜索题目名称或内容"
          clearable
          style="width: 300px; margin-right: 15px;"
          @change="fetchProblems"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select
          v-model="filter.difficulty"
          placeholder="筛选难度"
          clearable
          style="width: 120px; margin-right: 15px;"
          @change="fetchProblems"
        >
          <el-option label="简单" value="EASY" />
          <el-option label="中等" value="MEDIUM" />
          <el-option label="困难" value="HARD" />
        </el-select>
        
        <el-select
          v-model="filter.type"
          placeholder="筛选类型"
          clearable
          style="width: 120px; margin-right: 15px;"
          @change="fetchProblems"
        >
          <el-option label="单选题" value="SINGLE_CHOICE" />
          <el-option label="编程题" value="PROGRAMMING" />
          <el-option label="填空题" value="FILL_IN_BLANK" />
        </el-select>

        <el-button type="primary" :icon="Plus" @click="handleCreate">
          新增题目
        </el-button>
        <el-button :icon="Refresh" @click="fetchProblems" plain>
          刷新
        </el-button>
      </div>

      <el-table 
        :data="problemList" 
        v-loading="loading" 
        :style="{ width: '100%', marginTop: '20px' }" border
      >
        <el-table-column prop="id" label="ID" width="80" />
        
        <el-table-column prop="title" label="题目名称/描述" min-width="250" show-overflow-tooltip />
        
        <el-table-column label="类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)" effect="light" size="small">
              {{ formatType(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="难度" width="100">
          <template #default="scope">
            <el-tag :type="getDifficultyTag(scope.row.difficulty)" effect="dark" size="small">
              {{ formatDifficulty(scope.row.difficulty) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="creator" label="创建人" width="120" />

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleEdit(scope.row.id)">
              编辑
            </el-button>
            <el-button link type="info" size="small" @click="handleView(scope.row.id)">
              预览
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">
              删除
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
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Plus, Refresh } from '@element-plus/icons-vue';

// 假设的题目数据结构
interface Problem {
  id: number;
  title: string;
  type: 'SINGLE_CHOICE' | 'PROGRAMMING' | 'FILL_IN_BLANK';
  difficulty: 'EASY' | 'MEDIUM' | 'HARD';
  creator: string;
}

const router = useRouter();

// --- 状态管理 ---
const problemList = ref<Problem[]>([]);
const loading = ref(false);
const searchQuery = ref('');
const filter = ref({
  difficulty: '',
  type: '',
});
const pagination = ref({
  page: 1,
  limit: 10,
  total: 0,
});

// --- 模拟数据 ---
const mockData: Problem[] = [
  { id: 2001, title: '实现快速排序算法', type: 'PROGRAMMING', difficulty: 'HARD', creator: '张三' },
  { id: 2002, title: '什么是接口测试？', type: 'SINGLE_CHOICE', difficulty: 'EASY', creator: '李四' },
  { id: 2003, title: '数据库第三范式定义', type: 'FILL_IN_BLANK', difficulty: 'MEDIUM', creator: '王五' },
];
for (let i = 4; i <= 55; i++) {
  mockData.push({
    id: 2000 + i,
    title: `算法题 ${i}：请计算时间复杂度。`,
    type: i % 3 === 0 ? 'PROGRAMMING' : (i % 3 === 1 ? 'SINGLE_CHOICE' : 'FILL_IN_BLANK'),
    difficulty: i % 4 === 0 ? 'HARD' : (i % 4 === 1 ? 'MEDIUM' : 'EASY'),
    creator: i % 2 === 0 ? '张三' : '李四',
  });
}

// --- 数据获取（Mock实现） ---
const fetchProblems = async () => {
  loading.value = true;
  await new Promise(resolve => setTimeout(resolve, 500)); 
  
  let filteredData = mockData.filter(problem => {
    const searchMatch = problem.title.includes(searchQuery.value) || problem.id.toString().includes(searchQuery.value);
    const difficultyMatch = !filter.value.difficulty || problem.difficulty === filter.value.difficulty;
    const typeMatch = !filter.value.type || problem.type === filter.value.type;
    return searchMatch && difficultyMatch && typeMatch;
  });
  
  pagination.value.total = filteredData.length;
  const start = (pagination.value.page - 1) * pagination.value.limit;
  const end = start + pagination.value.limit;
  
  problemList.value = filteredData.slice(start, end);
  loading.value = false;
};

// --- 分页事件处理 ---
const handleSizeChange = (val: number) => {
  pagination.value.limit = val;
  pagination.value.page = 1;
  fetchProblems();
};

const handleCurrentChange = (val: number) => {
  pagination.value.page = val;
  fetchProblems();
};

// --- 操作方法 ---

const handleCreate = () => {
  router.push({ name: 'AdminProblemCreate' });
};

const handleEdit = (id: number) => {
  router.push({ name: 'AdminProblemEdit', params: { id: id.toString() } });
};

const handleView = (id: number) => {
    // 实际项目中：打开一个 modal 或新页面进行预览
    ElMessage.info(`预览题目 ID: ${id}`);
}

const handleDelete = async (id: number) => {
  ElMessageBox.confirm(
    `确定删除 ID 为 ${id} 的题目吗？`,
    '警告',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(async () => {
    // 实际项目中: 调用删除 API
    await new Promise(resolve => setTimeout(resolve, 300)); 
    const index = mockData.findIndex(p => p.id === id);
    if (index !== -1) {
      mockData.splice(index, 1);
    }
    ElMessage.success('题目删除成功！');
    fetchProblems(); 
  });
};

// --- 辅助函数：格式化显示 ---

const formatType = (type: Problem['type']) => {
    const map = {
        'SINGLE_CHOICE': '单选题',
        'PROGRAMMING': '编程题',
        'FILL_IN_BLANK': '填空题'
    };
    return map[type] || '未知';
}
const getTypeTag = (type: Problem['type']) => {
    const map = {
        'SINGLE_CHOICE': 'success',
        'PROGRAMMING': 'warning',
        'FILL_IN_BLANK': 'info'
    };
    // 确保返回的类型在 ElTag 支持的范围内
    return (map[type] as 'success' | 'warning' | 'info' | undefined) || 'info'; 
}

const formatDifficulty = (difficulty: Problem['difficulty']) => {
    const map = {
        'EASY': '简单',
        'MEDIUM': '中等',
        'HARD': '困难'
    };
    return map[difficulty] || '未知';
}
const getDifficultyTag = (difficulty: Problem['difficulty']) => {
    const map = {
        'EASY': 'success',
        'MEDIUM': 'warning',
        'HARD': 'danger'
    };
    // 确保返回的类型在 ElTag 支持的范围内
    return (map[difficulty] as 'success' | 'warning' | 'danger' | undefined) || 'info'; 
}


onMounted(() => {
  fetchProblems();
});
</script>

<style scoped>
.filter-and-action {
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
