package cn.haizhi.handler;

import cn.haizhi.bean.Device;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.Dataform;
import cn.haizhi.form.DeviceForm;
import cn.haizhi.form.DeviceIdForm;
import cn.haizhi.mapper.DeviceMapper;
import cn.haizhi.service.DataService;
import cn.haizhi.service.DeviceService;
import cn.haizhi.util.FormErrorUtil;
import cn.haizhi.util.ResultUtil;
import cn.haizhi.view.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class DeviceHandler {

    @Autowired
    private DeviceService service;


    @Autowired
    private DataService dataService;

    //注册设备
    @PostMapping("/device/register")
    public ResultView register(@RequestBody @Valid DeviceForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }

        service.register(new Device(form.getDeviceId(), form.getUserId()));
        return ResultUtil.returnSuccess();
    }

    //注销设备
    @PostMapping("/device/cancel")
    public ResultView cancel(@RequestBody @Valid DeviceIdForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }

        service.cancel(form.getDeviceId());
        return ResultUtil.returnSuccess();
    }

    @PostMapping("/data")
    public ResultView receiveData(@Valid @RequestBody Dataform form, BindingResult bindingResult, HttpServletRequest request, HttpSession session){
        if(bindingResult.hasErrors()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR, FormErrorUtil.getFormErrors(bindingResult));
        }
        dataService.receiveData(form, request, session);
        return ResultUtil.returnSuccess();
    }

}
