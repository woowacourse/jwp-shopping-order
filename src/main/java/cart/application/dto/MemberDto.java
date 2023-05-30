package cart.application.dto;

import cart.domain.member.Member;

public class MemberDto {
    private final Long id;
    private final String name;
    private final String password;

    public MemberDto(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public MemberDto(String name, String password) {
        this(null, name, password);
    }

    public MemberDto(Member member) {
        this(member.getId(), member.getName(), member.getPassword());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
