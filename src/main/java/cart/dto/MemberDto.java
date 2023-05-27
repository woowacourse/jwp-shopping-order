package cart.dto;

import cart.domain.cart.Member;

public class MemberDto {

    private final Long id;
    private final String email;
    private final String password;

    public MemberDto(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static MemberDto from(final Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getPassword());
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
