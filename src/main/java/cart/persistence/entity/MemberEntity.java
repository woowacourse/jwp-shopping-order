package cart.persistence.entity;

import cart.domain.Member;

public class MemberEntity {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    public MemberEntity(final String email, final String password, final String nickname) {
        this(null, email, password, nickname);
    }

    public MemberEntity(final Long id, final String email, final String password, final String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toDomain() {
        return new Member(id, email, password, nickname);
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

    public String getNickname() {
        return nickname;
    }
}
