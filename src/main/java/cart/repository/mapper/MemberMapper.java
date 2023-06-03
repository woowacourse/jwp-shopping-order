package cart.repository.mapper;

import cart.dao.entity.MemberEntity;
import cart.domain.Member;

public class MemberMapper {

    private MemberMapper() {
    }

    public static Member toMember(MemberEntity memberEntity) {
        return new Member(
            memberEntity.getId(),
            memberEntity.getEmail(),
            memberEntity.getPassword()
        );
    }

}
