package com.example.take_out.mapper;

import com.example.take_out.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @description 针对表【user(用户信息)】的数据库操作Mapper
 * @createDate 2022-08-18 10:34:21
 * @Entity com.example.take_out.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




