package cart.persistence.mapper;

import cart.domain.member.Member;
import cart.domain.member.dto.MemberWithId;
import cart.persistence.dto.CartItemDto;
import cart.persistence.entity.MemberEntity;

public class MemberMapper {

    public static Member convertMember(final MemberEntity memberEntity) {
        return Member.createWithEncodedPassword(memberEntity.getName(), memberEntity.getPassword());
    }

    public static MemberWithId convertMemberRes(final MemberEntity memberEntity) {
        return new MemberWithId(memberEntity.getId(), convertMember(memberEntity));
    }

    public static Member convertMember(final CartItemDto cartItemDto) {
        return Member.create(cartItemDto.getMemberName());
    }
}
