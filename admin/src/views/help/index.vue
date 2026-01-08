<template>
  <div class="help-page">
    <el-card>
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="工单编号">
          <el-input v-model="queryForm.ticketNo" placeholder="请输入工单编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待处理" :value="1" />
            <el-option label="处理中" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已关闭" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="ticketNo" label="工单编号" width="160" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="enterpriseName" label="企业" width="200" />
        <el-table-column prop="userName" label="提交人" width="100" />
        <el-table-column prop="urgentLevelName" label="紧急程度" width="100">
          <template #default="{ row }">
            <el-tag :type="row.urgentLevel === 3 ? 'danger' : row.urgentLevel === 2 ? 'warning' : 'info'">
              {{ row.urgentLevelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click="handleProcess(row)" v-if="row.status === 1 || row.status === 2">处理</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="queryForm.page" v-model:page-size="queryForm.size" :total="total" layout="total, prev, pager, next" @current-change="handleQuery" />
      </div>
    </el-card>

    <!-- 处理工单对话框 -->
    <el-dialog v-model="processVisible" title="处理工单" width="600px">
      <el-descriptions :column="1" border v-if="currentRow">
        <el-descriptions-item label="工单编号">{{ currentRow.ticketNo }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ currentRow.title }}</el-descriptions-item>
        <el-descriptions-item label="提交人">{{ currentRow.userName }} ({{ currentRow.userPhone }})</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ currentRow.content }}</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="处理结果">
          <el-input v-model="processForm.result" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="变更状态">
          <el-radio-group v-model="processForm.status">
            <el-radio :value="2">处理中</el-radio>
            <el-radio :value="3">已完成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitProcess">提交</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="工单详情" width="800px">
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="工单编号">{{ currentRow.ticketNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ currentRow.statusName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentRow.title }}</el-descriptions-item>
        <el-descriptions-item label="企业">{{ currentRow.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="提交人">{{ currentRow.userName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="紧急程度">
          <el-tag :type="currentRow.urgentLevel === 3 ? 'danger' : currentRow.urgentLevel === 2 ? 'warning' : 'info'">
            {{ currentRow.urgentLevelName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">{{ currentRow.content }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentRow.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ currentRow.handleTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">{{ currentRow.handleResult || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户反馈" :span="2" v-if="currentRow.feedback">
          满意度：{{ currentRow.satisfactionName }}<br />
          {{ currentRow.feedback }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ currentRow.completeTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { mockHelpTickets } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryForm = reactive({ ticketNo: '', status: '', page: 1, size: 10 })
const processVisible = ref(false)
const detailVisible = ref(false)
const currentRow = ref<any>(null)
const processForm = reactive({ result: '', status: 2 })

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockHelpTickets]
    if (queryForm.ticketNo) data = data.filter((item: any) => item.ticketNo.includes(queryForm.ticketNo))
    if (queryForm.status) data = data.filter((item: any) => item.status === queryForm.status)
    total.value = data.length
    tableData.value = data.slice((queryForm.page - 1) * queryForm.size, queryForm.page * queryForm.size)
    loading.value = false
  }, 500)
}

const handleQuery = () => { queryForm.page = 1; fetchData() }
const handleReset = () => { Object.assign(queryForm, { ticketNo: '', status: '', page: 1, size: 10 }); fetchData() }
const handleView = (row: any) => { currentRow.value = row; detailVisible.value = true }
const handleProcess = (row: any) => { currentRow.value = row; processForm.result = row.handleResult || ''; processForm.status = row.status; processVisible.value = true }
const handleSubmitProcess = () => { ElMessage.success('处理成功'); processVisible.value = false; fetchData() }

const getStatusType = (status: number) => {
  const map: Record<number, string> = { 1: 'info', 2: 'warning', 3: 'success', 4: 'info' }
  return map[status] || 'info'
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.help-page { .search-form { margin-bottom: 20px; } .pagination { display: flex; justify-content: flex-end; margin-top: 20px; } }
</style>
