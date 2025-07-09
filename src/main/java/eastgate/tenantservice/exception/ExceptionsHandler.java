package eastgate.tenantservice.exception;

import eastgate.tenantservice.response.BaseResponse;
import eastgate.tenantservice.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    private final ResponseFactory responseFactory;

    @ExceptionHandler(value = { AppException.class})
    protected ResponseEntity handleAppException(AppException ex) {
        return responseFactory.make(BaseResponse.error(ex));
    }
}
