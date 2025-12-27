package com.dragonvvcr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonvvcr.entity.DictionaryType;
import com.dragonvvcr.service.DictionaryTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型控制器
 */
@RestController
@RequestMapping("/api/dictionary-type")
public class DictionaryTypeController {

    @Resource
    private DictionaryTypeService dictionaryTypeService;

    /**
     * 分页查询字典类型列表
     */
    @GetMapping("/page")
    public IPage<DictionaryType> page(@RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String typeCode,
                                      @RequestParam(required = false) String typeName,
                                      @RequestParam(required = false) String status) {
        Page<DictionaryType> page = new Page<>(current, size);
        return dictionaryTypeService.getDictionaryTypePage(page, typeCode, typeName, status);
    }

    /**
     * 查询所有字典类型列表
     */
    @GetMapping("/list")
    public List<DictionaryType> list() {
        return dictionaryTypeService.list();
    }

    /**
     * 根据ID查询字典类型详情
     */
    @GetMapping("/{id}")
    public DictionaryType getById(@PathVariable Long id) {
        return dictionaryTypeService.getById(id);
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    public boolean add(@RequestBody DictionaryType dictionaryType) {
        return dictionaryTypeService.insertDictionaryType(dictionaryType);
    }

    /**
     * 修改字典类型
     */
    @PutMapping
    public boolean update(@RequestBody DictionaryType dictionaryType) {
        return dictionaryTypeService.updateDictionaryType(dictionaryType);
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return dictionaryTypeService.deleteDictionaryTypeById(id);
    }

    /**
     * 批量删除字典类型
     */
    @DeleteMapping("/batch")
    public boolean deleteBatch(@RequestBody List<Long> ids) {
        return dictionaryTypeService.deleteDictionaryTypeByIds(ids);
    }
}
