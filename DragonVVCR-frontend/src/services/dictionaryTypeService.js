
import api from './api';

export default {
  // 获取字典类型分页列表
  getDictionaryTypePage(params) {
    return api.get('/dictionary-type/page', { params });
  },

  // 获取所有字典类型列表
  getDictionaryTypeList() {
    return api.get('/dictionary-type/list');
  },

  // 根据ID获取字典类型详情
  getDictionaryTypeById(id) {
    return api.get(`/dictionary-type/${id}`);
  },

  // 新增字典类型
  createDictionaryType(data) {
    return api.post('/dictionary-type', data);
  },

  // 修改字典类型
  updateDictionaryType(data) {
    return api.put('/dictionary-type', data);
  },

  // 删除字典类型
  deleteDictionaryType(id) {
    return api.delete(`/dictionary-type/${id}`);
  },

  // 批量删除字典类型
  batchDeleteDictionaryType(ids) {
    return api.delete('/dictionary-type/batch', { data: ids });
  }
};
