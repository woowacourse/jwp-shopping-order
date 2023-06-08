package cart.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final Password password;

    public Member(final String name, final Password password) {
        validateName(name);
        this.id = null;
        this.name = name;
        this.password = password;
    }

    private void validateName(final String name) {
        if (name.length() < 4 || name.length() > 10) {
            throw new IllegalArgumentException("아이디는 4글자 이상 10글자 이하로 입력해주세요.");
        }
    }

    public Member(final Long id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = Password.of(password);
    }

    public boolean checkPassword(final String password) {
        return this.password.getPassword().equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password.getPassword();
    }
}
