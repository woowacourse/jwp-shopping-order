package cart.mapper;

import cart.domain.Member;
import cart.dto.response.MemberResponse;
import cart.entity.MemberEntity;

public class MemberMapper {

    private MemberMapper() {
    }

    public static Member toMember(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getMoney(), memberEntity.getPoint());
    }

    public static MemberEntity toEntity(Member member) {
        return new MemberEntity(member.getId(), member.getEmail(), member.getPassword(), member.getMoney(), member.getPoint());
    }

    public static MemberResponse toResponse(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getMoney(), member.getPoint());
    }
}
