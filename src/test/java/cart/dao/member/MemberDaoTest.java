package cart.dao.member;

import cart.entity.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
@Import({MemberDao.class})
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("사용자를 추가한다.")
    void add_member() {
        // given
        MemberEntity member = new MemberEntity(null, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long memberId = memberDao.addMember(member);

        // when
        MemberEntity result = memberDao.getMemberById(memberId).get();

        // then
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getGrade()).isEqualTo(member.getGrade());
        assertThat(result.getTotalPurchaseAmount()).isEqualTo(member.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("전체 사용자를 조회한다.")
    void find_all() {
        // given
        MemberEntity ako = new MemberEntity(null, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long akoId = memberDao.addMember(ako);

        MemberEntity ddoring = new MemberEntity(null, "ddoring@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long ddoringId = memberDao.addMember(ddoring);

        List<MemberEntity> expect = List.of(
                new MemberEntity(akoId, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0),
                new MemberEntity(ddoringId, "ddoring@wooteco.com", "Abcd1234@", "NORMAL", 0));


        // when
        List<MemberEntity> result = memberDao.getAllMembers();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("멤버 정보를 업데이트 한다.")
    void update_member() {
        // given
        MemberEntity member = new MemberEntity(null, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long memberId = memberDao.addMember(member);

        MemberEntity savedMember = memberDao.getMemberById(memberId).get();

        MemberEntity updateMember = new MemberEntity(savedMember.getId(), savedMember.getEmail(), savedMember.getPassword(), "GOLD", 200_001);

        // when
        memberDao.updateMember(updateMember);
        MemberEntity result = memberDao.getMemberById(memberId).get();

        // then
        assertThat(result.getGrade()).isEqualTo(updateMember.getGrade());
        assertThat(result.getTotalPurchaseAmount()).isEqualTo(updateMember.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("사용자를 이메일로 조회한다.")
    void find_by_email() {
        // given
        MemberEntity member = new MemberEntity(null, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long memberId = memberDao.addMember(member);

        // when
        MemberEntity result = memberDao.getMemberByEmail(member.getEmail()).get();

        // then
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getGrade()).isEqualTo(member.getGrade());
        assertThat(result.getTotalPurchaseAmount()).isEqualTo(member.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("사용자를 삭제한다.")
    void delete() {
        // given
        MemberEntity member = new MemberEntity(null, "ako@wooteco.com", "Abcd1234@", "NORMAL", 0);
        Long memberId = memberDao.addMember(member);

        // when
        memberDao.deleteMember(memberId);
        Optional<MemberEntity> result = memberDao.getMemberById(memberId);

        // then
        assertThat(result.isPresent()).isFalse();
    }
}
