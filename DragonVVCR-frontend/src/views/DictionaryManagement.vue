
<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h4 class="mb-0">字典管理</h4>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#dictionaryModal" @click="openAddModal">
              <i class="bi bi-plus-circle me-1"></i>新增字典
            </button>
          </div>
          <div class="card-body">
            <!-- 搜索区域 -->
            <div class="row mb-3">
              <div class="col-md-2">
                <input type="text" class="form-control" placeholder="字典编码" v-model="searchParams.dictCode">
              </div>
              <div class="col-md-2">
                <input type="text" class="form-control" placeholder="字典名称" v-model="searchParams.dictName">
              </div>
              <div class="col-md-2">
                <select class="form-select" v-model="searchParams.dictType">
                  <option value="">选择字典类型</option>
                  <option v-for="type in dictionaryTypes" :key="type.typeCode" :value="type.typeCode">
                    {{ type.typeName }}
                  </option>
                </select>
              </div>
              <div class="col-md-2">
                <select class="form-select" v-model="searchParams.status">
                  <option value="">全部状态</option>
                  <option value="0">正常</option>
                  <option value="1">停用</option>
                </select>
              </div>
              <div class="col-md-4">
                <button class="btn btn-primary me-2" @click="searchDictionaries">
                  <i class="bi bi-search me-1"></i>搜索
                </button>
                <button class="btn btn-secondary me-2" @click="resetSearchParams">
                  <i class="bi bi-arrow-clockwise me-1"></i>重置
                </button>
                <button class="btn btn-info me-2" @click="toggleTreeView">
                  <i :class="isTreeView ? 'bi bi-list-ul' : 'bi bi-diagram-3'" class="me-1"></i>
                  {{ isTreeView ? '列表视图' : '树形视图' }}
                </button>
              </div>
            </div>

            <!-- 树形视图 -->
            <div v-if="isTreeView" class="tree-view">
              <div class="tree-node" v-for="node in treeData" :key="node.id">
                <div class="node-content" :class="{ 'selected': selectedNode && selectedNode.id === node.id }" @click="selectNode(node)">
                  <span class="node-icon">
                    <i v-if="node.children && node.children.length > 0" :class="node.expanded ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
                    <i v-else class="bi bi-file-text"></i>
                  </span>
                  <span class="node-label">{{ node.dictName }}</span>
                  <span class="node-value">({{ node.dictValue }})</span>
                  <span class="node-actions ms-auto">
                    <button class="btn btn-sm btn-outline-primary me-1" @click.stop="openEditModal(node)">
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" @click.stop="confirmDelete(node.id)">
                      <i class="bi bi-trash"></i>
                    </button>
                  </span>
                </div>
                <div v-if="node.children && node.children.length > 0 && node.expanded" class="node-children">
                  <div class="tree-node" v-for="child in node.children" :key="child.id">
                    <div class="node-content" :class="{ 'selected': selectedNode && selectedNode.id === child.id }" @click="selectNode(child)">
                      <span class="node-icon">
                        <i v-if="child.children && child.children.length > 0" :class="child.expanded ? 'bi bi-chevron-down' : 'bi bi-chevron-right'"></i>
                        <i v-else class="bi bi-file-text"></i>
                      </span>
                      <span class="node-label">{{ child.dictName }}</span>
                      <span class="node-value">({{ child.dictValue }})</span>
                      <span class="node-actions ms-auto">
                        <button class="btn btn-sm btn-outline-primary me-1" @click.stop="openEditModal(child)">
                          <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" @click.stop="confirmDelete(child.id)">
                          <i class="bi bi-trash"></i>
                        </button>
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 表格视图 -->
            <div v-else class="table-responsive">
              <table class="table table-hover">
                <thead class="table-light">
                  <tr>
                    <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll"></th>
                    <th>字典编码</th>
                    <th>字典名称</th>
                    <th>字典类型</th>
                    <th>字典层级</th>
                    <th>父级ID</th>
                    <th>字典值</th>
                    <th>排序</th>
                    <th>状态</th>
                    <th>备注</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in dictionaries" :key="item.id">
                    <td><input type="checkbox" v-model="selectedIds" :value="item.id"></td>
                    <td>{{ item.dictCode }}</td>
                    <td>{{ item.dictName }}</td>
                    <td>{{ item.dictType }}</td>
                    <td>{{ item.dictLevel }}</td>
                    <td>{{ item.parentId || '-' }}</td>
                    <td>{{ item.dictValue }}</td>
                    <td>{{ item.sortOrder }}</td>
                    <td>
                      <span :class="item.status === '0' ? 'badge bg-success' : 'badge bg-danger'">
                        {{ item.status === '0' ? '正常' : '停用' }}
                      </span>
                    </td>
                    <td>{{ item.remark }}</td>
                    <td>
                      <button class="btn btn-sm btn-outline-primary me-1" @click="openEditModal(item)">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-sm btn-outline-danger" @click="confirmDelete(item.id)">
                        <i class="bi bi-trash"></i>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="dictionaries.length === 0">
                    <td colspan="11" class="text-center">暂无数据</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- 分页区域 -->
            <div v-if="!isTreeView" class="d-flex justify-content-between align-items-center mt-3">
              <div>
                <button class="btn btn-danger me-2" :disabled="selectedIds.length === 0" @click="confirmBatchDelete">
                  <i class="bi bi-trash me-1"></i>批量删除
                </button>
                <span class="text-muted">已选择 {{ selectedIds.length }} 项</span>
              </div>
              <nav>
                <ul class="pagination mb-0">
                  <li class="page-item" :class="{ disabled: pagination.current <= 1 }">
                    <a class="page-link" href="#" @click.prevent="changePage(pagination.current - 1)">上一页</a>
                  </li>
                  <li v-for="page in getPageNumbers()" :key="page" class="page-item" :class="{ active: page === pagination.current }">
                    <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                  </li>
                  <li class="page-item" :class="{ disabled: pagination.current >= pagination.pages }">
                    <a class="page-link" href="#" @click.prevent="changePage(pagination.current + 1)">下一页</a>
                  </li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 字典表单模态框 -->
    <div v-if="showModal" class="modal fade show d-block" tabindex="-1" style="background-color: rgba(0,0,0,0.5);">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEdit ? '编辑字典' : '新增字典' }}</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="dictCode" class="form-label">字典编码</label>
                  <input type="text" class="form-control" id="dictCode" v-model="formData.dictCode" required>
                </div>
                <div class="col-md-6 mb-3">
                  <label for="dictName" class="form-label">字典名称</label>
                  <input type="text" class="form-control" id="dictName" v-model="formData.dictName" required>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="dictType" class="form-label">字典类型</label>
                  <select class="form-select" id="dictType" v-model="formData.dictType" required>
                    <option value="">请选择字典类型</option>
                    <option v-for="type in dictionaryTypes" :key="type.typeCode" :value="type.typeCode">
                      {{ type.typeName }}
                    </option>
                  </select>
                </div>
                <div class="col-md-6 mb-3">
                  <label for="dictValue" class="form-label">字典值</label>
                  <input type="text" class="form-control" id="dictValue" v-model="formData.dictValue" required>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="dictLevel" class="form-label">字典层级</label>
                  <input type="number" class="form-control" id="dictLevel" v-model="formData.dictLevel" min="1">
                </div>
                <div class="col-md-6 mb-3">
                  <label for="parentId" class="form-label">父级ID</label>
                  <select class="form-select" id="parentId" v-model="formData.parentId">
                    <option value="">无父级</option>
                    <option v-for="dict in parentOptions" :key="dict.id" :value="dict.id">
                      {{ dict.dictName }}
                    </option>
                  </select>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="sortOrder" class="form-label">排序</label>
                  <input type="number" class="form-control" id="sortOrder" v-model="formData.sortOrder" min="0">
                </div>
                <div class="col-md-6 mb-3">
                  <label for="status" class="form-label">状态</label>
                  <select class="form-select" id="status" v-model="formData.status">
                    <option value="0">正常</option>
                    <option value="1">停用</option>
                  </select>
                </div>
              </div>
              <div class="mb-3">
                <label for="remark" class="form-label">备注</label>
                <textarea class="form-control" id="remark" rows="3" v-model="formData.remark"></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
            <button type="button" class="btn btn-primary" @click="submitForm">保存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 删除确认模态框 -->
    <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">确认删除</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>确定要删除该字典吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="deleteDictionary">确认删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 批量删除确认模态框 -->
    <div class="modal fade" id="batchDeleteConfirmModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">批量删除确认</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>确定要删除选中的 {{ selectedIds.length }} 个字典吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="batchDeleteDictionaries">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue';
import dictionaryService from '../services/dictionaryService';
import dictionaryTypeService from '../services/dictionaryTypeService';
import * as bootstrap from 'bootstrap';

export default {
  name: 'DictionaryManagement',
  setup() {
    // 数据列表
    const dictionaries = ref([]);
    const dictionaryTypes = ref([]);
    const parentOptions = ref([]);

    // 树形视图数据
    const treeData = ref([]);
    const isTreeView = ref(false);
    const selectedNode = ref(null);

    // 分页信息
    const pagination = reactive({
      current: 1,
      size: 10,
      total: 0,
      pages: 0
    });

    // 搜索参数
    const searchParams = reactive({
      dictCode: '',
      dictName: '',
      dictType: '',
      status: ''
    });

    // 表单数据
    const formData = reactive({
      id: null,
      dictCode: '',
      dictName: '',
      dictType: '',
      dictLevel: 1,
      parentId: '',
      dictValue: '',
      sortOrder: 0,
      status: '0',
      remark: ''
    });

    // 选择相关
    const selectedIds = ref([]);
    const selectAll = ref(false);

    // 模态框相关
    const dictionaryModal = ref(null);
    const deleteConfirmModal = ref(null);
    const batchDeleteConfirmModal = ref(null);
    const isEdit = ref(false);
    const deleteId = ref(null);
    const showModal = ref(false);

    // 获取字典类型列表
    const getDictionaryTypes = async () => {
      try {
        const response = await dictionaryTypeService.getDictionaryTypeList();
        if (response.data) {
          dictionaryTypes.value = response.data;
        }
      } catch (error) {
        console.error('获取字典类型列表失败:', error);
        showToast('获取字典类型列表失败', 'danger');
      }
    };

    // 获取字典列表
    const getDictionaries = async () => {
      try {
        const params = {
          current: pagination.current,
          size: pagination.size,
          ...searchParams
        };

        const response = await dictionaryService.getDictionaryPage(params);

        if (response.data && response.data.records) {
          dictionaries.value = response.data.records;
          pagination.total = response.data.total;
          pagination.pages = Math.ceil(pagination.total / pagination.size);
        }
      } catch (error) {
        console.error('获取字典列表失败:', error);
        showToast('获取字典列表失败', 'danger');
      }
    };

    // 获取树形数据
    const getTreeData = async () => {
      try {
        const params = { ...searchParams };
        // 获取所有数据不分页
        const response = await dictionaryService.getDictionaryPage({ ...params, current: 1, size: 1000 });

        if (response.data && response.data.records) {
          // 构建树形结构
          const allItems = response.data.records;
          const rootItems = allItems.filter(item => !item.parentId);

          // 递归构建子节点
          const buildTree = (items, parentId = null) => {
            return items
              .filter(item => item.parentId === parentId)
              .map(item => ({
                ...item,
                expanded: false,
                children: buildTree(items, item.id)
              }));
          };

          treeData.value = buildTree(rootItems);
        }
      } catch (error) {
        console.error('获取树形数据失败:', error);
        showToast('获取树形数据失败', 'danger');
      }
    };

    // 切换视图
    const toggleTreeView = () => {
      isTreeView.value = !isTreeView.value;
      if (isTreeView.value) {
        getTreeData();
      } else {
        getDictionaries();
      }
    };

    // 选择节点
    const selectNode = (node) => {
      selectedNode.value = node;
      // 如果有子节点，切换展开状态
      if (node.children && node.children.length > 0) {
        node.expanded = !node.expanded;
      }
    };

    // 搜索
    const searchDictionaries = () => {
      pagination.current = 1;
      if (isTreeView.value) {
        getTreeData();
      } else {
        getDictionaries();
      }
    };

    // 重置搜索参数
    const resetSearchParams = () => {
      searchParams.dictCode = '';
      searchParams.dictName = '';
      searchParams.dictType = '';
      searchParams.status = '';
      pagination.current = 1;
      if (isTreeView.value) {
        getTreeData();
      } else {
        getDictionaries();
      }
    };

    // 分页
    const changePage = (page) => {
      if (page < 1 || page > pagination.pages) return;
      pagination.current = page;
      getDictionaries();
    };

    // 获取页码数组
    const getPageNumbers = () => {
      const pages = [];
      const start = Math.max(1, pagination.current - 2);
      const end = Math.min(pagination.pages, pagination.current + 2);

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }

      return pages;
    };

    // 获取父级选项
    const getParentOptions = async () => {
      if (!formData.dictType) {
        parentOptions.value = [];
        return;
      }

      try {
        const response = await dictionaryService.getDictionaryByType(formData.dictType);
        if (response.data) {
          // 排除当前编辑项及其子项
          const filterOptions = (items, excludeId) => {
            return items.filter(item => {
              if (item.id === excludeId) return false;
              // 这里可以添加更复杂的过滤逻辑，排除子项
              return true;
            });
          };

          parentOptions.value = filterOptions(response.data, formData.id);
        }
      } catch (error) {
        console.error('获取父级选项失败:', error);
      }
    };

    // 监听字典类型变化
    watch(() => formData.dictType, () => {
      getParentOptions();
    });

    // 打开新增模态框
    const openAddModal = () => {
      isEdit.value = false;
      resetForm();
      showModal.value = true;
    };

    // 打开编辑模态框
    const openEditModal = async (item) => {
      isEdit.value = true;
      Object.assign(formData, item);
      await getParentOptions();
      showModal.value = true;
    };

    // 重置表单
    const resetForm = () => {
      formData.id = null;
      formData.dictCode = '';
      formData.dictName = '';
      formData.dictType = '';
      formData.dictLevel = 1;
      formData.parentId = '';
      formData.dictValue = '';
      formData.sortOrder = 0;
      formData.status = '0';
      formData.remark = '';
      parentOptions.value = [];
    };

    // 关闭模态框
    const closeModal = () => {
      showModal.value = false;
    };

    // 提交表单
    const submitForm = async () => {
      try {
        if (isEdit.value) {
          await dictionaryService.updateDictionary(formData);
          showToast('修改字典成功', 'success');
        } else {
          await dictionaryService.createDictionary(formData);
          showToast('新增字典成功', 'success');
        }

        showModal.value = false;
        if (isTreeView.value) {
          getTreeData();
        } else {
          getDictionaries();
        }
      } catch (error) {
        console.error('保存字典失败:', error);
        showToast('保存字典失败', 'danger');
      }
    };

    // 确认删除
    const confirmDelete = (id) => {
      deleteId.value = id;
      deleteConfirmModal.value = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
      deleteConfirmModal.value.show();
    };

    // 删除字典
    const deleteDictionary = async () => {
      try {
        await dictionaryService.deleteDictionary(deleteId.value);
        showToast('删除字典成功', 'success');
        deleteConfirmModal.value.hide();
        if (isTreeView.value) {
          getTreeData();
        } else {
          getDictionaries();
        }
      } catch (error) {
        console.error('删除字典失败:', error);
        showToast('删除字典失败', 'danger');
      }
    };

    // 全选/取消全选
    const toggleSelectAll = () => {
      if (selectAll.value) {
        selectedIds.value = dictionaries.value.map(item => item.id);
      } else {
        selectedIds.value = [];
      }
    };

    // 确认批量删除
    const confirmBatchDelete = () => {
      if (selectedIds.value.length === 0) return;
      batchDeleteConfirmModal.value = new bootstrap.Modal(document.getElementById('batchDeleteConfirmModal'));
      batchDeleteConfirmModal.value.show();
    };

    // 批量删除字典
    const batchDeleteDictionaries = async () => {
      try {
        await dictionaryService.batchDeleteDictionary(selectedIds.value);
        showToast('批量删除字典成功', 'success');
        batchDeleteConfirmModal.value.hide();
        selectedIds.value = [];
        selectAll.value = false;
        if (isTreeView.value) {
          getTreeData();
        } else {
          getDictionaries();
        }
      } catch (error) {
        console.error('批量删除字典失败:', error);
        showToast('批量删除字典失败', 'danger');
      }
    };

    // 显示提示消息
    const showToast = (message, type = 'info') => {
      // 创建toast元素
      const toastContainer = document.querySelector('.toast-container') || createToastContainer();
      const toastId = 'toast-' + Date.now();
      const toastHtml = `
        <div id="${toastId}" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="toast-header">
            <strong class="me-auto">系统提示</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div class="toast-body">${message}</div>
        </div>
      `;

      toastContainer.insertAdjacentHTML('beforeend', toastHtml);
      const toastElement = document.getElementById(toastId);
      const toast = new bootstrap.Toast(toastElement);

      // 设置样式类
      toastElement.className = `toast bg-${type === 'success' ? 'success' : type === 'danger' ? 'danger' : type === 'warning' ? 'warning' : 'info'} text-white`;

      // 显示toast
      toast.show();

      // 监听隐藏事件，移除元素
      toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
      });
    };

    // 创建toast容器
    const createToastContainer = () => {
      const container = document.createElement('div');
      container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
      container.style.zIndex = '11';
      document.body.appendChild(container);
      return container;
    };

    onMounted(() => {
      getDictionaryTypes();
      getDictionaries();
    });

    return {
      dictionaries,
      dictionaryTypes,
      parentOptions,
      treeData,
      isTreeView,
      selectedNode,
      pagination,
      searchParams,
      formData,
      selectedIds,
      selectAll,
      isEdit,
      showModal,
      searchDictionaries,
      resetSearchParams,
      changePage,
      getPageNumbers,
      toggleTreeView,
      selectNode,
      openAddModal,
      openEditModal,
      closeModal,
      submitForm,
      confirmDelete,
      deleteDictionary,
      toggleSelectAll,
      confirmBatchDelete,
      batchDeleteDictionaries
    };
  }
};
</script>

<style scoped>
.card {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
  border: 1px solid rgba(0, 0, 0, 0.125);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid rgba(0, 0, 0, 0.125);
}

.table th {
  border-top: none;
  font-weight: 600;
}

.btn {
  border-radius: 0.25rem;
}

.pagination {
  margin-bottom: 0;
}

.modal-content {
  border-radius: 0.3rem;
  border: none;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
}

.modal-header {
  border-bottom: 1px solid #dee2e6;
}

.modal-footer {
  border-top: 1px solid #dee2e6;
}

.form-control, .form-select {
  border-radius: 0.25rem;
}

.badge {
  font-size: 0.75em;
}

/* 树形视图样式 */
.tree-view {
  border: 1px solid #dee2e6;
  border-radius: 0.25rem;
  padding: 10px;
  max-height: 500px;
  overflow-y: auto;
}

.tree-node {
  margin-bottom: 5px;
}

.node-content {
  display: flex;
  align-items: center;
  padding: 8px 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.node-content:hover {
  background-color: #f8f9fa;
}

.node-content.selected {
  background-color: #e9ecef;
}

.node-icon {
  margin-right: 8px;
  width: 20px;
  text-align: center;
}

.node-label {
  font-weight: 500;
  margin-right: 8px;
}

.node-value {
  color: #6c757d;
  font-size: 0.9em;
}

.node-actions {
  opacity: 0;
  transition: opacity 0.2s;
}

.node-content:hover .node-actions {
  opacity: 1;
}

.node-children {
  margin-left: 20px;
  padding-left: 10px;
  border-left: 1px dashed #dee2e6;
}
</style>
