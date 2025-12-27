package com.dragonvvcr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dragonvvcr.entity.Dictionary;

import java.util.List;

/**
 * 字典服务接口
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 分页查询字典列表
     *
     * @param page 分页参数
     * @param dictCode 字典编码
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @param status 状态
     * @return 分页结果
     */
    IPage<Dictionary> getDictionaryPage(Page<Dictionary> page, String dictCode, String dictName, String dictType, String status);

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<Dictionary> getDictionaryByType(String dictType);

    /**
     * 根据父级ID查询子字典列表
     *
     * @param parentId 父级ID
     * @return 字典列表
     */
    List<Dictionary> getDictionaryByParentId(Long parentId);

    /**
     * 新增字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean insertDictionary(Dictionary dictionary);

    /**
     * 修改字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean updateDictionary(Dictionary dictionary);

    /**
     * 删除字典
     *
     * @param id 字典ID
     * @return 是否成功
     */
    boolean deleteDictionaryById(Long id);

    /**
     * 批量删除字典
     *
     * @param ids 字典ID集合
     * @return 是否成功
     */
    boolean deleteDictionaryByIds(List<Long> ids);
}
