package cart.db.entity;

import cart.domain.member.Member;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;

    public MemberEntity(String email, String password) {
        this(null, email, password);
    }

    public MemberEntity(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public Member toDomain() {
        return new Member(id, email, password);
    }
}
