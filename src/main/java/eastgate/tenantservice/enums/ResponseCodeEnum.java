package eastgate.tenantservice.enums;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    SUCCESS("CODE_00", "success"),
    UNAUTHORIZED("CODE_01", "UNAUTHORIZED"),
    FORBIDDEN("CODE_02", "FORBIDDEN");

    private String code;
    private String message;

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
