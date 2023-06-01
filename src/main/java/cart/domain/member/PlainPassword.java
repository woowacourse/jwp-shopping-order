package cart.domain.member;

public class PlainPassword implements Password {

    private final String password;

    private PlainPassword(final String password) {
        validatePassword(password);
        this.password = password;
    }

    public static Password of(final String password) {
        return new PlainPassword(password);
    }

    private void validatePassword(final String password) {
        if (password.length() < 4 || password.length() > 10) {
            throw new IllegalArgumentException("비밀번호는 4글자 이상 10글자 이하로 입력해주세요.");
        }
    }

    @Override
    public String getPassword() {
        return password;
    }
}
