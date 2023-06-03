package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class OrderDaoTest {

    private final Member member = new Member(1L, "test@test.com", "password");
    private final Coupon coupon = new Coupon("name", Amount.of(1_000), Amount.of(10_000), false);

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("주문을 저장한다.")
    void testSave() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());
        final Product product1 = new Product("product1", Amount.of(10_000), "imageUrl1");
        final Product product2 = new Product("product2", Amount.of(20_000), "imageUrl2");
        final Long product1Id = productDao.createProduct(product1);
        final Long product2Id = productDao.createProduct(product2);
        final Product savedProduct1 = new Product(product1Id, "product1", Amount.of(10_000), "imageUrl1");
        final Product savedProduct2 = new Product(product2Id, "product2", Amount.of(20_000), "imageUrl2");
        final Order order = new Order(new Products(List.of(savedProduct1, savedProduct2)),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()), Amount.of(29_000),
            Amount.of(3_000), "address");

        //when
        final Order savedOrder = orderDao.save(order, savedMember.getId());

        //then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("id로 주문을 찾는다.")
    void testFindById() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());
        final Product product1 = new Product("product1", Amount.of(10_000), "imageUrl1");
        final Product product2 = new Product("product2", Amount.of(20_000), "imageUrl2");
        final Long product1Id = productDao.createProduct(product1);
        final Long product2Id = productDao.createProduct(product2);
        final Product savedProduct1 = new Product(product1Id, "product1", Amount.of(10_000), "imageUrl1");
        final Product savedProduct2 = new Product(product2Id, "product2", Amount.of(20_000), "imageUrl2");
        final List<Product> savedProducts = List.of(savedProduct1, savedProduct2);
        final Order order = new Order(new Products(List.of(savedProduct1, savedProduct2)),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()), Amount.of(29_000),
            Amount.of(3_000), "address");
        final Order savedOrder = orderDao.save(order, savedMember.getId());

        //when
        final Order result = orderDao.findById(order.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(result.getId()).isEqualTo(savedOrder.getId());
        assertThat(result.getAddress()).isEqualTo(savedOrder.getAddress());
        assertThat(result.getDeliveryAmount()).isEqualTo(savedOrder.getDeliveryAmount());
        assertThat(result.getTotalAmount()).isEqualTo(savedOrder.getTotalAmount());
        final Products products = result.getProducts();
        for (int index = 0; index < 2; index++) {
            assertThat(products.getValue().get(index).getId()).isEqualTo(savedProducts.get(index).getId());
        }
    }

    @Test
    @DisplayName("회원으로 주문을 찾는다.")
    void testFindByMember() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());
        final Product product1 = new Product("product1", Amount.of(10_000), "imageUrl1");
        final Product product2 = new Product("product2", Amount.of(20_000), "imageUrl2");
        final Long product1Id = productDao.createProduct(product1);
        final Long product2Id = productDao.createProduct(product2);
        final Product savedProduct1 = new Product(product1Id, "product1", Amount.of(10_000), "imageUrl1");
        final Product savedProduct2 = new Product(product2Id, "product2", Amount.of(20_000), "imageUrl2");
        final List<Product> savedProducts = List.of(savedProduct1, savedProduct2);
        final Order order = new Order(new Products(List.of(savedProduct1, savedProduct2)),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()), Amount.of(29_000),
            Amount.of(3_000), "address");
        final Order savedOrder = orderDao.save(order, savedMember.getId());

        //when
        final List<Order> orders = orderDao.findByMember(savedMember);

        //then
        final Order result = orders.get(0);
        assertThat(result.getId()).isEqualTo(savedOrder.getId());
        assertThat(result.getAddress()).isEqualTo(savedOrder.getAddress());
        assertThat(result.getDeliveryAmount()).isEqualTo(savedOrder.getDeliveryAmount());
        assertThat(result.getTotalAmount()).isEqualTo(savedOrder.getTotalAmount());
        final Products products = result.getProducts();
        for (int index = 0; index < 2; index++) {
            assertThat(products.getValue().get(index).getId()).isEqualTo(savedProducts.get(index).getId());
        }
    }
}
