package cart.discountpolicy.application;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import cart.member.Member;
import cart.product.Product;
import cart.product.application.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DiscountPolicyServiceTest {

    @Autowired
    private DiscountPolicyRepository discountPolicyRepository;

    @Autowired
    private DiscountPolicyService discountPolicyService;

    @Autowired
    private ProductRepository productRepository;

    private Cart cart;
    private Long 피자아이디;
    private Long 치킨아이디;

    @BeforeEach
    void setUp() {
        this.cart = getTestCart();
    }

    public Cart getTestCart() {
        final var 백여우 = new Member(1L, "fox@gmail.com", "1234");
        final var 피자 = new Product("피자", 20_000, "img");
        final var 치킨 = new Product("치킨", 30_000, "img");

        final var 피자아이디 = productRepository.createProduct(피자);
        final var 치킨아이디 = productRepository.createProduct(치킨);

        this.피자아이디 = 피자아이디;
        this.치킨아이디 = 치킨아이디;

        final var 백여우가담은피자 = new CartItem(1L, 피자.getName(), 피자.getPrice(), 3, 피자.getImageUrl(), 피자아이디, 백여우.getId());
        final var 백여우가담은치킨 = new CartItem(2L, 치킨.getName(), 치킨.getPrice(), 2, 피자.getImageUrl(), 치킨아이디, 백여우.getId());
        return new Cart(List.of(백여우가담은피자, 백여우가담은치킨));
    }

    @Test
    @DisplayName("특정 상품에 할인을 적용하겠다는 선언, 할인을 적용할 상품의 목록, 할인 방식, 할인 정도를 넘겨주면 할인 정책이 등록돼요.")
    void saveDiscountPolicyForSpecific() {
        final var discountCondition = DiscountCondition.makeConditionForSpecificProducts(List.of(this.치킨아이디, this.피자아이디), DiscountUnit.PERCENTAGE, 50);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);
        assertThat(discountPolicyId).isNotNull();
    }

    @Test
    @DisplayName("배송비에 할인을 적용하겠다는 선언, 할인 방식, 할인 정도를 넘겨주면 할인 정책이 등록돼요.")
    void saveDiscountPolicyForDelivery() {
        final var discountCondition = DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.ABSOLUTE, 1500);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);
        assertThat(discountPolicyId).isNotNull();
    }

    @Test
    @DisplayName("모든 상품에 할인을 적용하겠다는 선언, 할인 방식, 할인 정도를 넘겨주면 할인 정책이 등록돼요.")
    void saveDiscountPolicyForAll() {
        final var discountCondition = DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 60);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);
        assertThat(discountPolicyId).isNotNull();
    }

    @Test
    @DisplayName("치킨에만 적용되는 할인정책을 적용하면 치킨 가격만 할인돼 있어요.")
    void discountForChicken() {
        final var discountCondition = DiscountCondition.makeConditionForSpecificProducts(List.of(1L), DiscountUnit.PERCENTAGE, 50);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);

        final var discountPolicy = discountPolicyRepository.findById(discountPolicyId);
        discountPolicy.discount(cart);

        assertThat(cart.getCartItems())
                .extracting(CartItem::getDiscountPrice)
                .containsExactly(10_000, 0);
        assertThat(cart.getOriginalDeliveryPrice())
                .isEqualTo(3_000);
    }

    @Test
    @DisplayName("배송비에만 적용되는 할인정책을 적용하면 배송비 가격만 할인돼 있어요.")
    void discountForDelivery() {
        final var discountCondition = DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.ABSOLUTE, 1500);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);

        final var discountPolicy = discountPolicyRepository.findById(discountPolicyId);
        discountPolicy.discount(cart);

        assertThat(cart.getCartItems())
                .extracting(CartItem::getDiscountPrice)
                .containsExactly(0, 0);
        assertThat(cart.getDiscountDeliveryPrice())
                .isEqualTo(1500);
    }

    @Test
    @DisplayName("전체 가격에 적용되는 할인정책을 적용하면 전체 가격이 할인돼 있어요.")
    void discountForAllProduct() {
        final var discountCondition = DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 60);
        final var discountPolicyId = discountPolicyService.savePolicy(discountCondition);

        final var discountPolicy = discountPolicyRepository.findById(discountPolicyId);
        discountPolicy.discount(cart);

        assertThat(cart.getCartItems())
                .extracting(CartItem::getDiscountPrice)
                .containsExactly(12_000, 18_000);
        assertThat(cart.getOriginalDeliveryPrice())
                .isEqualTo(3_000);
    }
}