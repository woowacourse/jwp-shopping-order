package cart.exception.dto;

public class ExceptionDto {

    private final String exceptionMessage;

    private ExceptionDto(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public static ExceptionDto from(Exception exception) {
        return new ExceptionDto(exception.getMessage());
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

}
