package cart.ui.member.dto;

import cart.application.service.member.dto.MemberDto;

public class MemberResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;


    private MemberResponse(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static MemberResponse from(MemberDto memberDto) {
        return new MemberResponse(memberDto.getId(), memberDto.getName(), memberDto.getEmail(), memberDto.getPassword());
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
