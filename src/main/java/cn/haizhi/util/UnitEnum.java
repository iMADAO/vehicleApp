package cn.haizhi.util;

import lombok.Getter;

@Getter
public enum UnitEnum {

    TEMPURATURE("°C"),
    WEIGHT("kg"),
    BLOODFAT("mmol/L"),
//    BLOODFAT("mg/dL"),
    HEARTBEAT("bpm"),
    BLOODPRESSURE("mmHg"),
    ;

    private String unit;

    UnitEnum(String unit) {
        this.unit = unit;
    }
}
