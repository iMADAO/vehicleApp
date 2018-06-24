package cn.haizhi.util;

import cn.haizhi.bean.BloodPressure;
import cn.haizhi.bean.BloodPressureStandard;
import cn.haizhi.bean.GroupData;
import cn.haizhi.bean.GroupDataDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String VALIDATECODE = "validateCode";
    public static final Integer SSO_SESSION_EXPIRE = 604800;
    public static final String TEMP_DIR="/data/tmp_data";
    public static final String HDFS_DIR="hdfs://localhost:8020/vehicle";
    public static final String USER_INFO="userinfo";

    public static final String TEMPERATURE = "temperature";
    public static final String WEIGHT = "weight";
    public static final String HEARTBEAT = "heartbeat";
    public static final String BLOODPRESSURE = "bloodPressure";
    public static final String  SYSTOLICPRESSURE = "systolicPressure";
    public static final String DIASTOLICPRESSURE = "diastolicPressure";
    public static final String BLOODFAT = "bloodFat";
    public static final String COUNT = "count";
    public static final String OUTPUT_FILE = "part-r-00000";
    public static final String REDIS_PREFIX = "VEHICLE";
    public static final String USER_PHONE = "phone";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String lastDateStr = "20180101";
    public static final String lastMonthStr = "201801";
    public static final LocalDate lastDate = LocalDate.parse(lastDateStr, dateTimeFormatter);
    public static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    public static final String NOT_DATA = "not-data";
    public static final String DEVICE = "device";

    public static Map<String, GroupDataDTO> data = new HashMap<>();
    public static List<String> paramList;

    public static Map<String, BloodPressure> bpStandardMap;

    public static Map<String, String> paramMap;
    static{
        paramList = new ArrayList<>();
        paramList.add("bloodFat");
        paramList.add("systolicPressure");
        paramList.add("diastolicPressure");
        paramList.add("count");
        paramList.add("heartbeat");
        paramList.add("temperature");
        paramList.add("weight");

        paramMap = new HashMap<>();
        paramMap.put("temperature", "temperature");
        paramMap.put("bloodFat", "bloodFat");
        paramMap.put("systolicPressure", "systolicPressure");
        paramMap.put("diastolicPressure", "diastolicPressure");
        paramMap.put("weight", "weight");
        paramMap.put("heartbeat", "heartbeat");
        paramMap.put("count", "count");

        bpStandardMap = new HashMap<>();
//        bpStandardMap.put("16-20-0", new BloodPressure(115, 73));
//        bpStandardMap.put("21-25-0", new BloodPressure(115, 73));
//        bpStandardMap.put("26-30-0", new BloodPressure(115, 75));
//        bpStandardMap.put("31-35-0", new BloodPressure(117, 76));
//        bpStandardMap.put("36-40-0", new BloodPressure(120, 80));
//        bpStandardMap.put("41-45-0", new BloodPressure(124, 81));
//        bpStandardMap.put("46-50-0", new BloodPressure(128, 82));
//        bpStandardMap.put("51-55-0", new BloodPressure(134, 84));
//        bpStandardMap.put("56-60-0", new BloodPressure(137, 84));
//        bpStandardMap.put("61-65-0", new BloodPressure(148, 86));
//
//        bpStandardMap.put("16-20-1", new BloodPressure(110, 70));
//        bpStandardMap.put("21-25-1", new BloodPressure(110, 71));
//        bpStandardMap.put("26-30-1", new BloodPressure(112,73));
//        bpStandardMap.put("31-35-1", new BloodPressure(114, 74));
//        bpStandardMap.put("36-40-1", new BloodPressure(116, 77));
//        bpStandardMap.put("41-45-1", new BloodPressure(122, 78));
//        bpStandardMap.put("46-50-1", new BloodPressure(128, 79));
//        bpStandardMap.put("51-55-1", new BloodPressure(134, 80));
//        bpStandardMap.put("56-60-1", new BloodPressure(139, 82));
//        bpStandardMap.put("61-65-1", new BloodPressure(145, 83));
    }
}
