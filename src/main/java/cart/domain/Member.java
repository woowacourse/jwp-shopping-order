package cart.domain;

import cart.application.service.MemberRequest;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(final String name, final String email, final String password) {
        this(null, name, email, password);
    }

    public Member(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(final MemberRequest memberRequest) {
        this(null, memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
