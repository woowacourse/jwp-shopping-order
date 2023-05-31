package cart.persistence.mapper;

import static cart.persistence.mapper.MemberCouponMapper.convertMemberCoupons;

import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.dto.MemberWithId;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.MemberEntity;
import java.util.List;

public class MemberMapper {

    public static Member convertMember(final MemberEntity memberEntity) {
        return Member.create(memberEntity.getName(), EncryptedPassword.create(memberEntity.getPassword()));
    }

    public static MemberWithId convertMemberWithId(final MemberEntity memberEntity) {
        return new MemberWithId(memberEntity.getId(), convertMember(memberEntity));
    }

    public static MemberWithId convertMemberWithId(final OrderDto orderDto) {
        return new MemberWithId(orderDto.getMemberId(), Member.create(orderDto.getMemberName(),
            EncryptedPassword.create(orderDto.getMemberPassword())));
    }

    public static Member convertMember(final CartItemDto cartItemDto) {
        return Member.create(cartItemDto.getMemberName(), EncryptedPassword.create(cartItemDto.getMemberPassword()));
    }

    public static Member convertMember(final List<MemberCouponDto> myCouponsByName, final MemberEntity memberEntity) {
        return Member.create(memberEntity.getName(), EncryptedPassword.create(memberEntity.getPassword()),
            convertMemberCoupons(myCouponsByName));
    }
}
