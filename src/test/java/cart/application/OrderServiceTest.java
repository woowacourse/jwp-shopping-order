package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemRequest;
import cart.dto.OrderRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    MemberDao memberDao;

    @Autowired
    ProductDao productDao;

    @Test
    void 회원의_주문이_생성되어야_한다() {
        Member member = memberDao.findById(1L).get();
        Long productAId = productDao.save(new Product(null, "A", 1000, "http://image.com/image.png"));
        Long productBId = productDao.save(new Product(null, "B", 2000, "http://image.com/image.png"));
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(productAId, 3),
                new OrderItemRequest(productBId, 5)
        ), 500L);

        Long orderId = orderService.createOrder(orderRequest, member);
        OrderDetailResponse orderDetailResponse = orderService.findOrderById(orderId, member);

        assertAll(
                () -> assertThat(orderDetailResponse.getOrderId()).isEqualTo(orderId),
                () -> assertThat(orderDetailResponse.getTotalPrice()).isEqualTo(13000),
                () -> assertThat(orderDetailResponse.getSpendPoint()).isEqualTo(500),
                () -> assertThat(orderDetailResponse.getSpendPrice()).isEqualTo(12500)
        );
    }

    @Test
    void 주문시_포인트를_사용하면_사용한_포인트는_차감되고_결제_금액의_일부가_포인트로_전환된다() {
        Member member = memberDao.findById(1L).get();
        Long productAId = productDao.save(new Product(null, "A", 1000, "http://image.com/image.png"));
        Long productBId = productDao.save(new Product(null, "B", 2000, "http://image.com/image.png"));
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(productAId, 3),
                new OrderItemRequest(productBId, 5)
        ), 1000L);

        orderService.createOrder(orderRequest, member);
        Member findMember = memberDao.findById(member.getId()).get();

        assertThat(findMember.getPoint().getAmount()).isEqualTo(1200);
    }

    @Test
    void 주문시_가지고_있는_포인트보다_더_많이_사용하려고_하면_예외가_발생한다() {
        Member member = memberDao.findById(1L).get();
        Long productId = productDao.save(new Product(null, "A", 1000, "http://image.com/image.png"));
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(productId, 3)
        ), 1001L);

        assertThatThrownBy(() -> orderService.createOrder(orderRequest, member))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_결제_금액보다_더_많은_포인트를_사용하면_예외가_발생한다() {
        Member member = memberDao.findById(1L).get();
        Long productId = productDao.save(new Product(null, "A", 100, "http://image.com/image.png"));
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(productId, 1)
        ), 101L);

        assertThatThrownBy(() -> orderService.createOrder(orderRequest, member))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
