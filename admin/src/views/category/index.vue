<template>
  <div class="category-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>分类树</span>
          </template>
          <el-tree
            :data="treeData"
            node-key="id"
            default-expand-all
            :props="{ label: 'categoryName', children: 'children' }"
            @node-click="handleNodeClick"
          >
            <template #default="{ node, data }">
              <span class="tree-node">
                <span>{{ node.label }}</span>
                <span class="tree-actions">
                  <el-button link type="primary" size="small" @click.stop="handleAddChild(data)">添加子项</el-button>
                  <el-button link type="primary" size="small" @click.stop="handleEdit(data)">编辑</el-button>
                </span>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>{{ currentCategory ? currentCategory.categoryName + ' - 子分类' : '子分类列表' }}</span>
          </template>
          <div v-if="currentCategory && tableData.length > 0">
            <el-table :data="tableData" stripe>
              <el-table-column type="index" label="#" width="60" />
              <el-table-column prop="categoryName" label="分类名称" />
              <el-table-column prop="categoryCode" label="分类编码" />
              <el-table-column prop="sortOrder" label="排序" width="100" />
              <el-table-column prop="manageMode" label="管理模式" width="120">
                <template #default="{ row }">
                  <el-tag :type="getManageModeType(row.manageMode)">{{ getManageModeName(row.manageMode) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-switch v-model="row.status" :active-value="1" :inactive-value="0" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="{ row }">
                  <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                  <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <el-empty v-else description="请点击左侧分类树查看子分类" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="上级分类">
          <el-tree-select :data="treeData" v-model="formData.parentId" :props="{ label: 'categoryName', value: 'id' }" check-strictly clearable placeholder="选择上级分类（不选则为根分类）" />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类编码">
          <el-input v-model="formData.categoryCode" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="管理模式" prop="manageMode">
          <el-select v-model="formData.manageMode" placeholder="请选择管理模式" style="width: 100%">
            <el-option label="列表模式" value="list">
              <div style="display: flex; align-items: center; justify-content: space-between">
                <span>列表模式</span>
                <el-tag size="small" type="primary">默认</el-tag>
              </div>
            </el-option>
            <el-option label="网格模式" value="grid">
              <div style="display: flex; align-items: center; justify-content: space-between">
                <span>网格模式</span>
                <el-tag size="small" type="success">卡片展示</el-tag>
              </div>
            </el-option>
            <el-option label="树形模式" value="tree">
              <div style="display: flex; align-items: center; justify-content: space-between">
                <span>树形模式</span>
                <el-tag size="small" type="warning">层级结构</el-tag>
              </div>
            </el-option>
            <el-option label="图文模式" value="image">
              <div style="display: flex; align-items: center; justify-content: space-between">
                <span>图文模式</span>
                <el-tag size="small" type="info">带图片</el-tag>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortOrder" :min="0" />
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

const treeData = ref([
  {
    id: 1,
    categoryName: '电子信息',
    categoryCode: 'XX001',
    parentId: 0,
    sortOrder: 1,
    status: 1,
    manageMode: 'list',
    children: [
      { id: 11, categoryName: '软件开发', categoryCode: 'XX001-01', parentId: 1, sortOrder: 1, status: 1, manageMode: 'grid' },
      { id: 12, categoryName: '硬件制造', categoryCode: 'XX001-02', parentId: 1, sortOrder: 2, status: 1, manageMode: 'list' }
    ]
  },
  {
    id: 2,
    categoryName: '装备制造',
    categoryCode: 'XX002',
    parentId: 0,
    sortOrder: 2,
    status: 1,
    manageMode: 'list',
    children: [
      { id: 21, categoryName: '数控设备', categoryCode: 'XX002-01', parentId: 2, sortOrder: 1, status: 1, manageMode: 'image' }
    ]
  },
  {
    id: 3,
    categoryName: '现代农业',
    categoryCode: 'XX003',
    parentId: 0,
    sortOrder: 3,
    status: 1,
    manageMode: 'grid'
  }
])

const tableData = ref([])
const currentCategory = ref<any>(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref()
const formData = reactive({ parentId: 0, categoryName: '', categoryCode: '', sortOrder: 0, manageMode: 'list' })
const formRules = { categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }] }

const handleNodeClick = (data: any) => {
  currentCategory.value = data
  tableData.value = data.children || []
}

const handleAddChild = (data: any) => {
  dialogTitle.value = '新增子分类'
  Object.assign(formData, { parentId: data.id, categoryName: '', categoryCode: '', sortOrder: 0, manageMode: 'list' })
  dialogVisible.value = true
}

const handleEdit = (data: any) => {
  dialogTitle.value = '编辑分类'
  Object.assign(formData, data)
  dialogVisible.value = true
}

const handleDelete = (data: any) => {
  ElMessageBox.confirm(`确定要删除分类「${data.categoryName}」吗？`, '删除确认', { type: 'warning' }).then(() => {
    ElMessage.success('删除成功')
  })
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (valid) {
    ElMessage.success('操作成功')
    dialogVisible.value = false
  }
}

// 获取管理模式名称
const getManageModeName = (mode: string) => {
  const modeMap: Record<string, string> = {
    'list': '列表模式',
    'grid': '网格模式',
    'tree': '树形模式',
    'image': '图文模式'
  }
  return modeMap[mode] || '列表模式'
}

// 获取管理模式标签类型
const getManageModeType = (mode: string) => {
  const typeMap: Record<string, string> = {
    'list': 'primary',
    'grid': 'success',
    'tree': 'warning',
    'image': 'info'
  }
  return typeMap[mode] || 'primary'
}

// 页面加载时默认选中第一个分类
onMounted(() => {
  if (treeData.value && treeData.value.length > 0) {
    const firstCategory = treeData.value[0]
    currentCategory.value = firstCategory
    tableData.value = firstCategory.children || []
  }
})
</script>

<style scoped lang="scss">
.category-page {
  .tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-right: 8px;

    .tree-actions {
      display: none;
    }

    &:hover .tree-actions {
      display: block;
    }
  }
}
</style>
