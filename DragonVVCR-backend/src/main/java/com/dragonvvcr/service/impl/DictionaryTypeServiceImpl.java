package com.dragonvvcr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragonvvcr.entity.Dictionary;
import com.dragonvvcr.entity.DictionaryType;
import com.dragonvvcr.mapper.DictionaryMapper;
import com.dragonvvcr.mapper.DictionaryTypeMapper;
import com.dragonvvcr.service.DictionaryTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型服务实现类
 */
@Service
public class DictionaryTypeServiceImpl extends ServiceImpl<DictionaryTypeMapper, DictionaryType> implements DictionaryTypeService {

    @Resource
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Override
    public IPage<DictionaryType> getDictionaryTypePage(Page<DictionaryType> page, String typeCode, String typeName, String status) {
        return dictionaryTypeMapper.selectDictionaryTypePage(page, typeCode, typeName, status);
    }

    @Override
    public boolean insertDictionaryType(DictionaryType dictionaryType) {
        // 检查字典类型编码是否已存在
        LambdaQueryWrapper<DictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryType::getTypeCode, dictionaryType.getTypeCode());
        if (count(queryWrapper) > 0) {
            throw new RuntimeException("字典类型编码已存在");
        }
        return save(dictionaryType);
    }

    @Override
    public boolean updateDictionaryType(DictionaryType dictionaryType) {
        // 检查字典类型编码是否已存在（排除自身）
        LambdaQueryWrapper<DictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryType::getTypeCode, dictionaryType.getTypeCode())
                .ne(DictionaryType::getId, dictionaryType.getId());
        if (count(queryWrapper) > 0) {
            throw new RuntimeException("字典类型编码已存在");
        }
        return updateById(dictionaryType);
    }

    @Override
    public boolean deleteDictionaryTypeById(Long id) {
        // 检查是否有使用该类型的字典
        LambdaQueryWrapper<Dictionary> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dictionary::getDictType, getById(id).getTypeCode());
        if (dictionaryMapper.selectCount(dictQueryWrapper) > 0) {
            throw new RuntimeException("该字典类型下存在字典数据，不能删除");
        }
        return removeById(id);
    }

    @Override
    public boolean deleteDictionaryTypeByIds(List<Long> ids) {
        // 检查是否有使用该类型的字典
        for (Long id : ids) {
            DictionaryType dictionaryType = getById(id);
            LambdaQueryWrapper<Dictionary> dictQueryWrapper = new LambdaQueryWrapper<>();
            dictQueryWrapper.eq(Dictionary::getDictType, dictionaryType.getTypeCode());
            if (dictionaryMapper.selectCount(dictQueryWrapper) > 0) {
                throw new RuntimeException("字典类型" + dictionaryType.getTypeName() + "下存在字典数据，不能删除");
            }
        }
        return removeByIds(ids);
    }
}
