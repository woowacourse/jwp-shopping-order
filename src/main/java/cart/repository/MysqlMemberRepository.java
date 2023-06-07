package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import cart.exception.OrderException;
import cart.exception.OrderServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MysqlMemberRepository implements MemberRepository {

    private static final String INVALID_ID_MESSAGE = "올바른 id를 입력해주세요.";
    private static final String INVALID_EMAIL_MESSAGE = "올바른 이메일을 입력해주세요.";
    private static final String FAIL_READ_BY_ID_MESSAGE = "멤버 id 조회 실패";
    private static final String FAIL_READ_BY_EMAIL_MESSAGE = "멤버 Email 조회 실패";

    private final MemberDao memberDao;

    public MysqlMemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberById(Long id) {
        MemberEntity memberEntity;
        try {
            memberEntity = memberDao.getMemberById(id);
        } catch (DataAccessException dataAccessException) {
            throw new OrderServerException(FAIL_READ_BY_ID_MESSAGE);
        }
        if (memberEntity == null) {
            throw new OrderException(INVALID_ID_MESSAGE);
        }
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public Member getMemberByEmail(String email) {
        MemberEntity memberEntity;
        try {
            memberEntity = memberDao.getMemberByEmail(email);
        } catch (DataAccessException dataAccessException) {
            throw new OrderServerException(FAIL_READ_BY_EMAIL_MESSAGE);
        }
        if (memberEntity == null) {
            throw new OrderException(INVALID_EMAIL_MESSAGE);
        }
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public List<Member> getAllMembers() {
        List<MemberEntity> memberEntities = memberDao.getAllMembers();
        return memberEntities.stream()
                .map(memberEntity -> new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword()))
                .collect(Collectors.toList());
    }
}
