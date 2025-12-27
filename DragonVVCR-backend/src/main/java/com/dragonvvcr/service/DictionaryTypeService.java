package com.dragonvvcr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dragonvvcr.entity.DictionaryType;

import java.util.List;

/**
 * 字典类型服务接口
 */
public interface DictionaryTypeService extends IService<DictionaryType> {

    /**
     * 分页查询字典类型列表
     *
     * @param page 分页参数
     * @param typeCode 字典类型编码
     * @param typeName 字典类型名称
     * @param status 状态
     * @return 分页结果
     */
    IPage<DictionaryType> getDictionaryTypePage(Page<DictionaryType> page, String typeCode, String typeName, String status);

    /**
     * 新增字典类型
     *
     * @param dictionaryType 字典类型信息
     * @return 是否成功
     */
    boolean insertDictionaryType(DictionaryType dictionaryType);

    /**
     * 修改字典类型
     *
     * @param dictionaryType 字典类型信息
     * @return 是否成功
     */
    boolean updateDictionaryType(DictionaryType dictionaryType);

    /**
     * 删除字典类型
     *
     * @param id 字典类型ID
     * @return 是否成功
     */
    boolean deleteDictionaryTypeById(Long id);

    /**
     * 批量删除字典类型
     *
     * @param ids 字典类型ID集合
     * @return 是否成功
     */
    boolean deleteDictionaryTypeByIds(List<Long> ids);
}
