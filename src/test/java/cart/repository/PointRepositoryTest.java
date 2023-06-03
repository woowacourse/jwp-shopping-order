package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.dao.entity.PointEntity;
import cart.dao.entity.PointHistoryEntity;
import cart.domain.Point;
import cart.exception.AuthenticationException;
import cart.exception.OrderNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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

    @Mock
    private PointHistoryDao pointHistoryDao;

    private PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        this.pointRepository = new PointRepository(pointDao, pointHistoryDao);
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

    @Test
    @DisplayName("한 주문에 적립된 포인트, 사용한 포인트를 저장한다.")
    void savePointHistory() {
        // given
        willDoNothing().given(pointHistoryDao).save(any(PointHistoryEntity.class));
        Point usedPoint = new Point(1000);
        Point pointToSave = new Point(500);

        // when, then
        Assertions.assertDoesNotThrow(
            () -> pointRepository.savePointHistory(pointToSave, usedPoint, 1L, 1L));
    }

    @Test
    @DisplayName("한 주문에서 적립된 포인트를 조회한다.")
    void findSavedPointByOrderId() {
        // given
        long orderId = 1L;
        given(pointHistoryDao.findByOrderId(1L)).willReturn(
            Optional.of(new PointHistoryEntity(1L, 1L, 1000, 500, orderId)));

        // when
        Point savedPoint = pointRepository.findSavedPointByOrderId(orderId);

        // then
        assertThat(savedPoint).isEqualTo(new Point(500));
    }

    @Test
    @DisplayName("조회할 적립된 포인트의 주문 번호가 존재하지 않는다면 예외가 발생한다.")
    void findSavedPointByOrderId_notExistingOrderId_fail() {
        // given
        long nonExistingOrderId = 2L;
        given(pointHistoryDao.findByOrderId(nonExistingOrderId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> pointRepository.findSavedPointByOrderId(nonExistingOrderId))
            .isInstanceOf(OrderNotFoundException.class);
    }
}