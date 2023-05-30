package cart.persistence;

import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberName;
import cart.domain.repository.MemberRepository;
import cart.exception.PersistenceException;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import cart.util.Encryptor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

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
    public Member findById(Long id) {
        MemberEntity findMember = memberDao.findById(id)
                .orElseThrow(() -> new PersistenceException(id + "를 갖는 회원을 찾을 수 없습니다."));

        return toMember(findMember);
    }

    @Override
    public Member findByName(String name) {
        MemberEntity findMember = memberDao.findByName(name)
                .orElseThrow(() -> new PersistenceException(name + "을 갖는 회원을 찾을 수 없습니다."));

        return toMember(findMember);
    }

    private Member toMember(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                new MemberName(memberEntity.getName()),
                new EncryptedPassword(memberEntity.getPassword())
        );
    }

    @Override
    public boolean hasMember(Member member) {
        Optional<MemberEntity> findMember = memberDao.findByName(member.getName());

        if (findMember.isPresent()) {
            String encryptedPassword = Encryptor.encrypt(member.getPassword());

            return Objects.equals(encryptedPassword, findMember.get().getPassword());
        }

        return false;
    }
}
