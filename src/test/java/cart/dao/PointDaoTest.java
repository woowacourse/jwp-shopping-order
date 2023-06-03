package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAscillCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class PointDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PointDao pointDao;

    @BeforeEach
    void setUp() {
        pointDao = new PointDao(jdbcTemplate);
    }

    @Test
    void 멤버_ID_와_포인트를_받아_해당_멤버의_포인트를_수정한다() {
        // given
        final Long memberId = 1L;
        final int point = 2000;

        // when
        pointDao.update(memberId, point);

        // then
        assertThat(pointDao.findByMemberId(1L).get().getPoint()).isEqualTo(2000);
    }
}
