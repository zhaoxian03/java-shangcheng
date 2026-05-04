package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.Contact;
import org.apache.ibatis.annotations.Mapper;

/**
 * 联系人表Mapper（MyBatis-Plus提供基础CRUD）
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {
}