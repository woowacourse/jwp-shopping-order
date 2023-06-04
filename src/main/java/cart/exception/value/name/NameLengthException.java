package cart.exception.value.name;

import cart.exception.value.ValueException;

public class NameLengthException extends ValueException {

    private static final String NAME_LENGTH_EXCEPTION_MESSAGE = "상품 이름의 길이는 100자보다 길면 안됩니다.";

    public NameLengthException() {
        super(NAME_LENGTH_EXCEPTION_MESSAGE);
    }
}
