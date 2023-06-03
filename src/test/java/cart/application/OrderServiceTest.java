package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private ProductDao productDao;

    private Member member;

    @BeforeEach
    void setUp() {
        final MemberEntity memberEntity = memberDao.findById(3L);
        member = Member.from(memberEntity);
    }

    @Test
    void 장바구니에_담긴_상품을_주문할_수_있다() {
        final MemberEntity memberEntity = memberDao.findById(2L);
        member = Member.from(memberEntity);
        // given
        final long productId = productDao.createProduct(
                new ProductEntity("피자", 10_000, "피자사진")
        );
        final long cartItemId = cartItemDao.createCartItem(
                new CartItemEntity(null, member.getId(), productId, 1)
        );

        // when
        final Long orderId = orderService.buy(member, new OrderRequest(List.of(cartItemId)));

        // then
        assertThat(orderId).isNotNull();
    }

    @Test
    void 회원의_모든_주문_목록을_확인할_수_있다() {
        // given
        final long firstProductId = productDao.createProduct(
                new ProductEntity("피자", 10_000, "피자사진")
        );
        final long firstCartItemId = cartItemDao.createCartItem(
                new CartItemEntity(null, member.getId(), firstProductId, 2)
        );
        final long secondProductId = productDao.createProduct(
                new ProductEntity("치킨", 20_000, "치킨사진")
        );
        final long secondCartItemId = cartItemDao.createCartItem(
                new CartItemEntity(null, member.getId(), secondProductId, 3)
        );

        final Long orderId = orderService.buy(member, new OrderRequest(List.of(firstCartItemId, secondCartItemId)));

        // when
        final List<OrderResponse> orderResponses = orderService.selectAll(member);

        // then
        assertAll(
                () -> assertThat(orderResponses).hasSize(1),
                () -> assertThat(orderResponses).extracting(
                        OrderResponse::getOrderId,
                        OrderResponse::getTotalPrice,
                        orderResponse -> orderResponse.getCartItemResponse().size()
                ).contains(
                        tuple(orderId, 80_000, 2)
                )
        );
    }

    @Test
    void id로_주문_목록을_확인할_수_있다() {
        // given
        final long productId = productDao.createProduct(
                new ProductEntity("피자", 10_000, "피자사진")
        );
        final long cartItemId = cartItemDao.createCartItem(
                new CartItemEntity(null, member.getId(), productId, 2)
        );
        final Long orderId = orderService.buy(member, new OrderRequest(List.of(cartItemId)));

        // when
        final OrderResponse orderResponse = orderService.selectOrder(orderId, member);

        // then
        assertAll(
                () -> assertThat(orderResponse.getOrderId()).isEqualTo(orderId),
                () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(20_000),
                () -> assertThat(orderResponse.getCartItemResponse()).hasSize(1)
        );
    }
}