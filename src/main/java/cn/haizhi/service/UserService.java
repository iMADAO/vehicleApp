package cn.haizhi.service;


import cn.haizhi.bean.User;
import cn.haizhi.bean.UserExample;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.UserAddForm;
import cn.haizhi.form.UserLoginForm;
import cn.haizhi.form.UserLoginForm2;
import cn.haizhi.mapper.UserMapper;
import cn.haizhi.util.*;
import cn.haizhi.util.Const;
import cn.haizhi.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean checkIfUsernameExist(String name){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size()==0)
            return false;
        return true;
    }

    public boolean checkIfPhoneHadExist(String phone){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size()==0)
            return false;
        return true;
    }


    public void registerUser(UserAddForm form, HttpSession session) {

        String code = (String) session.getAttribute(Const.VALIDATECODE);

        if(code==null || code=="" || !code.equals(form.getValidateCode())){
            throw new MadaoException(ErrorEnum.VALICADE_CODE_ERROR);
        }

        if (checkIfPhoneHadExist(form.getPhone())){
            throw new MadaoException(ErrorEnum.PHONE_HAD_REGISTER);
        }

        if(checkIfUsernameExist(form.getUsername())){
            throw new MadaoException(ErrorEnum.USERNAME_HAD_EXIST);
        }

        //TOdo 对用户名以及密码格式和位数的检查

        User user = new User();
        BeanUtils.copyProperties(form, user);
        user.setUserId(KeyUtil.genUniquKey());
        user.setPassword(MD5.getMD5ofStr(user.getPassword()));

        System.out.println(user);
        userMapper.insert(user);
    }

    public User loginByUsernameAndPassword(UserLoginForm form, HttpSession session, HttpServletResponse response){
        form.setPassword(MD5.getMD5ofStr(form.getPassword()));
        //先根据用户名来验证，如果不通过就根据电话号码来验证
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(form.getUsername());
        criteria.andPasswordEqualTo(form.getPassword());
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size()==0) {
            example = new UserExample();
            criteria = example.createCriteria();
            criteria.andPhoneEqualTo(form.getUsername());
            criteria.andPasswordEqualTo(form.getPassword());
            userList = userMapper.selectByExample(example);
            if (userList.size()==0)
                throw new MadaoException(ErrorEnum.LOGIN_FAIL);
        }

        User user = userList.get(0);
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        response.addCookie(cookie);
        cookie.setMaxAge(Const.SSO_SESSION_EXPIRE);
        //8、将cookie存进response
        response.addCookie(cookie);

        user.setPassword(null);
        session.setAttribute(Const.CURRENT_USER, user);
        //9、返回user给安卓端
        return user;
    }

    public User LoginByPhoneAndCode(UserLoginForm2 form,  HttpSession session, HttpServletResponse response){
        String validateCode = (String) session.getAttribute(Const.VALIDATECODE);
        String phone = (String) session.getAttribute(Const.USER_PHONE);

        //验证手机号码和验证码是否一致
        if (!(phone.equals(form.getPhone()) && validateCode.equals(form.getCode()))){
            throw new MadaoException(ErrorEnum.VALICADE_CODE_ERROR);
        }

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(form.getPhone());
        User user = (User) userMapper.selectByExample(example);
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        response.addCookie(cookie);
        cookie.setMaxAge(Const.SSO_SESSION_EXPIRE);
        //8、将cookie存进response
        response.addCookie(cookie);

        user.setPassword(null);
        session.setAttribute(Const.CURRENT_USER, user);
        //9、返回user给安卓端
        return user;

    }

    public void addReservePhone(String phone, HttpSession session) {
        User user = (User) session.getAttribute(Const.USER_INFO);
        user.setReservePhone(phone);
        userMapper.updateByPrimaryKeySelective(user);
    }
}










