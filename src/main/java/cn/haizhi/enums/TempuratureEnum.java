package cn.haizhi.enums;

import lombok.Getter;

@Getter
public enum TempuratureEnum {
//    public static final double warningOneTp = 37.4;
//    public static final double warningTwoTp = 38.1;
//    public static final double warningThreeTP = 39.1;
//    public static final double errorTP = 45.0;


    ERROR_H("数据异常，温度过高", 45.0, null),
    HEIGHT4("超高度发热", 41.1, 45.0),
    HEIGHT3("高度发热", 39.1, 41.0),
    HEIGHT2("中度发热,", 38.1, 39.0),
    HIGHER1("体温略高", 37.4, 38.0),
    NORMAL("体温正常", 36.0, 37.3),
    LOWER("体温略低", 35.0, 35.9),
    LOWER1("体温过低", null, 35.0);
    ;
    private String message;
    private Double startValue;
    private Double endValue;

    TempuratureEnum(String message, Double startValue, Double endValue) {
        this.message = message;
        this.startValue = startValue;
        this.endValue = endValue;
    }
}
