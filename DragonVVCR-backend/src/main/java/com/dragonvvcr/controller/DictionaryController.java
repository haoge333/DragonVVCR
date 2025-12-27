package com.dragonvvcr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.entity.Dictionary;
import com.dragonvvcr.service.DictionaryService;
import com.dragonvvcr.service.DictionaryTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典控制器
 */
@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private DictionaryTypeService dictionaryTypeService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/page")
    public IPage<Dictionary> page(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String dictCode,
                                   @RequestParam(required = false) String dictName,
                                   @RequestParam(required = false) String dictType,
                                   @RequestParam(required = false) String status) {
        Page<Dictionary> page = new Page<>(current, size);
        return dictionaryService.getDictionaryPage(page, dictCode, dictName, dictType, status);
    }

    /**
     * 根据字典类型查询字典列表
     */
    @GetMapping("/type/{dictType}")
    public List<Dictionary> listByType(@PathVariable String dictType) {
        return dictionaryService.getDictionaryByType(dictType);
    }

    /**
     * 根据父级ID查询子字典列表
     */
    @GetMapping("/parent/{parentId}")
    public List<Dictionary> listByParentId(@PathVariable Long parentId) {
        return dictionaryService.getDictionaryByParentId(parentId);
    }

    /**
     * 根据ID查询字典详情
     */
    @GetMapping("/{id}")
    public Dictionary getById(@PathVariable Long id) {
        return dictionaryService.getById(id);
    }

    /**
     * 新增字典
     */
    @PostMapping
    public boolean add(@RequestBody Dictionary dictionary) {
        return dictionaryService.insertDictionary(dictionary);
    }

    /**
     * 修改字典
     */
    @PutMapping
    public boolean update(@RequestBody Dictionary dictionary) {
        return dictionaryService.updateDictionary(dictionary);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return dictionaryService.deleteDictionaryById(id);
    }

    /**
     * 批量删除字典
     */
    @DeleteMapping("/batch")
    public boolean deleteBatch(@RequestBody List<Long> ids) {
        return dictionaryService.deleteDictionaryByIds(ids);
    }
}
