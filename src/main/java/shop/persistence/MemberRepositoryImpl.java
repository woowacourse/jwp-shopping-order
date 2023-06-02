package shop.persistence;

import org.springframework.stereotype.Repository;
import shop.domain.member.EncryptedPassword;
import shop.domain.member.Member;
import shop.domain.member.MemberName;
import shop.domain.repository.MemberRepository;
import shop.exception.DatabaseException;
import shop.persistence.dao.MemberDao;
import shop.persistence.entity.MemberEntity;
import shop.util.Encryptor;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberDao memberDao;

    public MemberRepositoryImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Long save(Member member) {
        String encryptedPassword = Encryptor.encrypt(member.getPassword());
        MemberEntity memberEntity = new MemberEntity(member.getName(), encryptedPassword);

        return memberDao.insertMember(memberEntity);
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> allMemberEntities = memberDao.findAll();

        return allMemberEntities.stream()
                .map(this::toMember)
                .collect(Collectors.toList());
    }

    @Override
    public Member findById(Long id) {
        MemberEntity findMember = memberDao.findById(id)
                .orElseThrow(() -> new DatabaseException.IllegalDataException(
                        id + "를 갖는 회원을 찾을 수 없습니다.")
                );

        return toMember(findMember);
    }

    @Override
    public Member findByName(String name) {
        MemberEntity findMember = memberDao.findByName(name)
                .orElseThrow(() -> new DatabaseException.IllegalDataException(
                        name + "을 갖는 회원을 찾을 수 없습니다.")
                );

        return toMember(findMember);
    }

    private Member toMember(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                new MemberName(memberEntity.getName()),
                new EncryptedPassword(memberEntity.getPassword())
        );
    }
}
