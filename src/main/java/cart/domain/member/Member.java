package cart.domain.member;

import cart.ui.member.dto.MemberRequest;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(final MemberRequest memberRequest) {
        this(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword());
    }

    public Member(
            final String name,
            final String email,
            final String password
    ) {
        this(null, name, email, password);
    }

    public Member(
            final Long id,
            final String name,
            final String email,
            final String password
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isEqualId(Long memberId) {
        return this.id.equals(memberId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
