package cn.haizhi.service;


import cn.haizhi.bean.User;
import cn.haizhi.bean.UserExample;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.UserAddForm;
import cn.haizhi.form.UserLoginForm;
import cn.haizhi.mapper.JedisClient;
import cn.haizhi.mapper.UserMapper;
import cn.haizhi.util.*;
import cn.haizhi.util.Const;
import cn.haizhi.util.MD5;
import org.apache.hadoop.fs.FileSystem;
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


    public void registerUser(UserAddForm form) {
        if(checkIfUsernameExist(form.getUsername())){
           throw new MadaoException(ErrorEnum.USERNAME_HAD_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(form, user);
        user.setUserId(KeyUtil.genUniquKey());
        user.setPassword(MD5.getMD5ofStr(user.getPassword()));

        System.out.println(user);
        userMapper.insert(user);
    }

    public User login(UserLoginForm form, HttpSession session, HttpServletResponse response){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(form.getUsername());
        form.setPassword(MD5.getMD5ofStr(form.getPassword()));
        criteria.andPasswordEqualTo(form.getPassword());
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size()==0)
            throw new MadaoException(ErrorEnum.LOGIN_FAIL);

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
}










