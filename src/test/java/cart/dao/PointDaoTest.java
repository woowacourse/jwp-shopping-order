package cart.dao;

import cart.dao.entity.PointEntity;
import cart.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@JdbcTest
class PointDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private MemberDao memberDao;
    private PointDao pointDao;
    private Long memberId;

    @BeforeEach
    public void setUp() {
        pointDao = new PointDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        memberDao.addMember(new Member("aaa@email.com","1234"));
        memberId = memberDao.getMemberByEmail("aaa@email.com").get().getId();
        pointDao.insertPoint(memberId);
    }

    @Test
    public void setPoint() {
        pointDao.updatePoint(memberId, 100L);
        PointEntity pointEntity = pointDao.findPointByMemberId(memberId).get();
        Assertions.assertThat(pointEntity.getPoint()).isEqualTo(100L);
    }

}
