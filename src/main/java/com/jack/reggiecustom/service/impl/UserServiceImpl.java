package com.jack.reggiecustom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jack.reggiecustom.common.BaseResponse;
import com.jack.reggiecustom.common.ErrorCode;
import com.jack.reggiecustom.common.ResultUtils;
import com.jack.reggiecustom.mapper.UserMapper;
import com.jack.reggiecustom.model.domain.User;
import com.jack.reggiecustom.service.UserService;
import com.jack.reggiecustom.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-10-05 16:15:18
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Override
    public BaseResponse sendMsg(User user, HttpServletRequest request) {
        String phone = user.getPhone();

        if (StringUtils.isBlank(phone)){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        String validCode = ValidateCodeUtils.generateValidateCode(4).toString(); //生成四位验证码
        request.getSession().setAttribute("code", validCode); //将验证码存到session中以便后续校验

        log.info("验证码："+validCode); //这里应该是发送验证码逻辑


        return ResultUtils.success(validCode);
    }

    @Override
    public BaseResponse login(Map<String, String> map, HttpServletRequest request) {
        String phone = map.get("phone");
        String code = map.get("code");

        if (!StringUtils.isNoneBlank(phone, code)){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        String codeInSession = (String) request.getSession().getAttribute("code");
        if (!codeInSession.equals(code)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "验证码错误！");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = this.getOne(queryWrapper);
        if (user == null){
            //未注册
            user = new User();
            user.setPhone(phone);
            this.save(user);
        }
        request.getSession().setAttribute("user", user);
        return ResultUtils.success("登录成功！");
    }
}




