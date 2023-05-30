package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MemberDaoTest extends DaoTest {

    private static final Member dummyMember1 = new Member("email@email.com", "password");

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 회원_데이터를_삽입한다() {
        // when
        long id = memberDao.insert(dummyMember1);

        // then
        assertThat(id).isNotZero();
    }

    @Test
    void ID로_단일_회원_데이터를_조회한다() {
        // when
        long savedId = memberDao.insert(dummyMember1);

        // then
        Member foundMember = memberDao.findById(savedId);
        assertThat(foundMember).isNotNull();
    }

    @Test
    void email로_단일_회원_데이터를_조회한다() {
        // when
        memberDao.insert(dummyMember1);

        // then
        Member foundMember = memberDao.findByEmail(dummyMember1.getEmail());
        assertThat(foundMember).isNotNull();
    }

    @Test
    void 존재하지_않은_회원을_ID로_조회하면_예외가_발생한다() {
        // given
        long id = 1241241L;

        // expect
        assertThatThrownBy(() -> memberDao.findById(id))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 존재하지_않은_회원을_email로_조회하면_예외가_발생한다() {
        // given
        String email = "abcd@email.com";

        // expect
        assertThatThrownBy(() -> memberDao.findByEmail(email))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 모든_회원_데이터를_조회한다() {
        // given
        memberDao.insert(dummyMember1);

        // when
        List<Member> result = memberDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 회원_데이터를_수정한다() {
        // given
        memberDao.insert(dummyMember1);

        String email = "doggy@naver.com";
        String password = "q1w2e3!";
        int point = 500;
        Member newMember = new Member(1L, email, password, point);

        // when
        memberDao.update(newMember);

        // then
        Member foundMember = memberDao.findByEmail(email);
        assertAll(
                () -> assertThat(foundMember.getEmail()).isEqualTo(email),
                () -> assertThat(foundMember.getPassword()).isEqualTo(password),
                () -> assertThat(foundMember.getPoint()).isEqualTo(point)
        );
    }

    @Test
    void ID로_회원_데이터를_삭제한다() {
        // given
        long savedId = memberDao.insert(dummyMember1);

        // when
        memberDao.delete(savedId);

        // then
        List<Member> allMember = memberDao.findAll();
        assertThat(allMember).isEmpty();
    }
}
