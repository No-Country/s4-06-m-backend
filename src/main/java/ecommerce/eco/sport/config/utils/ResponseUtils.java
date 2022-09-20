package ecommerce.eco.sport.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.eco.sport.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    private ResponseUtils() {}

    public static void setCustomForbiddenResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(getGenericErrorResponse()));
    }
    private static ErrorResponse getGenericErrorResponse() {
        return ErrorResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("Access denied. Please, try to login again or conctact your admin.")
                .build();
    }
}
