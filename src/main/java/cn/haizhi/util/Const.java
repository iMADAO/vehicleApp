package cn.haizhi.util;

import java.util.ArrayList;
import java.util.List;

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
    public static final String BLOODFAT = "bloodFat";
    public static final String COUNT = "count";
    public static final String OUTPUT_FILE = "part-r-00000";
    public static final String REDIS_PREFIX = "VEHICLE";
    public static final String USER_PHONE = "phone";

    public static List<String> paramList;
    static{
        paramList = new ArrayList<>();
        paramList.add("bloodFat");
        paramList.add("bloodPressure");
        paramList.add("count");
        paramList.add("heartbeat");
        paramList.add("temperature");
        paramList.add("weight");
    }
}
