package cn.haizhi.handler;

import cn.haizhi.bean.DataDTO;
import cn.haizhi.bean.GroupData;
import cn.haizhi.bean.User;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.Dataform;
import cn.haizhi.form.UserAddForm;
import cn.haizhi.form.UserLoginForm;
import cn.haizhi.service.DataService;
import cn.haizhi.service.UserService;
import cn.haizhi.util.Const;
import cn.haizhi.util.FormErrorUtil;
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

    @Autowired
    private DataService dataService;


    @PostMapping("/register")
    public ResultView register(@Valid @RequestBody UserAddForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }

        userService.registerUser(form);
        return ResultUtil.returnSuccess();
    }

    @PostMapping("/login")
    public ResultView login(@Valid @RequestBody UserLoginForm form, BindingResult bindingResult, HttpSession session, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        User user = userService.login(form, session, response);
        return ResultUtil.returnSuccess(user);
    }

    @PostMapping("/data")
    public ResultView receiveData(@Valid @RequestBody Dataform form, BindingResult bindingResult, HttpServletRequest request, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        dataService.receiveData(form, request, session);
        return ResultUtil.returnSuccess();
    }

    @GetMapping("/average")
    public ResultView getAverage(HttpSession session){
        GroupData groupData = dataService.getAverageData(session);
        return ResultUtil.returnSuccess(groupData);
    }


    @PostMapping("/hdfs")
    public void test(HttpServletRequest request){
        try {
            dataService.writeFromLocalToHdfs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/logout")
    public ResultView logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ResultUtil.returnSuccess();
    }

    @GetMapping("/history")
    public ResultView history(HttpSession session){
        return ResultUtil.returnSuccess(dataService.getHistoryData(session));
    }
}
