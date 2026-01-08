<template>
  <div class="employee-page">
    <el-card>
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="员工姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增员工
        </el-button>
        <el-button type="success">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
      </div>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="employeeName" label="姓名" width="120" />
        <el-table-column prop="enterpriseName" label="所属企业" width="200" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="position" label="岗位" width="120" />
        <el-table-column prop="isAdmin" label="是否管理员" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isAdmin ? 'warning' : 'info'">{{ row.isAdmin ? '是' : '否' }}</el-tag>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="所属企业" prop="enterpriseId">
          <el-select v-model="formData.enterpriseId" placeholder="请选择企业" style="width: 100%">
            <el-option v-for="e in enterprises" :key="e.id" :label="e.enterpriseName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="employeeName">
          <el-input v-model="formData.employeeName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="formData.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="岗位" prop="position">
          <el-input v-model="formData.position" placeholder="请输入岗位" />
        </el-form-item>
        <el-form-item label="是否管理员">
          <el-switch v-model="formData.isAdmin" />
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
import { mockEmployees, mockEnterprises } from '@/mock'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const enterprises = ref(mockEnterprises.filter(e => e.status === 1))
const queryForm = reactive({ name: '', page: 1, size: 10 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const formRef = ref()
const formData = reactive({ enterpriseId: '', employeeName: '', phone: '', idCard: '', department: '', position: '', isAdmin: false })
const formRules = {
  employeeName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockEmployees]
    if (queryForm.name) data = data.filter((item: any) => item.employeeName.includes(queryForm.name))
    total.value = data.length
    tableData.value = data.slice((queryForm.page - 1) * queryForm.size, queryForm.page * queryForm.size)
    loading.value = false
  }, 500)
}

const handleQuery = () => { queryForm.page = 1; fetchData() }
const handleAdd = () => {
  dialogTitle.value = '新增员工'
  Object.assign(formData, { enterpriseId: '', employeeName: '', phone: '', idCard: '', department: '', position: '', isAdmin: false })
  dialogVisible.value = true
}
const handleEdit = (row: any) => { dialogTitle.value = '编辑员工'; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除员工「${row.employeeName}」吗？`, '删除确认', { type: 'warning' }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  })
}
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (valid) { ElMessage.success('操作成功'); dialogVisible.value = false; fetchData() }
}

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.employee-page { .search-form { margin-bottom: 20px; } .toolbar { margin-bottom: 20px; } .pagination { display: flex; justify-content: flex-end; margin-top: 20px; } }
</style>
