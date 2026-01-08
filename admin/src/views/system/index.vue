<template>
  <div class="system-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="系统参数" name="param">
        <el-card>
          <el-form :model="paramForm" label-width="150px" style="max-width: 600px">
            <el-form-item label="系统名称">
              <el-input v-model="paramForm.systemName" />
            </el-form-item>
            <el-form-item label="系统Logo">
              <el-upload action="#" list-type="picture-card" :auto-upload="false">
                <el-icon><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="版权信息">
              <el-input v-model="paramForm.copyright" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item label="ICP备案号">
              <el-input v-model="paramForm.icp" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="数据字典" name="dict">
        <el-card>
          <div class="toolbar">
            <el-button type="primary">
              <el-icon><Plus /></el-icon>
              新增字典
            </el-button>
          </div>
          <el-table :data="dictData" stripe>
            <el-table-column prop="dictType" label="字典类型" width="150" />
            <el-table-column prop="dictLabel" label="字典标签" width="150" />
            <el-table-column prop="dictValue" label="字典值" width="150" />
            <el-table-column prop="dictSort" label="排序" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default>
                <el-button link type="primary" size="small">编辑</el-button>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="菜单配置" name="menu">
        <el-card>
          <div class="toolbar">
            <el-button type="primary">
              <el-icon><Plus /></el-icon>
              新增菜单
            </el-button>
          </div>
          <el-table :data="menuData" stripe row-key="id" :tree-props="{ children: 'children' }">
            <el-table-column prop="menuName" label="菜单名称" width="200" />
            <el-table-column prop="menuCode" label="菜单编码" width="150" />
            <el-table-column prop="icon" label="图标" width="100" />
            <el-table-column prop="sortOrder" label="排序" width="100" />
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
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="操作日志" name="log">
        <el-card>
          <el-table :data="logData" stripe>
            <el-table-column prop="moduleName" label="模块名称" width="120" />
            <el-table-column prop="businessType" label="业务类型" width="100" />
            <el-table-column prop="operName" label="操作人" width="100" />
            <el-table-column prop="operUrl" label="请求URL" min-width="200" show-overflow-tooltip />
            <el-table-column prop="operIp" label="操作IP" width="140" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '成功' : '失败' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="operTime" label="操作时间" width="180" />
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeTab = ref('param')

const paramForm = ref({
  systemName: '当才当用',
  copyright: '© 2024 当阳市科技经济信息商务局 版权所有',
  icp: '鄂ICP备XXXXXXXX号'
})

const dictData = ref([
  { dictType: 'user_type', dictLabel: '局管理员', dictValue: '1', dictSort: 1, status: 1 },
  { dictType: 'user_type', dictLabel: '企业管理员', dictValue: '2', dictSort: 2, status: 1 },
  { dictType: 'user_type', dictLabel: '企业员工', dictValue: '3', dictSort: 3, status: 1 },
  { dictType: 'user_type', dictLabel: '普通用户', dictValue: '4', dictSort: 4, status: 1 }
])

const menuData = ref([
  { id: 1, menuName: '系统管理', menuCode: 'system', icon: 'Setting', sortOrder: 1, status: 1, children: [
    { id: 11, menuName: '用户管理', menuCode: 'user', icon: 'User', sortOrder: 1, status: 1 },
    { id: 12, menuName: '角色管理', menuCode: 'role', icon: 'UserFilled', sortOrder: 2, status: 1 }
  ]},
  { id: 2, menuName: '企业管理', menuCode: 'enterprise', icon: 'OfficeBuilding', sortOrder: 2, status: 1 },
  { id: 3, menuName: '政策管理', menuCode: 'policy', icon: 'Document', sortOrder: 3, status: 1 }
])

const logData = ref([
  { moduleName: '企业管理', businessType: '新增', operName: 'admin', operUrl: '/api/enterprise/add', operIp: '192.168.1.100', status: 1, operTime: '2024-02-15 10:30:25' },
  { moduleName: '企业管理', businessType: '修改', operName: 'admin', operUrl: '/api/enterprise/edit', operIp: '192.168.1.100', status: 1, operTime: '2024-02-15 10:35:12' },
  { moduleName: '政策管理', businessType: '删除', operName: 'admin', operUrl: '/api/policy/delete', operIp: '192.168.1.100', status: 0, operTime: '2024-02-15 11:20:08' }
])
</script>

<style scoped lang="scss">
.system-page { .toolbar { margin-bottom: 20px; } }
</style>
