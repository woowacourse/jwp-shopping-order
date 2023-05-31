package cart.dto.response.exception;

import cart.dto.response.exception.IdsExceptionResponse;

public class Payload {

    private final IdsExceptionResponse payload;

    public Payload(final IdsExceptionResponse payload) {
        this.payload = payload;
    }

    public IdsExceptionResponse getPayload() {
        return payload;
    }
}
