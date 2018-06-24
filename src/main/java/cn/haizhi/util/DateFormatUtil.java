package cn.haizhi.util;

import cn.haizhi.bean.WeekPair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateFormatUtil {
    public static String DateToString(Date date){
//        Integer year = calendar.get(Calendar.YEAR);
//        Integer month = calendar.get(Calendar.MONTH);
//        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
//        return year.toString() + month.toString() + day.toString();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static Date StringToDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(dateStr);
    }

    public static Date getLastTimeOfDay(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    //todo test
    public static String getDateStrNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.now();
        String dateStr = date.format(dtf);
        return dateStr;
    }

    //todo test
    public static String[] getWeekDateString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.now();
        String[] dateStr = new String[7];
        for (int i = 0; i < 7; i++) {
            date = date.minusDays(1);
            dateStr[i] = date.format(dtf);
        }
        return dateStr;
    }


    public static List<String> getWeekDateStrList() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.now();
        List<String> dateStrList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            date = date.minusDays(1);
            dateStrList.add(date.format(dtf));
        }
        return dateStrList;
    }

    public static List<String> getDateStrInRange(String dateStartStr, String dateEndStr){
        LocalDate dateStart = null;
        LocalDate dateEnd = null;

        dateStart = LocalDate.parse(dateStartStr, Const.dateTimeFormatter);
        dateEnd = LocalDate.parse(dateEndStr, Const.dateTimeFormatter);
        if (dateStart.compareTo(dateEnd)>0){
            LocalDate temp = dateStart;
            dateStart = dateEnd;
            dateEnd = temp;
        }

        if (dateEnd.compareTo(Const.lastDate)<0)
            return null;
        if (dateStart.compareTo(Const.lastDate)<0)
            dateStart = Const.lastDate;

        LocalDate now = LocalDate.now();
        if (dateStart.compareTo(now)>0)
            return null;
        if (dateEnd.compareTo(now)>0)
            dateEnd = now;

        List<String> list = new ArrayList<>();
        while(dateStart.compareTo(dateEnd)<=0){
            String str = dateStart.format(Const.dateTimeFormatter);
            list.add(str);
            dateStart = dateStart.plusDays(1);
        }
        return list;
    }

    public static List<String> getMonthStrInRange(String startMonthStr, String endMonthStr) {
        List<String> list = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(startMonthStr + "01", Const.dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(endMonthStr + "01", Const.dateTimeFormatter);
        if (startDate.compareTo(endDate) > 0) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        //如果输入的年月在规定的最早年月之前，就返回null
        if (endDate.compareTo(Const.lastDate) < 0)
            return null;
        if (startDate.compareTo(Const.lastDate) < 0)
            startDate = Const.lastDate;

        LocalDate now = LocalDate.now();
        if (endDate.compareTo(now) > 0) {
            endDate = LocalDate.parse(now.format(Const.monthFormatter) + "01", Const.dateTimeFormatter);
        }

        while (startDate.compareTo(endDate) <= 0) {
            list.add(startDate.format(Const.monthFormatter));
            startDate = startDate.plusMonths(1);
        }
        return list;
    }

    public static List<LocalDate> getWeeklyDateStr(){
        List<LocalDate> localDateList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int n = dayOfWeek.getValue();
        while(n>1){
            n--;
            localDate = localDate.minusDays(1);
        }

        localDateList.add(localDate);

        n = dayOfWeek.getValue();
        localDate = LocalDate.now();
        while(n<7){
            n++;
            localDate = localDate.plusDays(1);
        }

        localDateList.add(localDate);
        return localDateList;
    }
}
