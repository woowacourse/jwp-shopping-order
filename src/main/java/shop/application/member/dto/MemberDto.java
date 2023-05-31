package shop.application.member.dto;

import shop.domain.member.Member;

public class MemberDto {
    private Long id;
    private String name;
    private String password;

    private MemberDto() {
    }

    private MemberDto(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getPassword());
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
