package eastgate.tenantservice.response;

import eastgate.tenantservice.enums.ResponseCodeEnum;
import eastgate.tenantservice.exception.AppException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@ToString
public class BaseResponse<T> {
    private String code;
    private String message;
    private T data;

    public static<T> BaseResponse<T> error(AppException errorEnum) {
        BaseResponse<T> simpleResponseDTO = new BaseResponse<>();
        simpleResponseDTO.setCode(errorEnum.getCode());
        simpleResponseDTO.setMessage(errorEnum.getMessage());
        log.info("response from server = {}", simpleResponseDTO);
        return simpleResponseDTO;
    }

    public static<T> BaseResponse<T> success(T data) {
        BaseResponse<T> simpleResponseDTO = new BaseResponse<>();
        simpleResponseDTO.setCode(ResponseCodeEnum.SUCCESS.getCode());
        simpleResponseDTO.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        simpleResponseDTO.setData(data);
        log.info("response from server = {}", simpleResponseDTO);
        return simpleResponseDTO;
    }

    public static<T> BaseResponse<T> success() {
        BaseResponse<T> simpleResponseDTO = new BaseResponse<>();
        simpleResponseDTO.setCode(ResponseCodeEnum.SUCCESS.getCode());
        simpleResponseDTO.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        log.info("response from server = {}", simpleResponseDTO);
        return simpleResponseDTO;
    }
}
