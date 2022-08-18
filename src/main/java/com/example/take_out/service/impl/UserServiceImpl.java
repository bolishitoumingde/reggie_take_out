package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.entity.User;
import com.example.take_out.service.IUserService;
import com.example.take_out.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-08-18 10:34:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

}




