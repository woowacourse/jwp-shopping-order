package cart.application.mapper;

import cart.domain.Member;
import cart.persistence.entity.MemberEntity;

public class MemberMapper {

    public static Member toMember(final MemberEntity entity) {
        return new Member(entity.getId(), entity.getEmail(), entity.getPassword());
    }
}
