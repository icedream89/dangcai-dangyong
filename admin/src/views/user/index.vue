<template>
  <div class="user-page">
    <el-card>
      <el-form :inline="true" class="search-form">
        <el-form-item label="用户名">
          <el-input placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
      </div>

      <el-table :data="mockUsers" stripe>
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="userTypeName" label="用户类型" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default>
            <el-button link type="primary" size="small">编辑</el-button>
            <el-button link type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination :total="100" layout="total, prev, pager, next" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const mockUsers = ref([
  { id: 1, username: 'admin', realName: '系统管理员', phone: '13800000001', userTypeName: '局管理员', status: 1 },
  { id: 2, username: 'ent001', realName: '张三', phone: '13800138001', userTypeName: '企业管理员', status: 1 },
  { id: 3, username: 'ent002', realName: '李四', phone: '13800138002', userTypeName: '企业管理员', status: 1 },
  { id: 4, username: 'user001', realName: '王五', phone: '13800138003', userTypeName: '企业员工', status: 1 },
  { id: 5, username: 'user002', realName: '赵六', phone: '13800138004', userTypeName: '普通用户', status: 1 }
])
</script>

<style scoped lang="scss">
.user-page { .search-form { margin-bottom: 20px; } .toolbar { margin-bottom: 20px; } .pagination { display: flex; justify-content: flex-end; margin-top: 20px; } }
</style>
