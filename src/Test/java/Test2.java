import cn.haizhi.form.DateForm;
import cn.haizhi.util.DateFormatUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
}
