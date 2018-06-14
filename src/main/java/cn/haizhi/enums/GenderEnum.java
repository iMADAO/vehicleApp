package cn.haizhi.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE((byte)0, "male", "M"),
    FEMALE((byte)1, "female", "F"),
    ;
    private byte code;
    private String message;
    private String flagStr;

    GenderEnum(byte code, String message, String flagStr) {
        this.code = code;
        this.message = message;
        this.flagStr = flagStr;
    }
}
