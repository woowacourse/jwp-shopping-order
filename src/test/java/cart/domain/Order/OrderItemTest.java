package cart.domain.Order;

import cart.domain.Product.Price;
import cart.domain.Product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemTest {

    @Test
    @DisplayName("수량에 따른 상품의 가격을 계산할 수 있다.")
    void calculatePrice() {
        // given
        Product product = new Product(1L, "치킨", 10000, "tmpImg");
        OrderItem orderItem = new OrderItem(product, 2);

        // when
        Price price = orderItem.totalPrice();

        // then
        assertThat(price).isEqualTo(new Price(20000));
    }

}