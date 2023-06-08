package cart.repository.mapper;

import cart.dao.dto.member.MemberDto;
import cart.domain.Member;

public class MemberMapper {

    private MemberMapper() {
    }

    public static Member toMember(MemberDto memberDto) {
        return new Member(
            memberDto.getId(),
            memberDto.getEmail(),
            memberDto.getPassword()
        );
    }

}
