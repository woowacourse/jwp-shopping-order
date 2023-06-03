package shop.domain.member;

import java.util.Objects;

public class Member {
    private final Long id;
    private final MemberName name;
    private final Password password;
//    private Cart cart;

    public Member(Long id, MemberName name, Password password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Member(MemberName name, Password password) {
        this(null, name, password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password.getPassword(), password);
    }
}
