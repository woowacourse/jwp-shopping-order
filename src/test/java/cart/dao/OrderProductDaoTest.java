package cart.dao;

import static fixture.OrderFixture.주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개;
import static fixture.OrderFixture.주문_유저_1_할인율_쿠폰_치킨_2개;
import static fixture.OrdersProductFixture.주문_상품_치킨_2개;
import static fixture.ProductFixture.상품_샐러드;
import static fixture.ProductFixture.상품_치킨;
import static fixture.ProductFixture.상품_피자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.dto.OrderProductDto;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderProductDaoTest {

    @Autowired
    private OrderProductDao orderProductDao;

    @Test
    @DisplayName("OrdersProduct 저장")
    void insert() {
        OrderProductDto orderProductDto = new OrderProductDto(주문_유저_1_할인율_쿠폰_치킨_2개.getId(), 상품_치킨.getId(), 1);

        Long orderProductId = orderProductDao.insert(orderProductDto);

        OrderProductDto orderProductDtoAfterSave = orderProductDao.findById(orderProductId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(orderProductDtoAfterSave).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderProductDto);
    }

    /**
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 1, 2);
     */
    @Test
    @DisplayName("OrdersProduct 조회하는 기능 테스트")
    void findById() {
        OrderProductDto orderProductDto = orderProductDao.findById(주문_상품_치킨_2개.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(orderProductDto)
                .extracting(OrderProductDto::getId, OrderProductDto::getOrderId, OrderProductDto::getProductId, OrderProductDto::getQuantity)
                .containsExactly(주문_상품_치킨_2개.getId(), 주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 상품_치킨.getId(), 2);
    }

    /**
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 1, 2);
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 2, 2);
     * INSERT INTO orders_product(order_id, product_id, quantity) VALUES (1, 3, 2);
     */
    @Test
    @DisplayName("OrderId 로 OrderProduct 들을 찾는다.")
    void findOrderProductByOrderId() {
        List<OrderProductDto> orderProductDtos = orderProductDao.findByOrderId(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId());

        assertThat(orderProductDtos)
                .extracting(OrderProductDto::getOrderId, OrderProductDto::getProductId, OrderProductDto::getQuantity)
                .containsExactly(
                        tuple(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 상품_치킨.getId(), 2),
                        tuple(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 상품_샐러드.getId(), 2),
                        tuple(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 상품_피자.getId(), 2)
                );
    }

}