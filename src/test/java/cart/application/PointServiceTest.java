package cart.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.application.event.PointAdditionEvent;
import cart.dao.PointAdditionDao;
import cart.dao.PointUsageDao;
import cart.domain.PointAddition;
import cart.domain.PointCalculator;
import cart.domain.PointUsage;
import cart.exception.IllegalPointException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointServiceTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final long orderId = 1L;
    private final long memberId = 1L;

    @Spy
    private PointCalculator pointCalculator;
    @Mock
    private PointAdditionDao pointAdditionDao;
    @Mock
    private PointUsageDao pointUsageDao;
    @InjectMocks
    private PointService pointService;

    @Nested
    class 주문_이벤트에_의해_포인트를_증감한다 {

        @Test
        void 지불할_금액을_초과하여_포인트를_사용하는_경우_예외가_발생한다() {
            // given
            int usePointAmount = 1000;
            int payAmount = 999;
            PointAdditionEvent pointAdditionEvent = makeEvent(usePointAmount, payAmount);

            // when & then
            assertThatThrownBy(
                () -> pointService.handlePointProcessInOrder(pointAdditionEvent))
                .isInstanceOf(IllegalPointException.UsingMorePointThanPrice.class)
                .hasMessage("지불할 금액을 초과하는 포인트를 사용할 수 없습니다");
        }

        @Test
        void 보유한_포인트_이상을_사용하는_경우_예외가_발생한다() {
            // given
            int usePointAmount = 1000;
            int payAmount = 20000;
            PointAdditionEvent pointAdditionEvent = makeEvent(usePointAmount, payAmount);
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus201 = new PointUsage(1L, memberId, orderId, 1L, 201);

            given(pointAdditionDao.findAllByMemberId(anyLong())).willReturn(List.of(plus500, plus700));
            given(pointUsageDao.findAllByMemberId(anyLong())).willReturn(List.of(minus201));

            // when & then
            assertThatThrownBy(() ->
                pointService.handlePointProcessInOrder(pointAdditionEvent))
                .isInstanceOf(IllegalPointException.PointIsNotEnough.class)
                .hasMessage("보유한 포인트 이상을 사용할 수 없습니다");
        }

        @Test
        void 포인트_관련_처리가_정상적으로_이루어진다() {
            // given
            int usePointAmount = 1000;
            int payAmount = 20000;
            PointAdditionEvent pointAdditionEvent = makeEvent(usePointAmount, payAmount);
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus200 = new PointUsage(1L, memberId, orderId, 1L, 200);

            given(pointAdditionDao.findAllByMemberId(anyLong())).willReturn(List.of(plus500, plus700));
            given(pointUsageDao.findAllByMemberId(anyLong())).willReturn(List.of(minus200));

            // when & then
            assertDoesNotThrow(() ->
                pointService.handlePointProcessInOrder(pointAdditionEvent));
        }
    }

    private PointAdditionEvent makeEvent(int usePointAmount, int payAmount) {
        return new PointAdditionEvent(orderId, memberId, usePointAmount, payAmount, now);
    }
}
