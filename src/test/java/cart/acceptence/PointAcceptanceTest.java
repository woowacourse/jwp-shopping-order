package cart.acceptence;

import cart.dao.MemberDao;
import cart.domain.Member;
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
    private Member 등록된_사용자1;
    private Member 등록된_사용자2;

    @BeforeEach
    void setUp() {
        super.setUp();

        등록된_사용자1 = memberDao.getMemberById(1L);
        등록된_사용자2 = memberDao.getMemberById(2L);
    }

    @Nested
    class 포인트를_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // when
            ExtractableResponse<Response> 포인트_조회_결과 = 포인트_조회_요청(등록된_사용자1);

            // then
            assertThat(포인트_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }
}
