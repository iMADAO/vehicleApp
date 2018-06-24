package cn.haizhi.service;


import cn.haizhi.bean.Device;
import cn.haizhi.bean.User;
import cn.haizhi.bean.UserExample;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.UserAddForm;
import cn.haizhi.form.UserLoginForm;
import cn.haizhi.form.UserLoginForm2;
import cn.haizhi.mapper.DeviceMapper;
import cn.haizhi.mapper.JedisClient;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private DeviceMapper deviceMapper;

    public boolean checkIfUsernameExist(String name){
        if(name==null || name==""){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, IdResultMap.getIdMapWithName("username", name));
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size()==0)
            return false;
        return true;
    }

    public boolean checkIfPhoneHadExist(String phone){
        Pattern pattern = Pattern.compile("^((17[0-9])|(14St [0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if(!matcher.find()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, IdResultMap.getIdMapWithName("phone", phone));
        }
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
        userMapper.insertSelective(user);
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
        updateDeviceUser(form.getDeviceCode(), user.getUserId());

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

    private void updateDeviceUser(String deviceCode, String userId){
        Device device = deviceMapper.selectByPrimaryKey(deviceCode);
        System.out.println(deviceCode);
        if(device==null){
            throw new MadaoException(ErrorEnum.DEVICE_NOT_EXIST);
        }
        device.setUserId(userId);
        deviceMapper.updateByPrimaryKeySelective(device);
        try {
            jedisClient.expire(Const.DEVICE + "-" + deviceCode, 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public User LoginByPhoneAndCode(UserLoginForm2 form,  HttpSession session, HttpServletResponse response){
        String validateCode = (String) session.getAttribute(Const.VALIDATECODE);
        String phone = (String) session.getAttribute(Const.USER_PHONE);

        if(validateCode==null || phone==null){
            throw new MadaoException(ErrorEnum.VALICADE_CODE_ERROR);
        }

        //验证手机号码和验证码是否一致
        if (!(phone.equals(form.getPhone()) && validateCode.equals(form.getCode()))){
            throw new MadaoException(ErrorEnum.VALICADE_CODE_ERROR);
        }

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(form.getPhone());
        User user = (User) userMapper.selectByExample(example);
        if(user==null){
            throw new  MadaoException(ErrorEnum.USER_NOE_EXIST);
        }

        updateDeviceUser(form.getDeviceCode(), user.getUserId());

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
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        user.setReservePhone(phone);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void revisePassword(String password, String code, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String validateCode = (String) session.getAttribute(Const.VALIDATECODE);
        if(validateCode==null || !validateCode.equals(code)){
            throw new MadaoException(ErrorEnum.VALICADE_CODE_ERROR);
        }
        user.setPassword(MD5.getMD5ofStr(password));
        userMapper.updateByPrimaryKeySelective(user);

    }

    public void reviseUsername(String username, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
        }

        user.setUsername(username);
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        userMapper.updateByPrimaryKeySelective(user);
    }
}










