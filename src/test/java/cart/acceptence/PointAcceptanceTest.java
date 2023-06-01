package cart.acceptence;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.response.exception.ExceptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static cart.acceptence.steps.PointSteps.포인트_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("포인트 조회 기능")
public class PointAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;
    private Member 등록된_사용자;

    @BeforeEach
    void setUp() {
        super.setUp();

        등록된_사용자 = memberDao.getMemberById(1L);
    }

    @Nested
    class 포인트를_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // when
            ExtractableResponse<Response> 포인트_조회_결과 = 포인트_조회_요청(등록된_사용자);

            // then
            assertThat(포인트_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(포인트_조회_결과.jsonPath().getLong("usablePoint")).isNotNull();
        }

        @Test
        void 존재하지_않는_회원이면_조회할_수_없다() {
            // given
            Member 잘못된_사용자 = new Member(등록된_사용자.getId(), 등록된_사용자.getEmail(), 등록된_사용자.getPassword() + "illegal");

            // when
            ExtractableResponse<Response> 포인트_조회_결과 = 포인트_조회_요청(잘못된_사용자);

            // then
            assertThat(포인트_조회_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(포인트_조회_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("존재하지 않는 회원입니다"));
        }
    }
}
