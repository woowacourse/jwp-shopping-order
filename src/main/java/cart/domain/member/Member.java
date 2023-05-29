package cart.domain.member;

public class Member {

    private final MemberName name;
    private final MemberPassword password;

    private Member(final MemberName name) {
        this(name, null);
    }

    private Member(final MemberName name, final MemberPassword password) {
        this.name = name;
        this.password = password;
    }

    public static Member create(final String name) {
        return new Member(MemberName.create(name));
    }

    public static Member create(final String name, final String password) {
        return new Member(MemberName.create(name), MemberPassword.create(password));
    }

    public static Member createWithEncodedPassword(final String name, final String password) {
        return new Member(MemberName.create(name), MemberPassword.createWithEncodedPassword(password));
    }

    public String name() {
        return name.getName();
    }

    public String password() {
        return password.getPassword();
    }
}
