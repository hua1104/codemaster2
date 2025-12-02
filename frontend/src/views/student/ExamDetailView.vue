<template>
  <div class="exam-wrapper">
    <el-header class="exam-header">
      <div class="header-left">
        <h2 class="exam-title">{{ exam.name }}</h2>
        <el-tag type="warning" size="large" effect="dark" style="margin-left: 20px;">
          {{ isTaking ? '考试进行中' : '考试回顾' }}
        </el-tag>
      </div>

      <div class="header-right">
        <div class="timer-display">
          <el-icon :size="20"><Timer /></el-icon>
          <span style="font-weight: bold; font-size: 1.2em; margin-left: 5px;">{{ formattedTimeLeft }}</span>
        </div>
        
        <el-button 
          type="primary" 
          size="large" 
          :disabled="!isTaking" 
          @click="submitExam" 
          style="margin-left: 20px;"
        >
          提交试卷
        </el-button>
      </div>
    </el-header>

    <el-container class="exam-content">
      
      <el-aside width="250px" class="question-sidebar">
        <el-card shadow="never" class="sidebar-card">
          <template #header>
            <div class="card-header">
              <span>试题导航 ({{ currentProblemIndex + 1 }} / {{ exam.problems.length }})</span>
            </div>
          </template>
          
          <div class="problem-grid">
            <el-button
              v-for="(problem, index) in exam.problems"
              :key="problem.id"
              :type="getProblemStatusType(problem.status)"
              :plain="currentProblemIndex !== index"
              size="small"
              circle
              @click="setCurrentProblem(index)"
            >
              {{ index + 1 }}
            </el-button>
          </div>
        </el-card>
      </el-aside>

      <el-main class="problem-main">
        <el-card shadow="hover" class="problem-statement-card">
          <template #header>
            <div class="card-header">
              <span>问题 {{ currentProblemIndex + 1 }}：</span>
              <el-tag size="small">{{ exam.problems[currentProblemIndex]?.score }} 分</el-tag>
            </div>
          </template>
          
          <div v-html="exam.problems[currentProblemIndex]?.content" class="problem-content"></div>
        </el-card>

        <el-card shadow="hover" class="answer-card">
          <template #header>
            作答区（请在下方输入代码或文字）
          </template>
          
          <el-input 
            type="textarea" 
            :rows="10" 
            placeholder="在这里输入您的代码或答案..."
            v-model="currentAnswer"
            resize="none"
          />
        </el-card>

        <div class="problem-actions">
          <el-button @click="prevProblem" :disabled="currentProblemIndex === 0">上一题</el-button>
          <el-button @click="saveDraft" type="info" :icon="Document">暂存答案</el-button>
          <el-button @click="nextProblem" :disabled="currentProblemIndex === exam.problems.length - 1" type="primary">下一题</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Timer, Search, Document } from '@element-plus/icons-vue'; 
import { ElMessage, ElMessageBox } from 'element-plus';

// 模拟数据和类型定义
interface Problem {
  id: number;
  status: 'UNANSWERED' | 'ANSWERED' | 'FLAGGED';
  score: number;
  content: string;
  answer: string;
}

// 模拟考试状态数据 (在实际项目中应从 Pinia Store 获取)
const exam = ref({
  id: 1,
  name: '软件测试原理期末考试',
  duration: 90, // 分钟
  timeLeft: 3600, // 秒 (1小时)
  isTaking: true,
  problems: [
    { id: 101, status: 'UNANSWERED', score: 10, content: '<p>什么是黑盒测试？</p>', answer: '' },
    { id: 102, status: 'ANSWERED', score: 15, content: '<p>请用Java实现一个快速排序。</p>', answer: 'console.log("快排代码...");' },
    { id: 103, status: 'UNANSWERED', score: 10, content: '<p>解释单元测试的意义。</p>', answer: '' },
    { id: 104, status: 'FLAGGED', score: 20, content: '<p>计算时间复杂度。</p>', answer: '' },
    // 更多问题...
  ] as Problem[]
});

const currentProblemIndex = ref(0);
const isTaking = computed(() => exam.value.isTaking);


// 【修正后的计算属性】：添加了非空检查来解决 TypeScript/ESLint 警告
const currentAnswer = computed({
    // Getter: 使用可选链 ?. 安全地访问 answer 属性
    get: () => exam.value.problems[currentProblemIndex.value]?.answer || '',
    
    set: (val: string) => {
        const currentProblem = exam.value.problems[currentProblemIndex.value];
        
        // 【关键修正】：检查当前题目对象是否存在
        if (!currentProblem) {
            console.error("当前题目对象不存在，无法设置答案。");
            return;
        }

        // 赋值
        currentProblem.answer = val;
        
        // 自动将状态从未作答改为已作答 (如果作答区有内容)
        if (val.trim() && currentProblem.status === 'UNANSWERED') {
            currentProblem.status = 'ANSWERED';
        }
    }
});

// 计算属性：格式化剩余时间
const formattedTimeLeft = computed(() => {
  const seconds = exam.value.timeLeft % 60;
  const minutes = Math.floor(exam.value.timeLeft / 60) % 60;
  const hours = Math.floor(exam.value.timeLeft / 3600);
  
  const pad = (num: number) => num.toString().padStart(2, '0');
  return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
});

// 方法：获取题目状态对应的 Element Plus Tag 颜色
const getProblemStatusType = (status: Problem['status']) => {
  switch (status) {
    case 'ANSWERED': return 'success';
    case 'FLAGGED': return 'danger';
    case 'UNANSWERED': 
    default: return 'info';
  }
};

// 题目导航逻辑
const setCurrentProblem = (index: number) => {
  // 确保索引有效
  if (index >= 0 && index < exam.value.problems.length) {
    currentProblemIndex.value = index;
  }
};
const prevProblem = () => {
  setCurrentProblem(currentProblemIndex.value - 1);
};
const nextProblem = () => {
  setCurrentProblem(currentProblemIndex.value + 1);
};
const saveDraft = () => {
  // 实际项目中应调用 Pinia action 保存到后端
  ElMessage.success('答案已暂存！');
};

// 提交试卷逻辑
const submitExam = () => {
  const unansweredCount = exam.value.problems.filter(p => p.status === 'UNANSWERED').length;

  ElMessageBox.confirm(
    unansweredCount > 0 
      ? `您还有 ${unansweredCount} 道题未作答，确定现在提交吗？` 
      : '您已完成所有题目，确定提交试卷吗？',
    '提交确认',
    {
      confirmButtonText: '确定提交',
      cancelButtonText: '继续作答',
      type: 'warning',
    }
  )
  .then(() => {
    // 实际项目中：调用 Pinia action 提交最终答案
    ElMessage.success('试卷提交成功！正在等待批改结果...');
    exam.value.isTaking = false; // 停止考试状态
  })
  .catch(() => {
    ElMessage.info('已取消提交，请继续作答。');
  });
};
</script>

<style scoped>
/* 考试界面样式 */
.exam-wrapper {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.exam-header {
  background-color: var(--el-color-primary); /* 利用您的主题色 */
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.header-left {
  display: flex;
  align-items: center;
}
.exam-title {
  margin: 0;
  font-size: 1.5em;
}
.header-right {
  display: flex;
  align-items: center;
}
.timer-display {
  display: flex;
  align-items: center;
  padding: 5px 10px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

/* 主内容区域样式 */
.exam-content {
  flex-grow: 1;
  overflow: hidden;
  height: calc(100vh - 60px); /* 减去 Header 的高度 */
}

/* 侧边栏样式 */
.question-sidebar {
  padding: 15px;
  background-color: #f7f9fa; /* 浅灰色背景 */
  overflow-y: auto;
}
.sidebar-card {
  height: 100%;
}
.problem-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(40px, 1fr));
  gap: 10px;
  padding-top: 10px;
}

/* 主内容样式 */
.problem-main {
  padding: 15px;
  overflow-y: auto; /* 允许内容滚动 */
}
.problem-statement-card {
  margin-bottom: 20px;
}
.problem-content {
  line-height: 1.8;
  padding: 10px 0;
}
.answer-card {
  margin-bottom: 20px;
}
.problem-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding-bottom: 20px;
}
</style>