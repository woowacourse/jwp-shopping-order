package cart.persistance.entity;

import cart.domain.Member;

public class MemberEntity {

    private Long id;
    private String email;
    private String password;

    public MemberEntity(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static MemberEntity of(Member member) {
        return new MemberEntity(member.getId(), member.getEmail(), member.getPassword());
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
