package cart.ui.member.dto;

import cart.application.service.member.dto.MemberResultDto;

public class MemberResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;


    public MemberResponse(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static MemberResponse from(MemberResultDto memberResultDto) {
        return new MemberResponse(memberResultDto.getId(), memberResultDto.getName(), memberResultDto.getEmail(), memberResultDto.getPassword());
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
