package cart.Repository.mapper;

import cart.domain.Member.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Component;

public class MemberMapper {
    public static Member toMember(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword()
        );
    }
}
