<template>
    <el-card shadow="hover" class="statistic-card" :class="'card-' + type">
        <div class="card-content">
            <el-icon :size="40">
                <component :is="iconType" />
            </el-icon>
            <div class="text-area">
                <p class="card-value">{{ formattedValue }}{{ isPercent ? '%' : '' }}</p>
                <p class="card-title">{{ title }}</p>
            </div>
        </div>
    </el-card>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue';

const props = defineProps({
    /** Element Plus Icon 名称字符串，例如: 'Clock', 'DocumentChecked' */
    iconType: {
        type: String,
        required: true
    },
    title: String,
    /** 值可以是字符串或数字 */
    value: [String, Number],
    /** 样式类型：primary, success, warning, info */
    type: {
        type: String,
        default: 'primary' 
    }, 
    /** 是否以百分比格式显示 */
    isPercent: {
        type: Boolean,
        default: false
    }
});

// 计算属性：确保值是字符串，如果需要则保留两位小数
const formattedValue = computed(() => {
    if (typeof props.value === 'number') {
        // 如果是百分比，保留两位小数
        if (props.isPercent) {
            return props.value.toFixed(2);
        }
        // 对于普通数值，直接转为字符串
        return props.value.toString();
    }
    return props.value; // 如果已经是字符串，则直接返回
});
</script>

<style scoped>
.statistic-card {
    height: 100%;
    /* 增加过渡效果，使鼠标悬停效果更平滑 */
    transition: all 0.3s ease;
}

.statistic-card:hover {
    transform: translateY(-3px);
}

.card-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.text-area {
    text-align: right;
}
.card-value {
    font-size: 28px;
    font-weight: bold;
    margin: 0;
    line-height: 1.2;
}
.card-title {
    font-size: 14px;
    color: var(--el-text-color-secondary);
    margin-top: 5px;
}

/* 颜色样式 */
.statistic-card .el-icon {
    /* 默认图标颜色为 info */
    color: var(--el-color-info); 
}

/* 根据 type 属性设置值和图标颜色 */
.card-warning .el-icon,
.card-warning .card-value { 
    color: var(--el-color-warning); 
}
.card-success .el-icon,
.card-success .card-value { 
    color: var(--el-color-success); 
}
.card-info .el-icon,
.card-info .card-value { 
    color: var(--el-color-info); 
}
.card-primary .el-icon,
.card-primary .card-value { 
    color: var(--el-color-primary); 
}
</style>