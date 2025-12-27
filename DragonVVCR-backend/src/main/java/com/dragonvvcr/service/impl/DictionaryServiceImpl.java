package com.dragonvvcr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragonvvcr.entity.Dictionary;
import com.dragonvvcr.mapper.DictionaryMapper;
import com.dragonvvcr.service.DictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典服务实现类
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Override
    public IPage<Dictionary> getDictionaryPage(Page<Dictionary> page, String dictCode, String dictName, String dictType, String status) {
        return dictionaryMapper.selectDictionaryPage(page, dictCode, dictName, dictType, status);
    }

    @Override
    public List<Dictionary> getDictionaryByType(String dictType) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getDictType, dictType)
                .eq(Dictionary::getStatus, "0")
                .orderByAsc(Dictionary::getSortOrder);
        return list(queryWrapper);
    }

    @Override
    public List<Dictionary> getDictionaryByParentId(Long parentId) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getParentId, parentId)
                .eq(Dictionary::getStatus, "0")
                .orderByAsc(Dictionary::getSortOrder);
        return list(queryWrapper);
    }

    @Override
    public boolean insertDictionary(Dictionary dictionary) {
        // 检查字典编码是否已存在
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getDictCode, dictionary.getDictCode());
        if (count(queryWrapper) > 0) {
            throw new RuntimeException("字典编码已存在");
        }
        return save(dictionary);
    }

    @Override
    public boolean updateDictionary(Dictionary dictionary) {
        // 检查字典编码是否已存在（排除自身）
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getDictCode, dictionary.getDictCode())
                .ne(Dictionary::getId, dictionary.getId());
        if (count(queryWrapper) > 0) {
            throw new RuntimeException("字典编码已存在");
        }
        return updateById(dictionary);
    }

    @Override
    public boolean deleteDictionaryById(Long id) {
        // 检查是否有子字典
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getParentId, id);
        if (count(queryWrapper) > 0) {
            throw new RuntimeException("存在子字典，不能删除");
        }
        return removeById(id);
    }

    @Override
    public boolean deleteDictionaryByIds(List<Long> ids) {
        // 检查是否有子字典
        for (Long id : ids) {
            LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dictionary::getParentId, id);
            if (count(queryWrapper) > 0) {
                throw new RuntimeException("ID为" + id + "的字典存在子字典，不能删除");
            }
        }
        return removeByIds(ids);
    }
}
