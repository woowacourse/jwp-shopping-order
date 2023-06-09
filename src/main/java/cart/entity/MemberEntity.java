package cart.entity;

import cart.domain.Member;

public class MemberEntity {

    private final Long id;
    private final String email;

    public MemberEntity(String email) {
        this(null, email);
    }

    public MemberEntity(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Member toDomain() {
        return new Member(id, email);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
