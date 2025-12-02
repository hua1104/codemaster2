<template>
    <div class="admin-exam-list-view">
        <el-card shadow="never">
            <template #header>
                <div class="card-header">
                    <h2>考试列表</h2>
                    <el-button 
                        type="primary" 
                        :icon="Plus" 
                        @click="router.push({ name: 'AdminExamCreate' })"
                    >
                        创建考试
                    </el-button>
                </div>
            </template>

            <div class="filter-bar">
                <el-input 
                    v-model="queryForm.keyword" 
                    placeholder="搜索考试名称" 
                    clearable 
                    style="width: 250px; margin-right: 15px;"
                    @clear="handleSearch"
                    @keyup.enter="handleSearch"
                />
                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <el-table 
                :data="examList" 
                v-loading="loading" 
                class="exam-table" 
            >
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="title" label="考试名称" min-width="180" />
                <el-table-column prop="startTime" label="开始时间" width="180" />
                <el-table-column prop="endTime" label="结束时间" width="180" />
                <el-table-column prop="duration" label="时长 (分钟)" width="120" />
                <el-table-column prop="totalScore" label="总分" width="80" />
                <el-table-column prop="status" label="状态" width="100">
                    <template #default="{ row }">
                        <el-tag :type="getTagType(row.status)">
                            {{ getStatusText(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                
                <el-table-column label="操作" width="180" fixed="right">
                    <template #default="{ row }">
                        <el-button link type="primary" size="small" @click="handleEdit(row.id)">编辑</el-button>
                        <el-button link type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="pagination-container">
                <el-pagination
                    @current-change="handlePageChange"
                    :current-page="queryForm.page"
                    :page-size="queryForm.size"
                    :total="total"
                    layout="total, prev, pager, next, jumper"
                    background
                />
            </div>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import apiClient from '@/services/apiClient';
import { endpoints } from '@/services/endpoints';

const router = useRouter();

// --- 类型定义 ---
interface Exam {
    id: number;
    title: string;
    startTime: string; // ISO 格式时间
    endTime: string;
    duration: number; // 分钟
    totalScore: number;
    status: 'SCHEDULED' | 'IN_PROGRESS' | 'FINISHED';
}

interface QueryForm {
    page: number;
    size: number;
    keyword: string;
}

// 🚨 修复点 1: 定义 Element Plus Tag 允许的类型 (用于 el-tag 的 type 属性) 🚨
type TagType = 'info' | 'success' | 'danger' | 'warning' | 'primary';

// --- 状态管理 ---
const examList = ref<Exam[]>([]);
const total = ref(0);
const loading = ref(false);

const queryForm = reactive<QueryForm>({
    page: 1,
    size: 10,
    keyword: '',
});

// --- API 调用：获取考试列表（管理员/教师自己的考试） ---
const fetchExamList = async () => {
    loading.value = true;
    try {
        // 后端分页是 0 开始，前端展示是从 1 开始
        const response = await apiClient.get(endpoints.admin.exams, {
            params: {
                page: queryForm.page - 1,
                size: queryForm.size,
                keyword: queryForm.keyword,
            },
        });

        const pageData = response.data;
        examList.value = pageData.content ?? [];
        total.value = pageData.totalElements ?? 0;
        // 同步后端实际页码（0-based）到前端展示（1-based）
        if (typeof pageData.number === 'number') {
            queryForm.page = pageData.number + 1;
        }
        
    } catch (error) {
        console.error('Fetch Exam List Error:', error);
        ElMessage.error('加载考试列表失败，请检查 API 接口。');
    } finally {
        loading.value = false;
    }
};

// --- 操作函数 ---
const handleSearch = () => {
    queryForm.page = 1; 
    fetchExamList();
};

const handlePageChange = (newPage: number) => {
    queryForm.page = newPage;
    fetchExamList();
};

const handleEdit = (id: number) => {
    router.push({ name: 'AdminExamEdit', params: { id: id.toString() } });
};

const handleDelete = (id: number) => {
    ElMessageBox.confirm(
        '此操作将永久删除该考试及其所有相关数据，是否继续?',
        '警告',
        {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(async () => {
        try {
            // await axios.delete(`/api/admin/exams/${id}`);
            ElMessage.success('删除成功！(API调用待实现)');
            fetchExamList();
        } catch (error) {
            ElMessage.error('删除失败，请重试！');
            console.error('Delete Exam Error:', error);
        }
    }).catch(() => {
        // 取消删除操作
    });
};

// --- 辅助函数 ---
const getStatusText = (status: Exam['status']) => {
    const map = {
        'SCHEDULED': '未开始',
        'IN_PROGRESS': '进行中',
        'FINISHED': '已结束',
    };
    return map[status] || '未知';
};

// 🚨 修复点 2: 明确指定返回类型为 TagType 🚨
const getTagType = (status: Exam['status']): TagType => {
    const map: Record<Exam['status'], TagType> = {
        'SCHEDULED': 'info',
        'IN_PROGRESS': 'success',
        'FINISHED': 'danger',
    };
    // 确保返回的值在 TagType 联合类型中
    return map[status] || 'info'; 
};

// --- 生命周期 ---
onMounted(() => {
    fetchExamList();
});
</script>

<style scoped>
.exam-table {
    width: 100%;
    margin-top: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.card-header h2 {
    font-size: 20px;
    margin: 0;
}

.filter-bar {
    display: flex;
    align-items: center;
    padding-bottom: 10px;
}

.pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}
</style>