package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("SELECT * FROM category ORDER BY `position` ASC")
    public List<Category> findAll();
}
