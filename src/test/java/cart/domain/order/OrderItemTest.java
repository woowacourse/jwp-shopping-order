package cart.domain.order;

import cart.domain.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    @Test
    public void 가격을_계산한다() {
        Product product = new Product("테스트상품", 1000, "이미지주소");
        OrderItem orderItem = new OrderItem(1L, product, 3);

        int totalPrice = orderItem.calculatePrice();

        assertThat(totalPrice).isEqualTo(3000);
    }
}
