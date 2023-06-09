package shop.application.member.dto;

import shop.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberDto {
    private final Long id;
    private final String name;
    private final String password;

    private MemberDto(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getPassword());
    }

    public static List<MemberDto> of(List<Member> members) {
        return members.stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
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
