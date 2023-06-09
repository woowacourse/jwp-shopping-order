package cart.repository.mapper;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.entity.MemberEntity;

public class MemberMapper {

    public static Member toMember(final MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                new Email(memberEntity.getEmail()),
                new Password(memberEntity.getPassword())
        );
    }
}
