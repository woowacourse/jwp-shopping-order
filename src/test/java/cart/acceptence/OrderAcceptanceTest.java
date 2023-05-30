package cart.acceptence;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.CartItemResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static cart.acceptence.fixtures.ProductFixtures.치킨_10000원;
import static cart.acceptence.fixtures.ProductFixtures.피자_15000원;
import static cart.acceptence.steps.CartItemSteps.장바구니_아이템_추가_요청;
import static cart.acceptence.steps.CartItemSteps.장바구니_조회_요청;
import static cart.acceptence.steps.OrderSteps.주문_등록_요청;
import static cart.acceptence.steps.ProductSteps.상품_추가하고_아이디_반환;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 관리 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

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
    class 주문을_등록할_때 {

        @Test
        void 정상_요청이면_성공적으로_등록한다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 치킨_아이디 = 상품_추가하고_아이디_반환(치킨_10000원);
            장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);
            장바구니_아이템_추가_요청(등록된_사용자1, 치킨_아이디);
            List<OrderItemDto> 장바구니 = 장바구니_조회_요청(등록된_사용자1).jsonPath()
                    .getList(".", CartItemResponse.class)
                    .stream()
                    .map(장바구니_아이템 -> new OrderItemDto(장바구니_아이템.getId(), 장바구니_아이템.getQuantity()))
                    .collect(Collectors.toList());

            // when
            ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(등록된_사용자1, new OrderRequest(28_000L, 장바구니));

            // then
            assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(주문_등록_결과.header("Location")).isNotBlank();
        }

    }

}
