<template>
  <div class="enterprise-page">
    <el-card>
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.name" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="所属行业">
          <el-select v-model="queryForm.industry" placeholder="请选择" clearable>
            <el-option label="电子信息" value="电子信息" />
            <el-option label="装备制造" value="装备制造" />
            <el-option label="现代农业" value="现代农业" />
            <el-option label="新能源" value="新能源" />
            <el-option label="食品加工" value="食品加工" />
          </el-select>
        </el-form-item>
        <el-form-item label="企业状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="待审核" :value="0" />
            <el-option label="禁用" :value="2" />
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
          新增企业
        </el-button>
        <el-button type="success" @click="handleImport">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="enterpriseName" label="企业名称" min-width="200" />
        <el-table-column prop="unifiedCode" label="统一社会信用代码" width="180" />
        <el-table-column prop="legalPerson" label="法人代表" width="120" />
        <el-table-column prop="industry" label="所属行业" width="120" />
        <el-table-column prop="enterpriseType" label="企业类型" width="100" />
        <el-table-column prop="scale" label="企业规模" width="100" />
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button link type="warning" size="small" @click="handleAudit(row)" v-if="row.status === 0">
              <el-icon><Select /></el-icon>
              审核
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
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
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="enterpriseName">
              <el-input v-model="formData.enterpriseName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="统一社会信用代码" prop="unifiedCode">
              <el-input v-model="formData.unifiedCode" placeholder="请输入信用代码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表" prop="legalPerson">
              <el-input v-model="formData.legalPerson" placeholder="请输入法人代表" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人电话" prop="legalPersonPhone">
              <el-input v-model="formData.legalPersonPhone" placeholder="请输入法人电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="注册资本(万元)" prop="registeredCapital">
              <el-input-number v-model="formData.registeredCapital" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成立日期" prop="establishDate">
              <el-date-picker
                v-model="formData.establishDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-select v-model="formData.industry" placeholder="请选择行业" style="width: 100%">
                <el-option label="电子信息" value="电子信息" />
                <el-option label="装备制造" value="装备制造" />
                <el-option label="现代农业" value="现代农业" />
                <el-option label="新能源" value="新能源" />
                <el-option label="食品加工" value="食品加工" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业类型" prop="enterpriseType">
              <el-select v-model="formData.enterpriseType" placeholder="请选择类型" style="width: 100%">
                <el-option label="国有企业" value="国有企业" />
                <el-option label="民营企业" value="民营企业" />
                <el-option label="外资企业" value="外资企业" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业规模" prop="scale">
              <el-select v-model="formData.scale" placeholder="请选择规模" style="width: 100%">
                <el-option label="小型" value="小型" />
                <el-option label="中型" value="中型" />
                <el-option label="大型" value="大型" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否推荐" prop="isRecommended">
              <el-switch v-model="formData.isRecommended" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="企业地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入企业地址" />
        </el-form-item>

        <el-form-item label="企业简介" prop="intro">
          <el-input
            v-model="formData.intro"
            type="textarea"
            :rows="4"
            placeholder="请输入企业简介"
          />
        </el-form-item>

        <el-form-item label="资质文件">
          <el-upload
            v-model:file-list="qualificationFiles"
            action="#"
            list-type="picture-card"
            :on-preview="handlePicturePreview"
            :on-remove="handleRemove"
            :on-change="handleFileChange"
            :auto-upload="false"
            accept=".jpg,.jpeg,.png,.pdf"
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">
            <p>支持上传jpg、png、pdf格式，单个文件不超过5MB，最多上传5个文件</p>
            <p>建议上传：营业执照、税务登记证、组织机构代码证、相关资质证书等</p>
          </div>
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
    <el-dialog v-model="detailVisible" title="企业详情" width="900px">
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="企业名称">{{ currentRow.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">{{ currentRow.unifiedCode }}</el-descriptions-item>
        <el-descriptions-item label="法人代表">{{ currentRow.legalPerson }}</el-descriptions-item>
        <el-descriptions-item label="法人电话">{{ currentRow.legalPersonPhone }}</el-descriptions-item>
        <el-descriptions-item label="注册资本">{{ currentRow.registeredCapital }}万元</el-descriptions-item>
        <el-descriptions-item label="成立日期">{{ currentRow.establishDate }}</el-descriptions-item>
        <el-descriptions-item label="所属行业">{{ currentRow.industry }}</el-descriptions-item>
        <el-descriptions-item label="企业类型">{{ currentRow.enterpriseType }}</el-descriptions-item>
        <el-descriptions-item label="企业规模">{{ currentRow.scale }}</el-descriptions-item>
        <el-descriptions-item label="企业状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ currentRow.statusName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="企业地址" :span="2">{{ currentRow.address }}</el-descriptions-item>
        <el-descriptions-item label="企业简介" :span="2">{{ currentRow.intro }}</el-descriptions-item>
        <el-descriptions-item label="资质文件" :span="2" v-if="currentRow.qualificationFiles && currentRow.qualificationFiles.length > 0">
          <div class="qualification-files">
            <el-tag
              v-for="(file, index) in currentRow.qualificationFiles"
              :key="index"
              style="margin-right: 10px; margin-bottom: 10px; cursor: pointer"
              @click="handleDownloadFile(file)"
            >
              <el-icon><Document /></el-icon>
              {{ file.name }}
            </el-tag>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图片预览">
      <img :src="previewUrl" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockEnterprises } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryForm = reactive({
  name: '',
  industry: '',
  status: '',
  page: 1,
  size: 10
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增企业')
const submitLoading = ref(false)
const formRef = ref()

// 资质文件相关
const qualificationFiles = ref<any[]>([])
const previewVisible = ref(false)
const previewUrl = ref('')

const formData = reactive({
  enterpriseName: '',
  unifiedCode: '',
  legalPerson: '',
  legalPersonPhone: '',
  registeredCapital: 0,
  establishDate: '',
  address: '',
  industry: '',
  enterpriseType: '',
  scale: '',
  isRecommended: false,
  intro: '',
  qualificationFiles: [] as any[]
})

const formRules = {
  enterpriseName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  unifiedCode: [{ required: true, message: '请输入统一社会信用代码', trigger: 'blur' }],
  legalPerson: [{ required: true, message: '请输入法人代表', trigger: 'blur' }],
  legalPersonPhone: [
    { required: true, message: '请输入法人电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  industry: [{ required: true, message: '请选择所属行业', trigger: 'change' }],
  enterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
  scale: [{ required: true, message: '请选择企业规模', trigger: 'change' }]
}

const detailVisible = ref(false)
const currentRow = ref<any>(null)

// 获取数据
const fetchData = () => {
  loading.value = true

  setTimeout(() => {
    let data = [...mockEnterprises]

    // 过滤
    if (queryForm.name) {
      data = data.filter((item: any) => item.enterpriseName.includes(queryForm.name))
    }
    if (queryForm.industry) {
      data = data.filter((item: any) => item.industry === queryForm.industry)
    }
    if (queryForm.status !== '') {
      data = data.filter((item: any) => item.status === queryForm.status)
    }

    total.value = data.length

    // 分页
    const start = (queryForm.page - 1) * queryForm.size
    const end = start + queryForm.size
    tableData.value = data.slice(start, end)

    loading.value = false
  }, 500)
}

// 查询
const handleQuery = () => {
  queryForm.page = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(queryForm, {
    name: '',
    industry: '',
    status: '',
    page: 1,
    size: 10
  })
  fetchData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增企业'
  Object.assign(formData, {
    enterpriseName: '',
    unifiedCode: '',
    legalPerson: '',
    legalPersonPhone: '',
    registeredCapital: 0,
    establishDate: '',
    address: '',
    industry: '',
    enterpriseType: '',
    scale: '',
    isRecommended: false,
    intro: '',
    qualificationFiles: []
  })
  qualificationFiles.value = []
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑企业'
  Object.assign(formData, row)
  qualificationFiles.value = row.qualificationFiles || []
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  currentRow.value = row
  detailVisible.value = true
}

// 审核
const handleAudit = (row: any) => {
  ElMessageBox.confirm(`确定通过企业「${row.enterpriseName}」的审核吗？`, '审核确认', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('审核通过')
    fetchData()
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除企业「${row.enterpriseName}」吗？`, '删除确认', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  })
}

// 提交表单
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true

  setTimeout(() => {
    ElMessage.success(dialogTitle.value === '新增企业' ? '新增成功' : '编辑成功')
    dialogVisible.value = false
    submitLoading.value = false
    fetchData()
  }, 1000)
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 导入
const handleImport = () => {
  ElMessage.info('批量导入功能开发中')
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

// 文件预览
const handlePicturePreview = (file: any) => {
  previewUrl.value = file.url
  previewVisible.value = true
}

// 文件移除
const handleRemove = (file: any) => {
  const index = qualificationFiles.value.indexOf(file)
  if (index > -1) {
    qualificationFiles.value.splice(index, 1)
  }
}

// 文件改变
const handleFileChange = (file: any, fileList: any[]) => {
  qualificationFiles.value = fileList
}

// 下载文件
const handleDownloadFile = (file: any) => {
  ElMessage.info(`下载文件：${file.name}`)
}

// 状态类型
const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    0: 'info',
    1: 'success',
    2: 'danger'
  }
  return map[status] || 'info'
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.enterprise-page {
  .search-form {
    margin-bottom: 20px;
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .upload-tip {
    margin-top: 10px;
    color: #999;
    font-size: 13px;
    line-height: 1.6;

    p {
      margin: 5px 0;
    }
  }

  .qualification-files {
    display: flex;
    flex-wrap: wrap;
  }
}
</style>
