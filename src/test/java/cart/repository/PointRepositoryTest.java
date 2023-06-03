package cart.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.dao.PointDao;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.exception.PointException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class PointRepositoryTest {

    @Mock
    private PointDao pointDao;

    private PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        pointRepository = new PointRepository(pointDao);
    }

    @Test
    void 존재하지_않는_멤버_포인트를_조회하면_예외가_발생한다() {
        // given
        final Member member = new Member(Long.MAX_VALUE, new Email("a@a.com"), new Password("1234"));
        given(pointDao.findByMemberId(Long.MAX_VALUE)).willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> pointRepository.findPointByMember(member))
                .isInstanceOf(PointException.NotFound.class);
    }
}
