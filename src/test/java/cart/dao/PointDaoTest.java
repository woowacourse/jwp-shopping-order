package cart.dao;

import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.dao.point.JdbcTemplatePointDao;
import cart.dao.point.PointDao;
import cart.domain.member.Member;
import cart.domain.point.Point;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.TimestampFixture.*;

@JdbcTest
public class PointDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private PointDao pointDao;

    private Member 하디_멤버;
    private Member 현구막_멤버;

    @BeforeEach
    void setUp() {
        this.memberDao = new JdbcTemplateMemberDao(jdbcTemplate);
        this.pointDao = new JdbcTemplatePointDao(jdbcTemplate);
        memberDao.addMember(하디);
        memberDao.addMember(현구막);
        하디_멤버 = memberDao.findMemberByEmail(하디.getEmail()).get();
        현구막_멤버 = memberDao.findMemberByEmail(현구막.getEmail()).get();
    }

    @Test
    void 포인트를_적립한다() {
        // given
        Point 하디_포인트_1 = new Point(300L, 250L, 하디_멤버, 만료일_1, 생성일_1);

        // when
        Long pointId = pointDao.createPoint(하디_포인트_1);

        // then
        List<Point> points = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), 하디_포인트_1.getCreatedAt());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(points.size()).isEqualTo(1);
        softAssertions.assertThat(points.get(0).getEarnedPoint()).isEqualTo(하디_포인트_1.getEarnedPoint());
        softAssertions.assertThat(points.get(0).getLeftPoint()).isEqualTo(하디_포인트_1.getLeftPoint());
        softAssertions.assertThat(points.get(0).getCreatedAt()).isEqualTo(하디_포인트_1.getCreatedAt());
        softAssertions.assertThat(points.get(0).getExpiredAt()).isEqualTo(하디_포인트_1.getExpiredAt());
        softAssertions.assertThat(points.get(0).getMember()).isEqualTo(하디_포인트_1.getMember());
        softAssertions.assertAll();
    }

    @Test
    void 특정시각_기준으로_만료되지_않은_포인트만_조회된다() {
        // given
        Point 하디_포인트_1 = new Point(300L, 250L, 하디_멤버, 만료일_1, 생성일_1);
        Point 하디_포인트_2 = new Point(400L, 350L, 하디_멤버, 만료일_2, 생성일_2);

        pointDao.createPoint(하디_포인트_1);
        Long 하디_포인트_2_아이디 = pointDao.createPoint(하디_포인트_2);
        Timestamp firstPointExpiredAt = 하디_포인트_1.getExpiredAt();
        Timestamp secondPointCreatedAt = 하디_포인트_2.getCreatedAt();

        // when
        List<Point> points = pointDao.findAllAvailablePointsByMemberId(하디_멤버.getId(), secondPointCreatedAt);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(firstPointExpiredAt).isBefore(secondPointCreatedAt);
        softAssertions.assertThat(points.size()).isEqualTo(1);
        softAssertions.assertThat(points.get(0).getId()).isEqualTo(하디_포인트_2_아이디);
        softAssertions.assertAll();
    }

    @Test
    void 남은_포인트가_0이면_조회되지_않는다() {
        // given
        Point 현구막_포인트_1 = new Point(300L, 0L, 현구막_멤버, 만료일_1, 생성일_1);
        pointDao.createPoint(현구막_포인트_1);

        // when
        List<Point> points = pointDao.findAllAvailablePointsByMemberId(현구막_멤버.getId(), 생성일_1);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(points.size()).isEqualTo(0);
    }

    @Test
    void 남은_포인트를_변경한다() {
        // given
        Point 현구막_포인트_1 = new Point(300L, 300L, 현구막_멤버, 만료일_1, 생성일_1);
        Long pointId = pointDao.createPoint(현구막_포인트_1);
        Point updatePoint = new Point(pointId, 300L, 1L, 현구막_멤버, 만료일_1, 생성일_1);

        // when
        pointDao.updateLeftPoint(updatePoint);

        // then
        List<Point> points = pointDao.findAllAvailablePointsByMemberId(현구막_멤버.getId(), 생성일_1);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(points.size()).isEqualTo(1);
        softAssertions.assertThat(points.get(0).getLeftPoint()).isEqualTo(1L);
        softAssertions.assertAll();
    }
}
