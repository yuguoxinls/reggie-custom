package com.jack.reggiecustom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2022-10-05 16:15:18
*/
public interface UserService extends IService<User> {
    public BaseResponse sendMsg(User user, HttpServletRequest request);

    BaseResponse login(Map<String, String> map, HttpServletRequest request);

}
