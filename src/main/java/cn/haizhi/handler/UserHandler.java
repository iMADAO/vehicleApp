package cn.haizhi.handler;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import cn.haizhi.bean.DataDTO;
import cn.haizhi.bean.GroupData;
import cn.haizhi.bean.User;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.*;
import cn.haizhi.service.DataService;
import cn.haizhi.service.UserService;
import cn.haizhi.util.Const;
import cn.haizhi.util.FormErrorUtil;
import cn.haizhi.util.MessageUtil;
import cn.haizhi.util.ResultUtil;
import cn.haizhi.view.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserHandler {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResultView register(@Valid @RequestBody UserAddForm form, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }

        userService.registerUser(form, session);
        return ResultUtil.returnSuccess();
    }

    @PostMapping("/login/1")
    public ResultView loginWithPassword(@Valid @RequestBody UserLoginForm form, BindingResult bindingResult, HttpSession session, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        User user = userService.loginByUsernameAndPassword(form, session, response);
        return ResultUtil.returnSuccess(user);
    }

    @PostMapping("/login/2")
    public ResultView loginWithCode(@Valid @RequestBody UserLoginForm2 form, BindingResult bindingResult, HttpSession session, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        User user = userService.LoginByPhoneAndCode(form, session, response);
        return ResultUtil.returnSuccess(user);
    }

    @GetMapping("/username/check")
    public ResultView checkIfUsernameExist(@Valid @RequestBody UsernameForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        if (userService.checkIfUsernameExist(form.getUsername())){
            return ResultUtil.returnSuccess(1);
        }else{
            return ResultUtil.returnSuccess(0);
        }
    }

    @GetMapping("/phone/check")
    public ResultView checkIfPhoneExist(@Valid @RequestBody PhoneForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        if (userService.checkIfPhoneHadExist(form.getPhone())){
            return ResultUtil.returnSuccess(1);
        }else{
            return ResultUtil.returnSuccess(0);
        }
    }

    @PostMapping("/validateCode")
    public ResultView getValidateCode(@Valid @RequestBody PhoneForm form, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        MessageUtil.SendMessage(form.getPhone(), session);
        return ResultUtil.returnSuccess();
    }

    @PutMapping("/reserve")
    public ResultView addReservePhone(@Valid @RequestBody PhoneForm form, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }

        userService.addReservePhone(form.getPhone(), session);
        return ResultUtil.returnSuccess();
    }


    @GetMapping("/logout")
    public ResultView logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ResultUtil.returnSuccess();
    }
}
