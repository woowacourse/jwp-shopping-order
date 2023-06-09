package cart.controller.dto;

public class Payload {
    private String errorMessage;

    public Payload() {
    }

    public Payload(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
