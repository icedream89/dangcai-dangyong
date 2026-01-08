<template>
  <div class="policy-page">
    <el-card>
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="政策标题">
          <el-input v-model="queryForm.title" placeholder="请输入政策标题" clearable />
        </el-form-item>
        <el-form-item label="政策类型">
          <el-select v-model="queryForm.policyType" placeholder="请选择" clearable>
            <el-option label="国家级" :value="1" />
            <el-option label="省级" :value="2" />
            <el-option label="市级" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="已发布" :value="1" />
            <el-option label="草稿" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          发布政策
        </el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="policyTitle" label="政策标题" min-width="300" show-overflow-tooltip />
        <el-table-column prop="policyNo" label="发文号" width="180" />
        <el-table-column prop="policyTypeName" label="政策类型" width="100" />
        <el-table-column prop="issueDept" label="发文部门" width="180" />
        <el-table-column prop="issueDate" label="发布日期" width="120" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryForm.page"
          v-model:page-size="queryForm.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleQuery"
          @current-change="handleQuery"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px" @close="handleDialogClose">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="政策标题" prop="policyTitle">
              <el-input v-model="formData.policyTitle" placeholder="请输入政策标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发文号" prop="policyNo">
              <el-input v-model="formData.policyNo" placeholder="请输入发文号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="政策类型" prop="policyType">
              <el-select v-model="formData.policyType" placeholder="请选择" style="width: 100%">
                <el-option label="国家级" :value="1" />
                <el-option label="省级" :value="2" />
                <el-option label="市级" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发文部门" prop="issueDept">
              <el-input v-model="formData.issueDept" placeholder="请输入发文部门" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发布日期" prop="issueDate">
              <el-date-picker v-model="formData.issueDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="formData.effectiveDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker v-model="formData.expiryDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="政策摘要" prop="summary">
          <el-input v-model="formData.summary" type="textarea" :rows="3" placeholder="请输入政策摘要" />
        </el-form-item>

        <el-form-item label="政策内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="10" placeholder="请输入政策内容（支持富文本）" />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload action="#" list-type="picture-card" :auto-upload="false">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="标签">
          <el-select v-model="formData.tags" multiple placeholder="请选择标签" style="width: 100%">
            <el-option label="科技创新" value="科技创新" />
            <el-option label="资金支持" value="资金支持" />
            <el-option label="税收优惠" value="税收优惠" />
          </el-select>
        </el-form-item>

        <el-form-item label="其他设置">
          <el-checkbox v-model="formData.isTop">置顶</el-checkbox>
          <el-checkbox v-model="formData.isRecommended">推荐</el-checkbox>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ submitLoading ? '提交中...' : '确定' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="政策详情" width="900px">
      <div v-if="currentRow">
        <h2 style="text-align: center; margin-bottom: 20px;">{{ currentRow.policyTitle }}</h2>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="发文号">{{ currentRow.policyNo }}</el-descriptions-item>
          <el-descriptions-item label="政策类型">{{ currentRow.policyTypeName }}</el-descriptions-item>
          <el-descriptions-item label="发文部门">{{ currentRow.issueDept }}</el-descriptions-item>
          <el-descriptions-item label="发布日期">{{ currentRow.issueDate }}</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ currentRow.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="失效日期">{{ currentRow.expiryDate }}</el-descriptions-item>
          <el-descriptions-item label="浏览量">{{ currentRow.viewCount }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentRow.status === 1 ? 'success' : 'info'">{{ currentRow.statusName }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h3>政策摘要</h3>
        <p>{{ currentRow.summary }}</p>
        <el-divider />
        <h3>政策内容</h3>
        <div style="line-height: 1.8; color: #666;">
          {{ currentRow.content || '暂无详细内容' }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockPolicies } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryForm = reactive({
  title: '',
  policyType: '',
  status: '',
  page: 1,
  size: 10
})

const dialogVisible = ref(false)
const dialogTitle = ref('发布政策')
const submitLoading = ref(false)
const formRef = ref()
const formData = reactive({
  policyTitle: '',
  policyNo: '',
  policyType: 3,
  issueDept: '',
  issueDate: '',
  effectiveDate: '',
  expiryDate: '',
  summary: '',
  content: '',
  tags: [],
  isTop: false,
  isRecommended: false
})

const formRules = {
  policyTitle: [{ required: true, message: '请输入政策标题', trigger: 'blur' }],
  policyNo: [{ required: true, message: '请输入发文号', trigger: 'blur' }],
  policyType: [{ required: true, message: '请选择政策类型', trigger: 'change' }],
  issueDept: [{ required: true, message: '请输入发文部门', trigger: 'blur' }],
  issueDate: [{ required: true, message: '请选择发布日期', trigger: 'change' }]
}

const detailVisible = ref(false)
const currentRow = ref<any>(null)

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockPolicies]
    if (queryForm.title) {
      data = data.filter((item: any) => item.policyTitle.includes(queryForm.title))
    }
    if (queryForm.policyType) {
      data = data.filter((item: any) => item.policyType === queryForm.policyType)
    }
    if (queryForm.status !== '') {
      data = data.filter((item: any) => item.status === queryForm.status)
    }
    total.value = data.length
    const start = (queryForm.page - 1) * queryForm.size
    const end = start + queryForm.size
    tableData.value = data.slice(start, end)
    loading.value = false
  }, 500)
}

const handleQuery = () => {
  queryForm.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(queryForm, { title: '', policyType: '', status: '', page: 1, size: 10 })
  fetchData()
}

const handleAdd = () => {
  dialogTitle.value = '发布政策'
  Object.assign(formData, {
    policyTitle: '',
    policyNo: '',
    policyType: 3,
    issueDept: '',
    issueDate: '',
    effectiveDate: '',
    expiryDate: '',
    summary: '',
    content: '',
    tags: [],
    isTop: false,
    isRecommended: false
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑政策'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row: any) => {
  currentRow.value = row
  detailVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除政策「${row.policyTitle}」吗？`, '删除确认', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  })
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  setTimeout(() => {
    ElMessage.success(dialogTitle.value === '发布政策' ? '发布成功' : '编辑成功')
    dialogVisible.value = false
    submitLoading.value = false
    fetchData()
  }, 1000)
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.policy-page {
  .search-form { margin-bottom: 20px; }
  .toolbar { margin-bottom: 20px; }
  .pagination { display: flex; justify-content: flex-end; margin-top: 20px; }
}
</style>
