package cart.dto.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse extends Response {
    private final Map<String, String> validation = new HashMap<>();

    public ErrorResponse(String code, String message) {
        super(code, message);
    }

    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse("400", message);
    }

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }

}
