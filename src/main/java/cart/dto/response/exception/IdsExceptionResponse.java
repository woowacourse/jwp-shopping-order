package cart.dto.response.exception;

import java.util.List;

public class IdsExceptionResponse {

    private String errorMessage;
    private List<Long> ids;

    public IdsExceptionResponse(final String errorMessage, final List<Long> ids) {
        this.errorMessage = errorMessage;
        this.ids = ids;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Long> getIds() {
        return ids;
    }
}
