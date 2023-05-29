package cart.domain.member;

import cart.exception.MemberException.PasswordEmpty;
import cart.exception.MemberException.PasswordOverLength;
import org.apache.logging.log4j.util.Strings;

class Password {

    private static final int MAXIMUM_LENGTH = 255;

    private final String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Strings.isBlank(value)) {
            throw new PasswordEmpty();
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new PasswordOverLength(value.length(), MAXIMUM_LENGTH);
        }
    }

    public String getValue() {
        return value;
    }
}
