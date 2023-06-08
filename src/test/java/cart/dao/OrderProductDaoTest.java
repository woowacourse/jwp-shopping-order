package cart.dao;

import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.SavedPoint;
import cart.domain.order.UsedPoint;
import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.exception.notfound.OrderNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import({OrderDao.class, OrderProductDao.class, ProductDao.class})
public class OrderProductDaoTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderProductDao orderProductDao;
    @Autowired
    private ProductDao productDao;

    private Long orderId;
    private Long productId1;
    private Long productId2;

    @BeforeEach
    void setUp() {
        final Member member = new Member(1L, new MemberEmail("a@a.com"), null);
        final Order order = new Order(member, new UsedPoint(1000), new SavedPoint(500), new DeliveryFee(3000));
        final Product product1 = new Product(new ProductName("초콜릿"), new ProductPrice(500), new ProductImageUrl("초콜릿URL"));
        final Product product2 = new Product(new ProductName("사탕"), new ProductPrice(1000), new ProductImageUrl("사탕URL"));
        orderId = orderDao.insert(order);
        productId1 = productDao.insert(product1);
        productId2 = productDao.insert(product2);
    }

    @DisplayName("주문한 상품을 삽입하고 조회할 수 있다.")
    @Test
    void insertAndFind() {
        // given
        final Product product1 = productDao.findById(productId1).orElseThrow(ProductNotFoundException::new);
        final Product product2 = productDao.findById(productId2).orElseThrow(ProductNotFoundException::new);
        final Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);
        final List<OrderProduct> orderProducts = List.of(
                new OrderProduct(1L, order,
                        product1.getId(),
                        product1.getProductName(),
                        product1.getProductPrice(),
                        product1.getProductImageUrl(),
                        new Quantity(3)
                ),
                new OrderProduct(2L, order,
                        product2.getId(),
                        product2.getProductName(),
                        product2.getProductPrice(),
                        product2.getProductImageUrl(),
                        new Quantity(1)
                )
        );

        // when
        orderProductDao.insertAll(orderProducts);
        final List<OrderProduct> findOrderProducts = orderProductDao.findAllByOrderId(order.getId());

        // then
        assertAll(
                () -> assertThat(findOrderProducts).hasSize(2),
                () -> assertThat(findOrderProducts.get(0).getOrderId()).isEqualTo(orderId),
                () -> assertThat(findOrderProducts.get(0).getProductName()).isEqualTo(new ProductName("초콜릿")),
                () -> assertThat(findOrderProducts.get(0).getQuantity()).isEqualTo(new Quantity(3)),
                () -> assertThat(findOrderProducts.get(1).getOrderId()).isEqualTo(orderId),
                () -> assertThat(findOrderProducts.get(1).getProductName()).isEqualTo(new ProductName("사탕")),
                () -> assertThat(findOrderProducts.get(1).getQuantity()).isEqualTo(new Quantity(1))
        );
    }

}
