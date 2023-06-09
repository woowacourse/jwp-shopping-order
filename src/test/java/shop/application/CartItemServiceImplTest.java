package shop.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shop.application.cart.CartItemService;
import shop.application.cart.dto.CartDto;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.domain.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class CartItemServiceImplTest extends ServiceTest {
    private Long pizzaId;
    private Long chickenId;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemService cartItemService;

    @BeforeEach
    void setUp() {
        memberRepository.save(ServiceTestFixture.member);
        pizzaId = productRepository.save(ServiceTestFixture.pizza);
        chickenId = productRepository.save(ServiceTestFixture.chicken);
    }

    @DisplayName("상품을 장바구니에 추가할 수 있다.")
    @Test
    void addCartItemTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());

        //when
        cartItemService.add(findMember, pizzaId);

        //then
        List<CartDto> carts = cartItemService.findByMember(findMember);

        assertThat(carts.size()).isEqualTo(1);
        CartDto cartDto = carts.get(0);
        assertThat(cartDto.getQuantity()).isEqualTo(1);
        assertThat(cartDto.getProduct().getName()).isEqualTo(ServiceTestFixture.pizza.getName());
        assertThat(cartDto.getProduct().getPrice()).isEqualTo(ServiceTestFixture.pizza.getPrice());
    }

    @DisplayName("장바구니에 담긴 상품의 수량을 수정할 수 있다.")
    @Test
    void updateQuantityTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());
        Long cartItemId = cartItemService.add(findMember, pizzaId);
        CartDto cartItemBeforeUpdateQuantity = cartItemService.findByMember(findMember).get(0);
        assertThat(cartItemBeforeUpdateQuantity.getQuantity()).isEqualTo(1);
        //when
        cartItemService.updateQuantity(findMember, cartItemId, 10);

        //then
        List<CartDto> carts = cartItemService.findByMember(findMember);

        assertThat(carts.size()).isEqualTo(1);
        CartDto cartDto = carts.get(0);
        assertThat(cartDto.getQuantity()).isEqualTo(10);
        assertThat(cartDto.getProduct().getName()).isEqualTo(ServiceTestFixture.pizza.getName());
        assertThat(cartDto.getProduct().getPrice()).isEqualTo(ServiceTestFixture.pizza.getPrice());
    }

    @DisplayName("장바구니에 담긴 상품을 삭제할 수 있다.")
    @Test
    void deleteCartItemTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());
        Long cartItemId = cartItemService.add(findMember, pizzaId);
        assertThat(cartItemService.findByMember(findMember).size()).isEqualTo(1);

        //when
        cartItemService.remove(findMember, cartItemId);

        //then
        assertThat(cartItemService.findByMember(findMember).size()).isEqualTo(0);
    }

    @DisplayName("장바구니에 담긴 상품 여러 개를 한 번에 삭제할 수 있다.")
    @Test
    void deleteCartItemsTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());
        Long cartItemIdOfPizza = cartItemService.add(findMember, pizzaId);
        Long cartItemIdOfChicken = cartItemService.add(findMember, chickenId);
        assertThat(cartItemService.findByMember(findMember).size()).isEqualTo(2);

        //when
        cartItemService.removeItems(findMember, List.of(cartItemIdOfPizza, cartItemIdOfChicken));

        //then
        assertThat(cartItemService.findByMember(findMember).size()).isEqualTo(0);
    }
}
