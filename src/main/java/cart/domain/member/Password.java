package cart.domain.member;

import cart.exception.badrequest.member.MemberEmailException;
import cart.exception.badrequest.member.MemberPasswordException;
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
            throw new MemberPasswordException("멤버 비밀번호는 존재하지 않거나 비어있을 수 없습니다.");
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new MemberEmailException("멤버 비밀번호는 최대 " + MAXIMUM_LENGTH + "글자까지 가능합니다. 현재 길이: " + value.length());
        }
    }

    public String getValue() {
        return value;
    }
}
