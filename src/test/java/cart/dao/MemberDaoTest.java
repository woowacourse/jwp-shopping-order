package cart.dao;

import cart.dao.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @Autowired
    public MemberDaoTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    public void 전체를_조회한다() {
        List<MemberEntity> members = memberDao.findAll();

        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void 이메일로_조회한다() {
        String email = "a@a.com";

        Optional<MemberEntity> memberOptional = memberDao.findByEmail(email);

        assertThat(memberOptional).isPresent();
        MemberEntity member = memberOptional.get();
        assertThat(member.getNickname()).isEqualTo("라잇");
    }

    @Test
    public void 이메일로_조회_시_없으면_빈_값을_반환한다() {
        String email = "nonexisting@example.com";

        Optional<MemberEntity> memberOptional = memberDao.findByEmail(email);

        assertThat(memberOptional).isEmpty();
    }

    @Test
    public void 아이디로_조회힌다() {
        Long id = 1L;

        Optional<MemberEntity> memberOptional = memberDao.findById(id);

        assertThat(memberOptional).isPresent();
        MemberEntity member = memberOptional.get();
        assertThat(member.getId()).isEqualTo(id);
    }

    @Test
    public void 아이디로_조회_시_없으면_빈_값을_반환한다() {
        Long id = 100L;

        Optional<MemberEntity> memberOptional = memberDao.findById(id);

        assertThat(memberOptional).isEmpty();
    }
}
