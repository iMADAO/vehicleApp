package cn.haizhi.handler;

import cn.haizhi.bean.GroupData;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.Dataform;
import cn.haizhi.service.DataService;
import cn.haizhi.util.FormErrorUtil;
import cn.haizhi.util.ResultUtil;
import cn.haizhi.view.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class DataHandler {
    @Autowired
    private DataService dataService;

    @PostMapping("/hdfs")
    public void test(HttpServletRequest request){
        try {
            dataService.writeFromLocalToHdfs();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @GetMapping("/history/1")
    public ResultView historyByDay(HttpSession session){
        return ResultUtil.returnSuccess(dataService.getHistoryDataByDay(session));
    }

    @GetMapping("/history/2")
    public ResultView historyByWeek(HttpSession session){
        return ResultUtil.returnSuccess();
    }

    @GetMapping("/history/3")
    public ResultView historyByMonth(HttpSession session){
        return ResultUtil.returnSuccess();
    }
}
