package cart.application;

import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.point.Point;
import cart.domain.point.PointRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.TimestampFixture.*;

@SpringBootTest
public class PointServiceTest {

    private Member 하디_멤버;
    private Point 포인트_1;
    private Point 포인트_2;
    private Point 포인트_3;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointService pointService;

    @BeforeEach
    void setUp() {
        memberRepository.addMember(하디);
        하디_멤버 = memberRepository.findMemberByEmail(하디.getEmail()).get();
        포인트_1 = new Point(300L, 300L, 하디_멤버, 만료일_1, 생성일_1);
        포인트_2 = new Point(1000L, 50L, 하디_멤버, 만료일_2, 생성일_2);
        포인트_3 = new Point(40L, 30L, 하디_멤버, 만료일_3, 생성일_3);
        pointRepository.createPoint(포인트_1);
        pointRepository.createPoint(포인트_2);
        pointRepository.createPoint(포인트_3);
    }

    @Transactional
    @ParameterizedTest
    @CsvSource(value = {"0, 300, 50, 30", "29, 300, 50, 1", "30, 300, 50, 0", "250, 80, 50, 0", "330, 0, 50, 0", "370, 0, 10, 0", "380, 0, 0, 0"}, delimiter = ',')
    void 포인트를_사용한다(long 사용_포인트, long 포인트_1_남은_포인트, long 포인트_2_남은_포인트, long 포인트_3_남은_포인트) {
        // given

        // when
        pointService.usePoint(하디_멤버, 생성일_3, 사용_포인트);
        List<Point> points = pointRepository.findAllAvailablePointsByMemberId(하디_멤버.getId(), 생성일_3);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        for (Point point : points) {
            if (point.getExpiredAt().equals(포인트_1.getExpiredAt())) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_1_남은_포인트);
                continue;
            }
            if (point.getExpiredAt().equals(포인트_2.getExpiredAt())) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_2_남은_포인트);
                continue;
            }
            if (point.getExpiredAt().equals(포인트_3.getExpiredAt())) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_3_남은_포인트);
            }
        }
        softAssertions.assertAll();
    }
}
