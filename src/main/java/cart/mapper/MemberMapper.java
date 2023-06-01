package cart.mapper;

import cart.domain.Member;
import cart.entity.MemberEntity;

public class MemberMapper {

    private MemberMapper() {
    }

    public static Member toMember(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getMoney(), memberEntity.getPoint());
    }
}
