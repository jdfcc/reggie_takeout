package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Category;


public interface CategoryService extends IService<Category> {

    public R<String> saveCategory(Category category);

    public R<Page> selectPage(int page,int size);

    /**
     * 根据id删除分类，删除前需要判断
     * @param id
     * @return
     */
    public void remove(Long id);

}
