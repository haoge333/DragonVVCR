
<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h4 class="mb-0">字典类型管理</h4>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#dictionaryTypeModal" @click="openAddModal">
              <i class="bi bi-plus-circle me-1"></i>新增字典类型
            </button>
          </div>
          <div class="card-body">
            <!-- 搜索区域 -->
            <div class="row mb-3">
              <div class="col-md-3">
                <input type="text" class="form-control" placeholder="字典类型编码" v-model="searchParams.typeCode">
              </div>
              <div class="col-md-3">
                <input type="text" class="form-control" placeholder="字典类型名称" v-model="searchParams.typeName">
              </div>
              <div class="col-md-3">
                <select class="form-select" v-model="searchParams.status">
                  <option value="">全部状态</option>
                  <option value="0">正常</option>
                  <option value="1">停用</option>
                </select>
              </div>
              <div class="col-md-3">
                <button class="btn btn-primary me-2" @click="searchDictionaryTypes">
                  <i class="bi bi-search me-1"></i>搜索
                </button>
                <button class="btn btn-secondary" @click="resetSearchParams">
                  <i class="bi bi-arrow-clockwise me-1"></i>重置
                </button>
              </div>
            </div>

            <!-- 表格区域 -->
            <div class="table-responsive">
              <table class="table table-hover">
                <thead class="table-light">
                  <tr>
                    <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll"></th>
                    <th>字典类型编码</th>
                    <th>字典类型名称</th>
                    <th>状态</th>
                    <th>备注</th>
                    <th>创建时间</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in dictionaryTypes" :key="item.id">
                    <td><input type="checkbox" v-model="selectedIds" :value="item.id"></td>
                    <td>{{ item.typeCode }}</td>
                    <td>{{ item.typeName }}</td>
                    <td>
                      <span :class="item.status === '0' ? 'badge bg-success' : 'badge bg-danger'">
                        {{ item.status === '0' ? '正常' : '停用' }}
                      </span>
                    </td>
                    <td>{{ item.remark }}</td>
                    <td>{{ formatDate(item.createTime) }}</td>
                    <td>
                      <button class="btn btn-sm btn-outline-primary me-1" @click="openEditModal(item)">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-sm btn-outline-danger" @click="confirmDelete(item.id)">
                        <i class="bi bi-trash"></i>
                      </button>
                    </td>
                  </tr>
                  <tr v-if="dictionaryTypes.length === 0">
                    <td colspan="7" class="text-center">暂无数据</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- 分页区域 -->
            <div class="d-flex justify-content-between align-items-center mt-3">
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

    <!-- 字典类型表单模态框 -->
    <div v-if="showModal" class="modal fade show d-block" tabindex="-1" style="background-color: rgba(0,0,0,0.5);">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEdit ? '编辑字典类型' : '新增字典类型' }}</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitForm">
              <div class="mb-3">
                <label for="typeCode" class="form-label">字典类型编码</label>
                <input type="text" class="form-control" id="typeCode" v-model="formData.typeCode" required>
              </div>
              <div class="mb-3">
                <label for="typeName" class="form-label">字典类型名称</label>
                <input type="text" class="form-control" id="typeName" v-model="formData.typeName" required>
              </div>
              <div class="mb-3">
                <label for="status" class="form-label">状态</label>
                <select class="form-select" id="status" v-model="formData.status">
                  <option value="0">正常</option>
                  <option value="1">停用</option>
                </select>
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
            <p>确定要删除该字典类型吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="deleteDictionaryType">确认删除</button>
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
            <p>确定要删除选中的 {{ selectedIds.length }} 个字典类型吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="batchDeleteDictionaryTypes">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import dictionaryTypeService from '../services/dictionaryTypeService';
import * as bootstrap from 'bootstrap';

export default {
  name: 'DictionaryTypeManagement',
  setup() {
    // 数据列表
    const dictionaryTypes = ref([]);

    // 分页信息
    const pagination = reactive({
      current: 1,
      size: 10,
      total: 0,
      pages: 0
    });

    // 搜索参数
    const searchParams = reactive({
      typeCode: '',
      typeName: '',
      status: ''
    });

    // 表单数据
    const formData = reactive({
      id: null,
      typeCode: '',
      typeName: '',
      status: '0',
      remark: ''
    });

    // 选择相关
    const selectedIds = ref([]);
    const selectAll = ref(false);

    // 模态框相关
    const dictionaryTypeModal = ref(null);
    const deleteConfirmModal = ref(null);
    const batchDeleteConfirmModal = ref(null);
    const isEdit = ref(false);
    const deleteId = ref(null);
    const showModal = ref(false);

    // 获取字典类型列表
    const getDictionaryTypes = async () => {
      try {
        const params = {
          current: pagination.current,
          size: pagination.size,
          ...searchParams
        };

        const response = await dictionaryTypeService.getDictionaryTypePage(params);

        if (response.data && response.data.records) {
          dictionaryTypes.value = response.data.records;
          pagination.total = response.data.total;
          pagination.pages = Math.ceil(pagination.total / pagination.size);
        }
      } catch (error) {
        console.error('获取字典类型列表失败:', error);
        showToast('获取字典类型列表失败', 'danger');
      }
    };

    // 搜索
    const searchDictionaryTypes = () => {
      pagination.current = 1;
      getDictionaryTypes();
    };

    // 重置搜索参数
    const resetSearchParams = () => {
      searchParams.typeCode = '';
      searchParams.typeName = '';
      searchParams.status = '';
      pagination.current = 1;
      getDictionaryTypes();
    };

    // 分页
    const changePage = (page) => {
      if (page < 1 || page > pagination.pages) return;
      pagination.current = page;
      getDictionaryTypes();
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

    // 打开新增模态框
    const openAddModal = () => {
      isEdit.value = false;
      resetForm();
      showModal.value = true;
    };

    // 打开编辑模态框
    const openEditModal = (item) => {
      isEdit.value = true;
      Object.assign(formData, item);
      showModal.value = true;
    };

    // 重置表单
    const resetForm = () => {
      formData.id = null;
      formData.typeCode = '';
      formData.typeName = '';
      formData.status = '0';
      formData.remark = '';
    };

    // 关闭模态框
    const closeModal = () => {
      showModal.value = false;
    };

    // 提交表单
    const submitForm = async () => {
      try {
        // 创建新的表单数据对象，确保字典类型编码正确提交
        const submitData = {
          ...formData,
          typeCode: formData.typeCode // 确保使用字典类型编码
        };
        
        if (isEdit.value) {
          await dictionaryTypeService.updateDictionaryType(submitData);
          showToast('修改字典类型成功', 'success');
        } else {
          await dictionaryTypeService.createDictionaryType(submitData);
          showToast('新增字典类型成功', 'success');
        }

        showModal.value = false;
        getDictionaryTypes();
      } catch (error) {
        console.error('保存字典类型失败:', error);
        showToast('保存字典类型失败', 'danger');
      }
    };

    // 确认删除
    const confirmDelete = (id) => {
      deleteId.value = id;
      deleteConfirmModal.value = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
      deleteConfirmModal.value.show();
    };

    // 删除字典类型
    const deleteDictionaryType = async () => {
      try {
        await dictionaryTypeService.deleteDictionaryType(deleteId.value);
        showToast('删除字典类型成功', 'success');
        deleteConfirmModal.value.hide();
        getDictionaryTypes();
      } catch (error) {
        console.error('删除字典类型失败:', error);
        showToast('删除字典类型失败', 'danger');
      }
    };

    // 全选/取消全选
    const toggleSelectAll = () => {
      if (selectAll.value) {
        selectedIds.value = dictionaryTypes.value.map(item => item.id);
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

    // 批量删除字典类型
    const batchDeleteDictionaryTypes = async () => {
      try {
        await dictionaryTypeService.batchDeleteDictionaryType(selectedIds.value);
        showToast('批量删除字典类型成功', 'success');
        batchDeleteConfirmModal.value.hide();
        selectedIds.value = [];
        selectAll.value = false;
        getDictionaryTypes();
      } catch (error) {
        console.error('批量删除字典类型失败:', error);
        showToast('批量删除字典类型失败', 'danger');
      }
    };

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
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
    });

    return {
      dictionaryTypes,
      pagination,
      searchParams,
      formData,
      selectedIds,
      selectAll,
      isEdit,
      showModal,
      searchDictionaryTypes,
      resetSearchParams,
      changePage,
      getPageNumbers,
      openAddModal,
      openEditModal,
      closeModal,
      submitForm,
      confirmDelete,
      deleteDictionaryType,
      toggleSelectAll,
      confirmBatchDelete,
      batchDeleteDictionaryTypes,
      formatDate
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
</style>
