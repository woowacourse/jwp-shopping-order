package cart.acceptence;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.exception.CartItemIdExceptionResponse;
import cart.dto.response.exception.ExceptionResponse;
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
import static cart.acceptence.steps.OrderSteps.주문_등록하고_아이디_반환;
import static cart.acceptence.steps.OrderSteps.주문_목록_조회_요청;
import static cart.acceptence.steps.OrderSteps.주문_상세_조회_요청;
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

        @Test
        void 등록되지_않은_상품이_존재하면_주문할_수_없다() {
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
            long 존재하지_않는_장바구니_아이템_아이디 = 5959L;
            장바구니.add(new OrderItemDto(존재하지_않는_장바구니_아이템_아이디, 1));

            // when
            ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(등록된_사용자1, new OrderRequest(28_000L, 장바구니));

            // then
            assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(주문_등록_결과.jsonPath().getObject("payload", CartItemIdExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new CartItemIdExceptionResponse(
                                    "등록되지 않은 상품이 포함되어 있습니다. 다시 한번 확인해주세요",
                                    List.of(존재하지_않는_장바구니_아이템_아이디)
                            )
                    );
        }

        @Test
        void 총_가격이_일치하지_않으면_주문할_수_없다() {
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
            ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(등록된_사용자1, new OrderRequest(29_000L, 장바구니));

            // then
            assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(주문_등록_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("상품 정보에 변동사항이 존재합니다. 금액을 다시 한번 확인해주세요"));
        }

        @Test
        void 타인의_장바구니_정보로_주문할_수_없다() {
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
            ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(등록된_사용자2, new OrderRequest(28_000L, 장바구니));

            assertThat(주문_등록_결과.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
            assertThat(주문_등록_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("잘못된 요청입니다"));
        }

    }

    @Nested
    class 주문_목록을_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 치킨_아이디 = 상품_추가하고_아이디_반환(치킨_10000원);
            장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);
            장바구니_아이템_추가_요청(등록된_사용자1, 치킨_아이디);
            List<OrderItemDto> 장바구니 = 장바구니_조회_요청(등록된_사용자1).jsonPath()
                    .getList(".", CartItemResponse.class)
                    .stream()
                    .map(장바구니_아이템 -> new OrderItemDto(장바구니_아이템.getId(), 장바구니_아이템.getQuantity()))
                    .collect(Collectors.toList());
            long 주문_아이디 = 주문_등록하고_아이디_반환(등록된_사용자1, new OrderRequest(28_000L, 장바구니));
            System.out.println(주문_아이디);

            // when
            ExtractableResponse<Response> 주문_목록_조회_결과 = 주문_목록_조회_요청(등록된_사용자1);
            List<OrderResponse> 주문_목록 = 주문_목록_조회_결과.jsonPath().getList(".", OrderResponse.class);

            // then
            assertThat(주문_목록_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(주문_목록.get(0).getOrderId()).isEqualTo(주문_아이디);
            assertThat(주문_목록.get(0).getTotalPrice()).isEqualTo(28_000L);
            assertThat(주문_목록.get(0).getOrderDetails().size()).isEqualTo(2);
        }
    }

    @Nested
    class 특정_주문의_상세_정보를_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 치킨_아이디 = 상품_추가하고_아이디_반환(치킨_10000원);
            장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);
            장바구니_아이템_추가_요청(등록된_사용자1, 치킨_아이디);
            List<OrderItemDto> 장바구니 = 장바구니_조회_요청(등록된_사용자1).jsonPath()
                    .getList(".", CartItemResponse.class)
                    .stream()
                    .map(장바구니_아이템 -> new OrderItemDto(장바구니_아이템.getId(), 장바구니_아이템.getQuantity()))
                    .collect(Collectors.toList());
            long 주문_아이디 = 주문_등록하고_아이디_반환(등록된_사용자1, new OrderRequest(28_000L, 장바구니));
            System.out.println(주문_아이디);

            // when
            ExtractableResponse<Response> 주문_상세_조회_결과 = 주문_상세_조회_요청(등록된_사용자1, 주문_아이디);
            OrderResponse 주문_상세 = 주문_상세_조회_결과.jsonPath().getObject(".", OrderResponse.class);

            // then
            assertThat(주문_상세_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(주문_상세.getOrderId()).isEqualTo(주문_아이디);
            assertThat(주문_상세.getTotalPrice()).isEqualTo(28_000L);
        }
    }

}
