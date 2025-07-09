package eastgate.tenantservice.response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {
    public ResponseFactory() {
    }
    public ResponseEntity make(BaseResponse response) {
        return ResponseEntity.ok(response);
    }

}
