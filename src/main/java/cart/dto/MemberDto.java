package cart.dto;

import cart.domain.Member;

public class MemberDto {
    private final Long id;
    private final String email;

    private MemberDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberDto from(Member member){
        return new MemberDto(member.getId(), member.getEmail());
    }
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
