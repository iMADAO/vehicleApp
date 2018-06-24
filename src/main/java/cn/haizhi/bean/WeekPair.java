package cn.haizhi.bean;

import cn.haizhi.util.Const;
import cn.haizhi.util.DateFormatUtil;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class WeekPair {
    private LocalDate firstDate;
    private LocalDate lastDate;

    public WeekPair(){
        List<LocalDate> localDateList = DateFormatUtil.getWeeklyDateStr();
        firstDate = localDateList.get(0);
        lastDate = localDateList.get(1);
    }

    public  List<String> getWeekPairStr(int n){
        List<String> list = new ArrayList<>();
        list.add(firstDate.plusWeeks(n).format(Const.dateTimeFormatter));
        list.add(lastDate.plusWeeks(n).format(Const.dateTimeFormatter));
        return list;
    }
}