package com.example.userloginsystem.mapper;

import com.example.userloginsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);

    int insert(User user);

    User findById(@Param("id") Long id);

    User findByOpenid(@Param("openid") String openid);

    // ✅ 新增：微信用户再次登录时更新头像/昵称
    int updateWxProfileByOpenid(@Param("openid") String openid,
                                @Param("avatarUrl") String avatarUrl,
                                @Param("wxNickname") String wxNickname);
}