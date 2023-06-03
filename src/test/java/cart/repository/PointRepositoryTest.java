package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.dao.PointDao;
import cart.dao.entity.PointEntity;
import cart.domain.Point;
import cart.exception.AuthenticationException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointRepositoryTest {

    @Mock
    private PointDao pointDao;

    private PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        this.pointRepository = new PointRepository(pointDao);
    }

    @Test
    @DisplayName("사용자의 보유 포인트를 조회할 수 있다.")
    void findByMemberId() {
        // given
        given(pointDao.findByMemberId(1L)).willReturn(Optional.of(new PointEntity(1L, 1L, 1000)));

        // when
        Point point = pointRepository.findByMemberId(1L);

        // then
        assertThat(point).isEqualTo(new Point(1000));
    }

    @Test
    @DisplayName("사용자의 id가 존재하지 않는다면 예외가 발생한다.")
    void findByMemberId_fail() {
        // given
        given(pointDao.findByMemberId(1L)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> pointRepository.findByMemberId(1L))
            .isInstanceOf(AuthenticationException.class);
    }
}