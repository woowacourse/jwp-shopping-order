package cart.domain.member;

public class MemberPassword {

    private final String password;

    private MemberPassword(String password) {
        this.password = password;
    }

    public static MemberPassword from(String password) {
        validate(password);
        return new MemberPassword(password);
    }

    private static void validate(String password) {
        if (password.length() < 6 || password.length() > 12) {
            throw new IllegalArgumentException("잘못된 비밀번호 형식입니다.");
        }
    }
}
