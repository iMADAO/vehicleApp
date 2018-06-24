import cn.haizhi.bean.BloodPressure;
import cn.haizhi.bean.GroupDataDTO;
import cn.haizhi.bean.WeekPair;
import cn.haizhi.enums.BloodPressureEnum;
import cn.haizhi.form.Dataform;
import cn.haizhi.util.AccessUtil;
import cn.haizhi.util.Const;
import cn.haizhi.util.DateFormatUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {
    @org.junit.Test
    public void test4(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
//        LocalDate.parse("199901", dateTimeFormatter);
        dateTimeFormatter.parse("199901");

        List<String> list = new ArrayList<>();
        String monthStr1 = "200811";
        String monthstr2 = "199902";
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(monthStr1 + "11", dateTimeFormatter1);
        LocalDate endDate = LocalDate.parse(monthstr2 + "01", dateTimeFormatter1);
        if (startDate.compareTo(endDate)>0){
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        while(startDate.compareTo(endDate)<=0){
            list.add(startDate.format(dateTimeFormatter));
            startDate = startDate.plusMonths(1);
        }
        list.forEach(System.out::println);

    }

    @Test
    public void test5(){
//        DateFormatUtil.getMonthStrInRange("201811", "201801").forEach(System.out::println);
        DateFormatUtil.getDateStrInRange("20180111", "19960101").forEach(System.out::println);
    }

    @Test
    public void test6(){
        Map<String, String> map = new HashMap<>();
        map.put("aa", "11");
        map.put("aa", "22");

        for(Map.Entry<String, String> e: map.entrySet()){
            System.out.println(e.getKey() + "------" + e.getValue());
        }
    }

    @Test
    public void test7(){
        Dataform dataform = new Dataform();
        dataform.setBloodFat(1.0);
//        dataform.setBloodPressure(1.0);
        dataform.setHeartbeat(1.0);
        dataform.setTemperature(1.0);
        dataform.setWeight(1.0);
        GroupDataDTO groupDataDTO = new GroupDataDTO();
        BeanUtils.copyProperties(dataform, groupDataDTO);
        System.out.println(groupDataDTO);
    }

    @Test
    public void get(){
//        List<String> list = new ArrayList<>();
//        LocalDate localDate = LocalDate.now();
//        LocalDate firstDate = null;
//        LocalDate lastDate = null;
//        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
//        int n = dayOfWeek.getValue();
//        WeekPair weekStrPair = new WeekPair();
//        System.out.println(n);
//        while(n>1){
//            n--;
//            localDate = localDate.minusDays(1);
//        }
//        weekStrPair.setFirstDateStr(localDate.format(Const.dateTimeFormatter));
//        firstDate = localDate;
//
//        n = dayOfWeek.getValue();
//        localDate = LocalDate.now();
//        while(n<7){
//            n++;
//            localDate = localDate.plusDays(1);
//        }
//
//        weekStrPair.setLastDateStr(localDate.format(Const.dateTimeFormatter));
//        lastDate = localDate;
//        System.out.println(localDate);
//        System.out.println(firstDate + "----" + lastDate);
//        System.out.println(firstDate.minusWeeks(1));
        List<LocalDate> localDateList = DateFormatUtil.getWeeklyDateStr();
        System.out.println(localDateList);
    }

    @Test
    public void test8(){
        WeekPair weekPair = new WeekPair();
        System.out.println(weekPair);
        System.out.println(weekPair.getWeekPairStr(0));
        System.out.println(weekPair.getWeekPairStr(1));
        System.out.println(weekPair.getWeekPairStr(-1));
    }

    @Test
    public void test9(){
        DateFormatUtil.getWeekDateStrList().forEach(System.out::println);
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    @Test
    public void test10(){
        double w = 0.1;
        String wReport = "";
        wReport = wReport +  "近期" + (w>0 ? "增加" + w : (w<0 ? "减少"  + w: "基本不变"));
        System.out.println(wReport);
//        int data = Integer.parseInt("\\u0xB0", 16);
//        System.out.println((char) data);
//        System.out.println("");
    }

    @Test
    public void test11(){
        String accessResult = AccessUtil.accessTemp(34.0);
        System.out.println(accessResult);
    }

    @Test
    public void test12(){
        System.out.println(AccessUtil.accessBloodPressure(new BloodPressure(147.0, 95)));
        System.out.println(BloodPressureEnum.HIGH2.getLowDiastolicPressure() + "---" + BloodPressureEnum.HIGH2.getLowSystolicPressure());

        System.out.println(147.0 >= BloodPressureEnum.HIGH2.getLowSystolicPressure());
        System.out.println( 95 >= BloodPressureEnum.HIGH2.getLowDiastolicPressure());


    }



}
