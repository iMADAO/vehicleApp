package cn.haizhi.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum DataNameEnum {
    TEMPERATURE("temperature"),
    WEIGHT("weight"),
    HEARTBEAT("heartbeat"),
    BLOODPRESSUER("bloodPressure"),
    SYSTOLICPRESSURE("systolicPressure"),
    DIASTOLICPRESSURE("diastolicPressure"),
    BLOODFAT("bloodFat"),
    COUNT("count");
    ;


    private String name;

    DataNameEnum(String name) {
        this.name = name;
    }
}
