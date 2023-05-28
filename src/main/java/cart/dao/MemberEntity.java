package cart.dao;

import cart.domain.Member;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;

    public MemberEntity(final String email, final String password) {
        this(null, email, password);
    }

    public MemberEntity(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static MemberEntity from(final Member member) {
        return new MemberEntity(member.getEmail(), member.getPassword());
    }
    
    public Member toMember() {
        return new Member(this.id, this.email, this.password);
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
