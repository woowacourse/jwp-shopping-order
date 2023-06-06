package cart.application.service.cartitem;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.ui.MemberAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static cart.fixture.MemberFixture.레오_ID포함;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CartItemReadServiceTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemReadService cartItemReadService;
    @Autowired
    private CartItemWriteService cartItemWriteService;

    Product chicken;
    Member leo;

    @BeforeEach
    void setUp() {
        Member member = new Member("leo", "leo@leo.com", "32423543");
        Product product = new Product("치킨", 15000, "치킨이미지");
        Long memberId = memberRepository.createMember(member);
        Long productId = productRepository.createProduct(product);

        chicken = new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
        leo = new Member(memberId, member.getName(), member.getEmail(), member.getPassword());
    }

    @Test
    @DisplayName("사용자의 해당하는 장바구니의 정보를 조회한다.")
    void findByMemberTest() {
        // given
        MemberAuth memberAuth = new MemberAuth(레오_ID포함.getId(), "레오", "leo@gmail.com", "leo123");
        // when
        Long cartItemId = cartItemRepository.createCartItem(new CartItem(3, chicken, leo));
        CartResultDto resultDto = cartItemReadService.findByMember(new MemberAuth(leo.getId(), leo.getName(), leo.getEmail(), leo.getPassword()));

        // then
        assertThat(resultDto.getTotalPrice()).isEqualTo(45000);
    }

    @Test
    @DisplayName("사용자의 장바구니에 없으면 빈 리스트 반환")
    void findWhenNonExistItems() {
        MemberAuth memberAuth = new MemberAuth(leo.getId(), leo.getName(), leo.getEmail(), leo.getPassword());
        CartResultDto result = cartItemReadService.findByMember(memberAuth);
        Assertions.assertAll(
                () -> assertThat(result.getTotalPrice()).isEqualTo(0),
                () -> assertThat(result.getCartItemResultDtos()).isEmpty()
        );
    }

    @Test
    @DisplayName("잘못된 사용자 정보로 장바구니 조회 시 예외발생")
    void nonExistMemberTest() {
        MemberAuth memberAuth = new MemberAuth(3L, "eee", "ee@ee.com", "ee");
        assertThatThrownBy(() -> cartItemReadService.findByMember(memberAuth))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 사용자가 없습니다.");
    }

}
