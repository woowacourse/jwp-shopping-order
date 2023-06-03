package cart.persistence.mapper;

import cart.domain.member.Member;
import cart.persistence.entity.MemberEntity;

public class MemberMapper {
    public static Member toDomain(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public static MemberEntity toEntity(Member member) {
        return new MemberEntity(member.getId(), member.getEmail(), member.getPassword());
    }
}
