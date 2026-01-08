<template>
  <div class="product-page">
    <el-card>
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="产品名称">
          <el-input v-model="queryForm.name" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增产品
        </el-button>
      </div>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column label="图片" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" fit="cover" style="width: 60px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="180" />
        <el-table-column prop="enterpriseName" label="所属企业" width="200" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">￥{{ row.price.toLocaleString() }} / {{ row.unit }}</template>
        </el-table-column>
        <el-table-column prop="salesCount" label="销量" width="100" />
        <el-table-column prop="recommendWeight" label="推荐权重" width="120">
          <template #default="{ row }">
            <el-tag :type="getWeightType(row.recommendWeight)">{{ row.recommendWeight || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="queryForm.page" v-model:page-size="queryForm.size" :total="total" layout="total, prev, pager, next" @current-change="handleQuery" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="所属企业" prop="enterpriseId">
          <el-select v-model="formData.enterpriseId" placeholder="请选择企业" style="width: 100%">
            <el-option v-for="e in enterprises" :key="e.id" :label="e.enterpriseName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="formData.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="软件" value="软件" />
            <el-option label="装备制造" value="装备制造" />
            <el-option label="农产品" value="农产品" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 200px" />
          <el-input v-model="formData.unit" placeholder="单位" style="width: 100px; margin-left: 10px" />
        </el-form-item>
        <el-form-item label="推荐权重">
          <el-slider
            v-model="formData.recommendWeight"
            :min="0"
            :max="100"
            :step="10"
            show-input
            :marks="{ 0: '普通', 50: '推荐', 100: '热门' }"
          />
          <div class="weight-tip">
            <p>权重越高，产品在列表中越靠前</p>
            <p>0-30：普通展示 | 31-70：推荐展示 | 71-100：热门推荐</p>
          </div>
        </el-form-item>
        <el-form-item label="产品图片">
          <el-upload action="#" list-type="picture-card" :auto-upload="false">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="产品描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="4" placeholder="请输入产品描述" />
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
import { mockProducts, mockEnterprises } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const enterprises = ref(mockEnterprises.filter(e => e.status === 1))
const queryForm = reactive({ name: '', page: 1, size: 10 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增产品')
const formRef = ref()
const formData = reactive({ productName: '', enterpriseId: '', category: '', price: 0, unit: '', description: '', recommendWeight: 0 })
const formRules = { productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }] }

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockProducts]
    if (queryForm.name) data = data.filter((item: any) => item.productName.includes(queryForm.name))
    total.value = data.length
    tableData.value = data.slice((queryForm.page - 1) * queryForm.size, queryForm.page * queryForm.size)
    loading.value = false
  }, 500)
}

const handleQuery = () => { queryForm.page = 1; fetchData() }
const handleReset = () => { Object.assign(queryForm, { name: '', page: 1, size: 10 }); fetchData() }
const handleAdd = () => {
  dialogTitle.value = '新增产品'
  Object.assign(formData, { productName: '', enterpriseId: '', category: '', price: 0, unit: '', description: '', recommendWeight: 0 })
  dialogVisible.value = true
}
const handleEdit = (row: any) => { dialogTitle.value = '编辑产品'; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除「${row.productName}」吗？`, '删除确认', { type: 'warning' }).then(() => {
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

// 获取权重标签类型
const getWeightType = (weight: number) => {
  if (weight >= 71) return 'danger' // 热门
  if (weight >= 31) return 'warning' // 推荐
  return 'info' // 普通
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.product-page {
  .search-form { margin-bottom: 20px; }
  .toolbar { margin-bottom: 20px; }
  .pagination { display: flex; justify-content: flex-end; margin-top: 20px; }

  .weight-tip {
    margin-top: 10px;
    color: #999;
    font-size: 13px;
    line-height: 1.6;

    p {
      margin: 5px 0;
    }
  }
}
</style>
