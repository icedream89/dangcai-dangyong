<template>
  <div class="purchase-page">
    <el-card>
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="需求标题">
          <el-input v-model="queryForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待匹配" :value="1" />
            <el-option label="已匹配" :value="2" />
            <el-option label="已完成" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="requirementNo" label="需求编号" width="160" />
        <el-table-column prop="title" label="需求标题" min-width="200" />
        <el-table-column prop="enterpriseName" label="发布企业" width="200" />
        <el-table-column prop="budget" label="预算(元)" width="120">
          <template #default="{ row }">￥{{ row.budget?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="replyCount" label="回复数" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看详情</el-button>
            <el-button link type="success" size="small" @click="handleMatch(row)" v-if="row.status === 1">
              <el-icon><Connection /></el-icon>
              匹配企业
            </el-button>
            <el-button link type="warning" size="small" @click="handleViewMatches(row)" v-if="row.status === 2">
              <el-icon><View /></el-icon>
              查看匹配
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="queryForm.page" v-model:page-size="queryForm.size" :total="total" layout="total, prev, pager, next" @current-change="handleQuery" />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="需求详情" width="800px">
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="需求编号">{{ currentRow.requirementNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ currentRow.statusName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="需求标题" :span="2">{{ currentRow.title }}</el-descriptions-item>
        <el-descriptions-item label="发布企业">{{ currentRow.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentRow.userName }} ({{ currentRow.userPhone }})</el-descriptions-item>
        <el-descriptions-item label="预算金额">￥{{ currentRow.budget?.toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="期望交付日期">{{ currentRow.expectDate }}</el-descriptions-item>
        <el-descriptions-item label="需求地址" :span="2">{{ currentRow.address }}</el-descriptions-item>
        <el-descriptions-item label="需求描述" :span="2">{{ currentRow.content }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="浏览次数">{{ currentRow.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="已匹配企业" :span="2" v-if="currentRow.matchedEnterprises && currentRow.matchedEnterprises.length > 0">
          <div class="matched-enterprises">
            <el-tag
              v-for="(enterprise, index) in currentRow.matchedEnterprises"
              :key="index"
              style="margin-right: 10px; margin-bottom: 10px"
              closable
              @close="handleUnmatchEnterprise(enterprise)"
            >
              {{ enterprise.enterpriseName }}
              <span v-if="enterprise.hasReplied" style="margin-left: 5px; color: #67c23a;">✓ 已回复</span>
            </el-tag>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 匹配企业对话框 -->
    <el-dialog v-model="matchVisible" title="匹配供应商企业" width="900px">
      <div class="match-info">
        <el-alert
          :title="`当前需求：${currentRow?.title}`"
          type="info"
          :closable="false"
          style="margin-bottom: 20px"
        >
          <template #default>
            <p>预算：￥{{ currentRow?.budget?.toLocaleString() }} | 期望交付：{{ currentRow?.expectDate }}</p>
            <p>需求描述：{{ currentRow?.content }}</p>
          </template>
        </el-alert>

        <el-form :inline="true" :model="matchQuery" class="search-form">
          <el-form-item label="企业名称">
            <el-input v-model="matchQuery.name" placeholder="搜索企业" clearable />
          </el-form-item>
          <el-form-item label="所属行业">
            <el-select v-model="matchQuery.industry" placeholder="选择行业" clearable>
              <el-option label="电子信息" value="电子信息" />
              <el-option label="装备制造" value="装备制造" />
              <el-option label="现代农业" value="现代农业" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQueryEnterprises">查询</el-button>
          </el-form-item>
        </el-form>

        <el-checkbox-group v-model="selectedEnterprises" style="width: 100%">
          <el-table :data="enterpriseList" @selection-change="handleSelectionChange" stripe>
            <el-table-column type="selection" width="55" />
            <el-table-column label="企业名称" prop="enterpriseName" min-width="180" />
            <el-table-column prop="industry" label="所属行业" width="120" />
            <el-table-column prop="legalPerson" label="法人" width="100" />
            <el-table-column prop="scale" label="规模" width="100" />
            <el-table-column prop="address" label="地址" min-width="150" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="handleViewEnterprise(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-checkbox-group>
      </div>

      <template #footer>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="color: #999">已选择 {{ selectedEnterprises.length }} 家企业</span>
          <div>
            <el-button @click="matchVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmitMatch" :disabled="selectedEnterprises.length === 0">
              确认匹配
            </el-button>
          </div>
        </div>
      </template>
    </el-dialog>

    <!-- 匹配结果查看对话框 -->
    <el-dialog v-model="matchResultVisible" title="匹配结果" width="800px">
      <div class="match-results">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in matchResults"
            :key="index"
            :timestamp="item.matchTime"
            placement="top"
          >
            <el-card>
              <h4>{{ item.enterpriseName }}</h4>
              <p><el-icon><User /></el-icon> 联系人：{{ item.contactPerson }} | {{ item.contactPhone }}</p>
              <p><el-icon><Phone /></el-icon> 电话：{{ item.phoneNumber }}</p>
              <p><el-icon><Location /></el-icon> 地址：{{ item.address }}</p>
              <el-tag :type="item.hasReplied ? 'success' : 'info'" style="margin-top: 10px">
                {{ item.hasReplied ? '✓ 已回复需求' : '尚未回复' }}
              </el-tag>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockPurchaseRequirements, mockEnterprises } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryForm = reactive({ title: '', status: '', page: 1, size: 10 })
const detailVisible = ref(false)
const currentRow = ref<any>(null)

// 匹配相关
const matchVisible = ref(false)
const matchResultVisible = ref(false)
const matchQuery = reactive({ name: '', industry: '' })
const enterpriseList = ref<any[]>([])
const selectedEnterprises = ref<any[]>([])
const matchResults = ref<any[]>([])

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockPurchaseRequirements]
    if (queryForm.title) data = data.filter((item: any) => item.title.includes(queryForm.title))
    if (queryForm.status) data = data.filter((item: any) => item.status === queryForm.status)
    total.value = data.length
    tableData.value = data.slice((queryForm.page - 1) * queryForm.size, queryForm.page * queryForm.size)
    loading.value = false
  }, 500)
}

const handleQuery = () => { queryForm.page = 1; fetchData() }
const handleView = (row: any) => { currentRow.value = row; detailVisible.value = true }

// 打开匹配对话框
const handleMatch = (row: any) => {
  currentRow.value = row
  selectedEnterprises.value = []
  // 加载企业列表
  enterpriseList.value = [...mockEnterprises.filter(e => e.status === 1)]
  matchVisible.value = true
}

// 查询企业
const handleQueryEnterprises = () => {
  let data = [...mockEnterprises.filter(e => e.status === 1)]
  if (matchQuery.name) {
    data = data.filter((e: any) => e.enterpriseName.includes(matchQuery.name))
  }
  if (matchQuery.industry) {
    data = data.filter((e: any) => e.industry === matchQuery.industry)
  }
  enterpriseList.value = data
}

// 表格选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedEnterprises.value = selection
}

// 查看企业详情
const handleViewEnterprise = (enterprise: any) => {
  ElMessageBox.alert(`
    企业名称：${enterprise.enterpriseName}
    法人代表：${enterprise.legalPerson}
    所属行业：${enterprise.industry}
    企业规模：${enterprise.scale}
    联系电话：${enterprise.legalPersonPhone}
    企业地址：${enterprise.address}
    企业简介：${enterprise.intro}
  `, '企业详情', { confirmButtonText: '关闭' })
}

// 提交匹配
const handleSubmitMatch = () => {
  ElMessageBox.confirm(
    `确定将此需求匹配给 ${selectedEnterprises.value.length} 家企业吗？匹配后系统将通知这些企业。`,
    '确认匹配',
    { type: 'warning' }
  ).then(() => {
    // 模拟匹配成功
    if (currentRow.value) {
      currentRow.value.status = 2 // 更新为已匹配状态
      currentRow.value.matchedEnterprises = selectedEnterprises.value.map((e: any) => ({
        ...e,
        hasReplied: false,
        matchTime: new Date().toLocaleString()
      }))
    }

    ElMessage.success('匹配成功！已通知相关企业')
    matchVisible.value = false
    fetchData()
  })
}

// 查看匹配结果
const handleViewMatches = (row: any) => {
  currentRow.value = row
  matchResults.value = row.matchedEnterprises || []
  matchResultVisible.value = true
}

// 取消匹配企业
const handleUnmatchEnterprise = (enterprise: any) => {
  ElMessageBox.confirm(
    `确定取消与「${enterprise.enterpriseName}」的匹配吗？`,
    '取消匹配',
    { type: 'warning' }
  ).then(() => {
    if (currentRow.value && currentRow.value.matchedEnterprises) {
      currentRow.value.matchedEnterprises = currentRow.value.matchedEnterprises.filter(
        (e: any) => e.id !== enterprise.id
      )
      if (currentRow.value.matchedEnterprises.length === 0) {
        currentRow.value.status = 1 // 如果没有匹配企业了，恢复为待匹配
      }
    }
    ElMessage.success('已取消匹配')
  })
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = { 1: 'info', 2: 'warning', 3: 'warning', 4: 'success', 5: 'info' }
  return map[status] || 'info'
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.purchase-page {
  .search-form { margin-bottom: 20px; }
  .pagination { display: flex; justify-content: flex-end; margin-top: 20px; }

  .matched-enterprises {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .match-info {
    .search-form {
      margin-bottom: 15px;
    }
  }

  .match-results {
    h4 {
      margin: 0 0 10px 0;
      font-size: 16px;
      color: #333;
    }

    p {
      margin: 5px 0;
      color: #666;
      font-size: 14px;

      .el-icon {
        margin-right: 5px;
      }
    }

    .el-card {
      margin-bottom: 0;
    }
  }
}
</style>
