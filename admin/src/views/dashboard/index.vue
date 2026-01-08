<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in stats" :key="item.title">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: item.color }">
              <el-icon :size="30">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-value">{{ item.value }}</p>
              <p class="stat-title">{{ item.title }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最新求助工单</span>
              <el-button text @click="$router.push('/help')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentTickets" stripe>
            <el-table-column prop="ticketNo" label="工单编号" width="150" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="enterpriseName" label="企业" width="200" />
            <el-table-column prop="statusName" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ row.statusName }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="quick-actions">
            <div
              v-for="action in quickActions"
              :key="action.name"
              class="action-item"
              @click="$router.push(action.path)"
            >
              <el-icon :size="30" :color="action.color">
                <component :is="action.icon" />
              </el-icon>
              <span>{{ action.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快捷入口</span>
            </div>
          </template>
          <div class="quick-actions-full">
            <div
              v-for="action in quickActions"
              :key="action.name"
              class="action-item"
              @click="$router.push(action.path)"
            >
              <el-icon :size="40" :color="action.color">
                <component :is="action.icon" />
              </el-icon>
              <span>{{ action.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  OfficeBuilding,
  Document,
  Reading,
  ChatLineSquare,
  ShoppingCart,
  User
} from '@element-plus/icons-vue'
import { mockEnterprises, mockHelpTickets, mockPolicies } from '@/mock'

const stats = ref([
  { title: '企业总数', value: '156', icon: OfficeBuilding, color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { title: '政策文件', value: '89', icon: Document, color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { title: '企业课堂', value: '234', icon: Reading, color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { title: '求助工单', value: '45', icon: ChatLineSquare, color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
])

const quickActions = ref([
  { name: '发布政策', icon: Document, path: '/policy', color: '#409eff' },
  { name: '企业管理', icon: OfficeBuilding, path: '/enterprise', color: '#67c23a' },
  { name: '工单处理', icon: ChatLineSquare, path: '/help', color: '#e6a23c' },
  { name: '需求管理', icon: ShoppingCart, path: '/purchase', color: '#f56c6c' },
  { name: '课堂管理', icon: Reading, path: '/course', color: '#909399' },
  { name: '用户管理', icon: User, path: '/user', color: '#909399' }
])

const recentTickets = ref(mockHelpTickets.slice(0, 5))

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    1: 'info',
    2: 'warning',
    3: 'success',
    4: 'info'
  }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.dashboard {
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #333;
          margin-bottom: 5px;
        }

        .stat-title {
          font-size: 14px;
          color: #999;
          margin: 0;
        }
      }
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .quick-actions-full {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 20px;

    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 30px 20px;
      border-radius: 12px;
      background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
      cursor: pointer;
      transition: all 0.3s;
      border: 2px solid transparent;

      &:hover {
        background: linear-gradient(135deg, #e6f7ff 0%, #ffffff 100%);
        border-color: #409eff;
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
      }

      span {
        margin-top: 12px;
        font-size: 15px;
        font-weight: 500;
        color: #333;
      }
    }
  }

  .quick-actions {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;

    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 20px;
      border-radius: 8px;
      background-color: #f5f7fa;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background-color: #e6f7ff;
        transform: translateY(-2px);
      }

      span {
        margin-top: 10px;
        font-size: 14px;
        color: #333;
      }
    }
  }
}
</style>
