package cart.domain.member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberEmail {
    String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private final Pattern emailPattern = Pattern.compile(regex);
    private final String memberEmail;

    private MemberEmail(String memberEmail) {
        validate(memberEmail);
        this.memberEmail = memberEmail;
    }

    private void validate(String memberEmail) {
        Matcher matcher = emailPattern.matcher(memberEmail);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public static MemberEmail from(String memberEmail) {
        return new MemberEmail(memberEmail);
    }

    public String getMemberEmail() {
        return memberEmail;
    }
}
