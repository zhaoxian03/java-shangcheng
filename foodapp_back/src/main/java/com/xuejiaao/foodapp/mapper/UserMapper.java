// mapper/UserMapper.java
package com.xuejiaao.foodapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuejiaao.foodapp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 注意：由于继承了BaseMapper，已经有基本的CRUD方法
    // 下面的自定义方法可能需要调整

    /**
     * 根据用户名查询用户（用于登录验证）
     * 这个方法可能不需要，直接用BaseMapper的selectOne方法
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User selectByPhone(@Param("phone") String phone);

    @Update("UPDATE users SET avatar_url = #{avatarUrl}, update_time = NOW() WHERE cid = #{userId}")
    int updateAvatar(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);
}