package cart.domain.point;

import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberPointsTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private MemberPoints memberPoints;

    @BeforeEach
    void setUp() {
        Member member = new Member(1L, "test@email.com", "testPassword");

        // 포인트 사용 순서는 유효기간에 따라 3L -> 1L -> 2L이다
        List<Point> points = List.of(
                new Point(1L, 100, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2023-06-01 12:12:12", formatter)),
                new Point(2L, 300, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2023-06-06 12:12:12", formatter)),
                new Point(3L, 200, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-29 12:12:12", formatter)),
                new Point(4L, 1000, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2030-05-29 12:12:12", formatter))
        );
        memberPoints = new MemberPoints(member, points);
    }

    @DisplayName("여러 포인트를 사용하되 각 포인트가 남지 않도록 사용하는 경우")
    @Test
    void 포인트_사용시_포인트가_남지_않게_사용하는_경우() {
        // given
        int usedPoint = 300;

        // when
        List<UsedPoint> usedPoints = memberPoints.usedPoint(usedPoint);

        // then
        assertAll(
                () -> assertThat(usedPoints).usingRecursiveFieldByFieldElementComparator()
                                            .contains(new UsedPoint(3L, 200), new UsedPoint(1L, 100)),
                () -> assertThat(memberPoints.getPoints()).usingRecursiveFieldByFieldElementComparator()
                                                          .contains(
                                                                  new Point(1L, 0,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-06-01 12:12:12", formatter)),
                                                                  new Point(2L, 300,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-06-06 12:12:12", formatter)),
                                                                  new Point(3L, 0,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-05-29 12:12:12", formatter))
                                                          )
        );
    }

    @DisplayName("여러 포인트를 사용하되 특정 포인트는 일정 금액이 남는 경우")
    @Test
    void 포인트_사용시_특정_포인트는_남는_경우() {
        // given
        int usedPoint = 400;

        // when
        List<UsedPoint> usedPoints = memberPoints.usedPoint(usedPoint);

        // then
        assertAll(
                () -> assertThat(usedPoints).usingRecursiveFieldByFieldElementComparator()
                                            .contains(new UsedPoint(3L, 200),
                                                    new UsedPoint(1L, 100),
                                                    new UsedPoint(2L, 100)),
                () -> assertThat(memberPoints.getPoints()).usingRecursiveFieldByFieldElementComparator()
                                                          .contains(
                                                                  new Point(1L, 0,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-06-01 12:12:12", formatter)),
                                                                  new Point(2L, 200,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-06-06 12:12:12", formatter)),
                                                                  new Point(3L, 0,
                                                                          LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                                                                          LocalDateTime.parse("2023-05-29 12:12:12", formatter))
                                                          )
        );
    }

    @DisplayName("전체 포인트를 반환한다")
    @Test
    void getUsablePoints() {
        // when
        int usablePoints = memberPoints.getUsablePoints();

        // then
        assertThat(usablePoints).isEqualTo(600);
    }

    @DisplayName("30일 이하로 남은 포인트를 조회한다")
    @Test
    void getToBeExpiredPoints() {
        // when
        int toBeExpiredPoints = memberPoints.getToBeExpiredPoints();

        // then
        assertThat(toBeExpiredPoints).isEqualTo(600);
    }
}
