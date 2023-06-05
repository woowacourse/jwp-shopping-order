package cart.entity;

import cart.dto.MemberInfo;

public class MemberInfoEntity {

    private final Long id;
    private final String email;

    public MemberInfoEntity(String email) {
        this(null, email);
    }

    public MemberInfoEntity(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public MemberInfo toDomain() {
        return new MemberInfo(id, email);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
