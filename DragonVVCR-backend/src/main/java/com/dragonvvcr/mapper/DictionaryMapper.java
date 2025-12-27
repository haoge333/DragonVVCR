package com.dragonvvcr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典 Mapper 接口
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

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
    IPage<Dictionary> selectDictionaryPage(Page<Dictionary> page,
                                          @Param("dictCode") String dictCode,
                                          @Param("dictName") String dictName,
                                          @Param("dictType") String dictType,
                                          @Param("status") String status);

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<Dictionary> selectDictionaryByType(@Param("dictType") String dictType);

    /**
     * 根据父级ID查询子字典列表
     *
     * @param parentId 父级ID
     * @return 字典列表
     */
    List<Dictionary> selectDictionaryByParentId(@Param("parentId") Long parentId);
}
