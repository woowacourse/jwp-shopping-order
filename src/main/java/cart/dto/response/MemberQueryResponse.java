package cart.dto.response;

import cart.domain.Member;

public class MemberQueryResponse {
    private final Long id;
    private final String email;

    public MemberQueryResponse(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public static MemberQueryResponse from(final Member member) {
        return new MemberQueryResponse(
                member.getId(),
                member.getEmail()
        );
    }
}
