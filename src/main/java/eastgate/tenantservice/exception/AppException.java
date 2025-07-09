package eastgate.tenantservice.exception;

import eastgate.tenantservice.enums.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppException extends Exception{
    private String code;
    private String message;

    public AppException(ResponseCodeEnum codeEnum) {
        super();
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
}
