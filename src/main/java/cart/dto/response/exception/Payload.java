package cart.dto.response.exception;

public class Payload {

    private ExceptionResponse payload;

    private Payload(){
    }

    public Payload(final ExceptionResponse payload) {
        this.payload = payload;
    }

    public ExceptionResponse getPayload() {
        return payload;
    }
}
