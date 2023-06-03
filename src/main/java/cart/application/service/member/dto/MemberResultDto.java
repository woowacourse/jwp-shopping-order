package cart.application.service.member.dto;

import cart.domain.Member;

public class MemberResultDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;


    private MemberResultDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static MemberResultDto from(Member member) {
        return new MemberResultDto(member.getId(), member.getName(), member.getEmail(), member.getPassword());
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
