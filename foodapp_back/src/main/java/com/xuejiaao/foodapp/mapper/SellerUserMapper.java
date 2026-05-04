package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.SellerUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家用户表Mapper（MyBatis-Plus提供基础CRUD）
 */
@Mapper
public interface SellerUserMapper extends BaseMapper<SellerUser> {

}