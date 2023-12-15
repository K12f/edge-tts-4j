package io.github.k12f.edgetts4j;

import io.github.k12f.edgetts4j.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class EdgeTTSException extends Exception {

    @Serial
    private static final long serialVersionUID = 5061470124605586443L;

    private Integer code;

    private String msg;

    public EdgeTTSException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        code = codeEnum.getCode();
        msg = codeEnum.getMsg();
    }

    public EdgeTTSException(ResponseCodeEnum codeEnum, String msg) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg() + "msg:" + msg;
    }

    public EdgeTTSException(String msg, Throwable cause) {
        super(msg, cause);
        code = ResponseCodeEnum.SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public EdgeTTSException(String msg) {
        super(msg);
    }

    public EdgeTTSException(Throwable cause) {
        super(cause);
    }
}
