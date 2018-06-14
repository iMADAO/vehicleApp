package cn.haizhi.exception;


import cn.haizhi.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class MadaoException extends RuntimeException {
    private Integer code;
    private Object id;

    public MadaoException(int code, String mess){
        super(mess);
        this.code = code;
    }

    public MadaoException(int code, String mess, Object id){
        super(mess);
        this.code = code;
        this.id = id;
    }

    public MadaoException(ErrorEnum e, Object id){
        super(e.getMessage());
        this.code = e.getCode();
        this.id = id;
    }

    public MadaoException(ErrorEnum e){
        super(e.getMessage());
        this.code = e.getCode();
    }
}
