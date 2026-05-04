package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Category;
import com.xuejiaao.foodapp.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有的商品分类
     * @return
     */
    public List<Category> findAll(){
        return categoryMapper.findAll();
    }
}
