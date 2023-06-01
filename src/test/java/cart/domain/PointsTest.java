package cart.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointsTest {

    private final long memberId = 1L;
    private final long orderId = 1L;
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void 포인트가_충분하지_않은지_판별한다() {
        // given
        int usePointAmount = 1000;
        PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
        PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
            now.plusMinutes(1));
        PointUsage minus201 = new PointUsage(1L, memberId, orderId, 1L, 201);
        Points points = new Points(List.of(plus500, plus700), List.of(minus201));

        // when
        boolean isNotEnough = points.hasNotEnoughPoint(usePointAmount);

        // then
        assertThat(isNotEnough).isTrue();
    }

    @Nested
    class 남은_포인트_중_사용할_포인트를_오래된_것부터_우선_선별한다 {
        @Test
        void 포인트가_분할_없이_사용되는_경우() {
            // 500 + 700 적립 후 201 + 299를 사용한 상황에서 700을 추가 사용하는 경우
            // given
            int usePointAmount = 700;
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus201 = new PointUsage(1L, memberId, orderId, 1L, 201);
            PointUsage minus299 = new PointUsage(2L, memberId, orderId, 1L, 299);
            Points points = new Points(List.of(plus500, plus700), List.of(minus201, minus299));

            // when
            List<PointUsage> pointsToBeUsed = points.findPointsToBeUsed(usePointAmount);

            // then
            assertAll(() -> {
                assertThat(pointsToBeUsed.size()).isOne();
                assertThat(pointsToBeUsed.get(0).getPointAdditionId()).isEqualTo(2L);
                assertThat(pointsToBeUsed.get(0).getAmount()).isEqualTo(700);
            });
        }

        @Test
        void 일부만_쓴_포인트의_잔액까지만_사용하는_경우() {
            // 500, 700 적립 후 200만 사용한 상태에서 300을 사용한다 (500은 다 쓰이고 700은 사용되지 않는다)
            // given
            int usePointAmount = 300;
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus200 = new PointUsage(1L, memberId, orderId, 1L, 200);
            Points points = new Points(List.of(plus500, plus700), List.of(minus200));

            // when
            List<PointUsage> pointsToBeUsed = points.findPointsToBeUsed(usePointAmount);

            // then
            assertAll(() -> {
                assertThat(pointsToBeUsed.size()).isOne();
                assertThat(pointsToBeUsed.get(0).getPointAdditionId()).isEqualTo(1L);
                assertThat(pointsToBeUsed.get(0).getAmount()).isEqualTo(usePointAmount);
            });
        }

        @Test
        void 일부만_쓴_포인트의_잔액과_다음_포인트의_일부만_사용하는_경우() {
            // 500, 700 적립 후 200만 사용한 상태에서 550을 사용한다 (500은 200 + 300으로 나눠 쓰이고 700은 250만 사용된다)
            // given
            int usePointAmount = 550;
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus200 = new PointUsage(1L, memberId, orderId, 1L, 200);
            Points points = new Points(List.of(plus500, plus700), List.of(minus200));

            // when
            List<PointUsage> pointsToBeUsed = points.findPointsToBeUsed(usePointAmount);

            // then
            assertAll(() -> {
                assertThat(pointsToBeUsed.size()).isEqualTo(2);
                assertThat(pointsToBeUsed.get(0).getPointAdditionId()).isEqualTo(1L);
                assertThat(pointsToBeUsed.get(0).getAmount()).isEqualTo(300);
                assertThat(pointsToBeUsed.get(1).getPointAdditionId()).isEqualTo(2L);
                assertThat(pointsToBeUsed.get(1).getAmount()).isEqualTo(250);
            });
        }

        @Test
        void 일부만_쓴_포인트의_잔액과_다음_포인트의_전액을_사용하는_경우() {
            // 500, 700 적립 후 200만 사용한 상태에서 1000을 사용한다 (500은 200 + 300으로 나눠 쓰이고 700은 모두 사용된다)
            // given
            int usePointAmount = 1000;
            PointAddition plus500 = new PointAddition(1L, memberId, orderId, 500, now, now);
            PointAddition plus700 = new PointAddition(2L, memberId, orderId, 700, now.plusMinutes(1),
                now.plusMinutes(1));
            PointUsage minus200 = new PointUsage(1L, memberId, orderId, 1L, 200);
            Points points = new Points(List.of(plus500, plus700), List.of(minus200));

            // when
            List<PointUsage> pointsToBeUsed = points.findPointsToBeUsed(usePointAmount);

            // then
            assertAll(() -> {
                assertThat(pointsToBeUsed.size()).isEqualTo(2);
                assertThat(pointsToBeUsed.get(0).getPointAdditionId()).isEqualTo(1L);
                assertThat(pointsToBeUsed.get(0).getAmount()).isEqualTo(300);
                assertThat(pointsToBeUsed.get(1).getPointAdditionId()).isEqualTo(2L);
                assertThat(pointsToBeUsed.get(1).getAmount()).isEqualTo(700);
            });
        }
    }
}
