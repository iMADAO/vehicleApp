package cn.haizhi.enums;

import lombok.Getter;

@Getter
public enum TypeEnum {
    TEMPERATURE((byte)1),
    BLOODPRESSURE((byte)2),
    ;
    private byte type;

    TypeEnum(byte type) {
        this.type = type;
    }
}
