package com.dragonvvcr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.entity.DictionaryType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 字典类型 Mapper 接口
 */
@Mapper
public interface DictionaryTypeMapper extends BaseMapper<DictionaryType> {

    /**
     * 分页查询字典类型列表
     *
     * @param page 分页参数
     * @param typeCode 字典类型编码
     * @param typeName 字典类型名称
     * @param status 状态
     * @return 分页结果
     */
    IPage<DictionaryType> selectDictionaryTypePage(Page<DictionaryType> page,
                                                  @Param("typeCode") String typeCode,
                                                  @Param("typeName") String typeName,
                                                  @Param("status") String status);
}
