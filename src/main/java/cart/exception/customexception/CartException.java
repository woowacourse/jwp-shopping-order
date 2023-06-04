package cart.exception.customexception;

import java.util.Objects;

public class CartException extends RuntimeException{

    private final ErrorCode errorCode;

    public CartException(ErrorCode errorCode) {
        super();
        this.errorCode =errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartException that = (CartException) o;
        return errorCode == that.errorCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode);
    }
}
