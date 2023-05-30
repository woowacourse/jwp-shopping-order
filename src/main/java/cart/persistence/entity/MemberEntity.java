package cart.persistence.entity;

import cart.domain.Member;

public class MemberEntity {

    private Long id;
    private String email;
    private String password;

    public MemberEntity(final String email, final String password) {
        this(null, email, password);
    }

    public MemberEntity(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member toDomain() {
        return new Member(id, email, password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
