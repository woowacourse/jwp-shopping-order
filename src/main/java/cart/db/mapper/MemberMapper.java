package cart.db.mapper;

import cart.db.entity.MemberEntity;
import cart.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberMapper {

    public static Member toDomain(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getName(), memberEntity.getPassword());
    }

    public static List<Member> toDomain(final List<MemberEntity> memberEntities) {
        return memberEntities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static MemberEntity toEntity(final Member member) {
        return new MemberEntity(member.getId(), member.getName(), member.getPassword());
    }
}
