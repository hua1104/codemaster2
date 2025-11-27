<template>
    <div class="admin-user-list-view">
        <el-card shadow="never">
            <template #header>
                <div class="card-header">
                    <h2>用户列表</h2>
                    <el-button 
                        type="primary" 
                        :icon="Plus" 
                        disabled 
                        title="暂未实现用户创建功能"
                    >
                        新增用户
                    </el-button>
                </div>
            </template>

            <div class="filter-bar">
                <el-input 
                    v-model="queryForm.keyword" 
                    placeholder="搜索用户名或邮箱" 
                    clearable 
                    style="width: 300px; margin-right: 15px;"
                    @clear="handleSearch"
                    @keyup.enter="handleSearch"
                />
                <el-select 
                    v-model="queryForm.role" 
                    placeholder="筛选角色" 
                    clearable 
                    style="width: 150px; margin-right: 15px;"
                    @change="handleSearch"
                >
                    <el-option label="管理员" value="ADMIN" />
                    <el-option label="学生" value="STUDENT" />
                </el-select>
                <el-button type="primary" @click="handleSearch">查询</el-button>
            </div>

            <el-table 
                :data="userList" 
                v-loading="loading" 
                class="user-table" 
            >
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="username" label="用户名" min-width="150" />
                <el-table-column prop="email" label="邮箱" min-width="200" />
                <el-table-column prop="role" label="角色" width="100">
                    <template #default="{ row }">
                        <el-tag :type="getTagType(row.role)">
                            {{ row.role === 'ADMIN' ? '管理员' : '学生' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createdAt" label="注册时间" width="180" />
                
                <el-table-column label="操作" width="150" fixed="right">
                    <template #default="{ row }">
                        <el-button link type="warning" size="small" @click="handleEditRole(row.id)">修改角色</el-button>
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
import { Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios'; 

// --- 类型定义 ---
type UserRole = 'ADMIN' | 'STUDENT';
type TagType = 'info' | 'success' | 'danger' | 'warning' | 'primary';

interface User {
    id: number;
    username: string;
    email: string;
    role: UserRole;
    createdAt: string; // ISO 格式时间
}

interface QueryForm {
    page: number;
    size: number;
    keyword: string;
    role: UserRole | '';
}

// --- 状态管理 ---
const userList = ref<User[]>([]);
const total = ref(0);
const loading = ref(false);

const queryForm = reactive<QueryForm>({
    page: 1,
    size: 10,
    keyword: '',
    role: '',
});

// --- API 调用：获取用户列表 ---
const fetchUserList = async () => {
    loading.value = true;
    try {
        // 🚨 替换为您的实际后端 API 地址 🚨
        const response = await axios.get('/api/admin/users', {
            params: queryForm,
        });

        // 🚨 注意：这里使用模拟数据进行演示 🚨
        const mockData = {
             content: [
                 { id: 101, username: 'admin_test', email: 'admin@example.com', role: 'ADMIN' as const, createdAt: '2024-01-01T10:00:00' },
                 { id: 201, username: 'student_lily', email: 'lily@example.com', role: 'STUDENT' as const, createdAt: '2024-03-15T12:00:00' },
                 { id: 202, username: 'student_bob', email: 'bob@example.com', role: 'STUDENT' as const, createdAt: '2024-03-20T14:00:00' },
                 { id: 203, username: 'student_alice', email: 'alice@example.com', role: 'STUDENT' as const, createdAt: '2024-04-01T08:00:00' },
             ],
             totalElements: 4,
        };
        
        userList.value = response.data.content || mockData.content; 
        total.value = response.data.totalElements || mockData.totalElements;
        
    } catch (error) {
        ElMessage.error('加载用户列表失败，请检查 API 接口。');
        console.error('Fetch User List Error:', error);
    } finally {
        loading.value = false;
    }
};

// --- 操作函数 ---
const handleSearch = () => {
    queryForm.page = 1; 
    fetchUserList();
};

const handlePageChange = (newPage: number) => {
    queryForm.page = newPage;
    fetchUserList();
};

const handleEditRole = (id: number) => {
    // 实际逻辑应弹出对话框供管理员修改用户角色
    ElMessage.info(`尝试修改用户 ID: ${id} 的角色`);
};

const handleDelete = (id: number) => {
    ElMessageBox.confirm(
        '此操作将永久删除该用户，是否继续?',
        '警告',
        {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(async () => {
        try {
            // 🚨 替换为您的实际后端 API 地址 🚨
            // await axios.delete(`/api/admin/users/${id}`);
            
            ElMessage.success('删除成功！(API调用待实现)');
            fetchUserList();
        } catch (error) {
            ElMessage.error('删除失败，请重试！');
            console.error('Delete User Error:', error);
        }
    }).catch(() => {
        // 取消删除操作
    });
};

// --- 辅助函数 ---
const getTagType = (role: UserRole): TagType => {
    if (role === 'ADMIN') {
        return 'danger';
    }
    return 'success';
};

// --- 生命周期 ---
onMounted(() => {
    fetchUserList();
});
</script>

<style scoped>
.user-table {
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