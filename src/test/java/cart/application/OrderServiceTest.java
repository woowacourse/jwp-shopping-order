package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private ProductDao productDao;

    @DisplayName("주문할 수 있다")
    @Test
    void order() {
        //given
        memberDao.addMember(new Member("abc@naver.com", "123"));
        final Member member = memberDao.getMemberByEmail("abc@naver.com");
        final Long productId = productDao.createProduct(new Product("예비군", 200000, "image"));
        final Product product = productDao.getProductById(productId);
        final Long cartItemId = cartItemDao.save(new CartItem(member, product));

        //when
        final Long orderId = orderService.save(member, new OrderRequest(List.of(cartItemId)));
        final OrderResponse persisted = orderService.findById(orderId);
        final List<CartItemResponse> items = persisted.getCartItems();

        //then
        assertSoftly(soft -> {
            assertThat(items).hasSize(1);
            assertThat(items.get(0).getId()).isNull();
            assertThat(items.get(0).getQuantity()).isOne();
            assertThat(items.get(0).getProduct().getName()).isEqualTo("예비군");
        });
    }
}
