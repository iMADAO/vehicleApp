package cn.haizhi.handler;

import cn.haizhi.bean.BloodPressure;
import cn.haizhi.bean.DataResult;
import cn.haizhi.bean.DataWeekly;
import cn.haizhi.bean.GroupData;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.Dataform;
import cn.haizhi.form.DateForm;
import cn.haizhi.service.DataService;
import cn.haizhi.util.FormErrorUtil;
import cn.haizhi.util.KeyUtil;
import cn.haizhi.util.ResultUtil;
import cn.haizhi.view.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.Path;
import java.io.IOException;
import java.time.LocalDateTime;

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

    @GetMapping("/average")
    public ResultView getAverage(HttpSession session){
        GroupData groupData = dataService.getAverageData(session);
        return ResultUtil.returnSuccess(groupData);
    }


    @GetMapping("/history/1/{startDate}/{endDate}")
    public ResultView historyByDay(HttpSession session, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate){
        return ResultUtil.returnSuccess(dataService.getHistoryDataByDay(startDate, endDate, session));
    }

    @GetMapping("/history/2/{startMonth}/{endMonth}")
    public ResultView historyByWeek(@PathVariable("startMonth") String startMonth, @PathVariable("endMonth") String endMonth, HttpSession session){
        return ResultUtil.returnSuccess(dataService.getHistoryDataByMonth(startMonth, endMonth, session));
    }

    @GetMapping("/history/3/{weekNum}")
    public ResultView historyByMonth(@PathVariable("weekNum") int weekNum,  HttpSession session){
        DataWeekly dataWeekly = dataService.getWeeklyData(weekNum, session);
        return ResultUtil.returnSuccess(dataWeekly);
    }

    @GetMapping("/getData")
    public ResultView getData(HttpSession session){
        DataResult dataResult = dataService.getData(session);
        return ResultUtil.returnSuccess(dataResult);
    }



//    @PostMapping("/test")
//    public void save(byte gender, byte startAge, byte endAge, double pressure){
//        BloodPressure bloodPressure = new BloodPressure();
//        bloodPressure.setGender(gender);
//        bloodPressure.setStartAge(startAge);
//        bloodPressure.setEndAge(endAge);
//        bloodPressure.setPressure(pressure);
//        bloodPressure.setId(KeyUtil.genUniquKey());
//
//
//
//    }
}
