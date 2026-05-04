package com.xuejiaao.foodapp.mapper;

import com.xuejiaao.foodapp.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {

    // 查询用户所有地址
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} ORDER BY is_default DESC, create_time DESC")
    List<Address> selectByUserId(@Param("userId") Long userId);

    // 根据ID查询地址
    @Select("SELECT * FROM user_address WHERE id = #{id} AND user_id = #{userId}")
    Address selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    // 新增地址
    @Insert("INSERT INTO user_address(user_id, name, phone, province, city, district, detail, is_default) " +
            "VALUES(#{userId}, #{name}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);

    // 更新地址
    @Update("UPDATE user_address SET name = #{name}, phone = #{phone}, province = #{province}, " +
            "city = #{city}, district = #{district}, detail = #{detail}, is_default = #{isDefault} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int update(Address address);

    // 删除地址
    @Delete("DELETE FROM user_address WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    // 取消用户所有默认地址
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int cancelAllDefault(@Param("userId") Long userId);

    // 设置默认地址
    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(@Param("id") Long id, @Param("userId") Long userId);

    // 获取用户默认地址
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} AND is_default = 1 LIMIT 1")
    Address selectDefaultAddress(@Param("userId") Long userId);
}