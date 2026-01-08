<template>
  <div class="course-page">
    <el-card>
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="queryForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryForm.category" placeholder="请选择" clearable>
            <el-option label="政策解读" value="政策解读" />
            <el-option label="财务税务" value="财务税务" />
            <el-option label="数字化" value="数字化" />
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
          新增课程
        </el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column label="封面" width="120">
          <template #default="{ row }">
            <el-image :src="row.coverImage" fit="cover" style="width: 80px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="courseTypeName" label="类型" width="100" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model:current-page="queryForm.page" v-model:page-size="queryForm.size" :total="total" layout="total, prev, pager, next" @current-change="handleQuery" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="formData.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="政策解读" value="政策解读" />
            <el-option label="财务税务" value="财务税务" />
            <el-option label="数字化" value="数字化" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="courseType">
          <el-radio-group v-model="formData.courseType">
            <el-radio :value="1">文档</el-radio>
            <el-radio :value="2">视频</el-radio>
            <el-radio :value="3">音频</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="formData.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input v-model="formData.summary" type="textarea" :rows="3" placeholder="请输入简介" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="8" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mockCourses } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryForm = reactive({ title: '', category: '', page: 1, size: 10 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const formRef = ref()
const formData = reactive({ title: '', category: '', courseType: 1, author: '', summary: '', content: '' })
const formRules = { title: [{ required: true, message: '请输入标题', trigger: 'blur' }] }

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockCourses]
    if (queryForm.title) data = data.filter((item: any) => item.title.includes(queryForm.title))
    if (queryForm.category) data = data.filter((item: any) => item.category === queryForm.category)
    total.value = data.length
    tableData.value = data.slice((queryForm.page - 1) * queryForm.size, queryForm.page * queryForm.size)
    loading.value = false
  }, 500)
}

const handleQuery = () => { queryForm.page = 1; fetchData() }
const handleReset = () => { Object.assign(queryForm, { title: '', category: '', page: 1, size: 10 }); fetchData() }
const handleAdd = () => {
  dialogTitle.value = '新增课程'
  Object.assign(formData, { title: '', category: '', courseType: 1, author: '', summary: '', content: '' })
  dialogVisible.value = true
}
const handleEdit = (row: any) => { dialogTitle.value = '编辑课程'; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除「${row.title}」吗？`, '删除确认', { type: 'warning' }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  })
}
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (valid) {
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchData()
  }
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.course-page { .search-form { margin-bottom: 20px; } .toolbar { margin-bottom: 20px; } .pagination { display: flex; justify-content: flex-end; margin-top: 20px; } }
</style>
