
import api from './api';

export default {
  // 获取字典分页列表
  getDictionaryPage(params) {
    return api.get('/dictionary/page', { params });
  },

  // 根据字典类型查询字典列表
  getDictionaryByType(dictType) {
    return api.get(`/dictionary/type/${dictType}`);
  },

  // 根据父级ID查询子字典列表
  getDictionaryByParentId(parentId) {
    return api.get(`/dictionary/parent/${parentId}`);
  },

  // 根据ID获取字典详情
  getDictionaryById(id) {
    return api.get(`/dictionary/${id}`);
  },

  // 新增字典
  createDictionary(data) {
    return api.post('/dictionary', data);
  },

  // 修改字典
  updateDictionary(data) {
    return api.put('/dictionary', data);
  },

  // 删除字典
  deleteDictionary(id) {
    return api.delete(`/dictionary/${id}`);
  },

  // 批量删除字典
  batchDeleteDictionary(ids) {
    return api.delete('/dictionary/batch', { data: ids });
  }
};
