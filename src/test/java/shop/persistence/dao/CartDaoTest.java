package shop.persistence.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.exception.DatabaseException;
import shop.persistence.entity.CartEntity;
import shop.persistence.entity.MemberEntity;
import shop.persistence.entity.ProductEntity;
import shop.persistence.entity.detail.CartItemDetail;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import({CartDao.class, MemberDao.class, ProductDao.class})
class CartDaoTest extends DaoTest {
    private static Long memberId;
    private static Long chickenId;
    private static Long pizzaId;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        memberId = memberDao.insertMember(Data.member);
        chickenId = productDao.insert(Data.chicken);
        pizzaId = productDao.insert(Data.pizza);
    }

    @DisplayName("장바구니에 상품을 담을 수 있다.")
    @Test
    void insertCartTest() {
        //given
        CartEntity cartEntity = new CartEntity(memberId, chickenId, 5);

        //when
        Long cartId = cartDao.insert(cartEntity);

        //then
        CartItemDetail cartItemDetail = cartDao.findById(cartId);

        assertThat(cartItemDetail.getMemberName()).isEqualTo(Data.member.getName());
        assertThat(cartItemDetail.getProductName()).isEqualTo(Data.chicken.getName());
        assertThat(cartItemDetail.getPrice()).isEqualTo(Data.chicken.getPrice());
        assertThat(cartItemDetail.getQuantity()).isEqualTo(5);
    }

    @DisplayName("특정 회원의 장바구니를 조회할 수 있다.")
    @Test
    void findAllCartsByMemberIdTest() {
        //given
        CartEntity cartOfChicken = new CartEntity(memberId, chickenId, 5);
        CartEntity cartOfPizza = new CartEntity(memberId, pizzaId, 1);

        //when
        cartDao.insert(cartOfChicken);
        cartDao.insert(cartOfPizza);

        //then
        List<CartItemDetail> allCartItems = cartDao.findAllByMemberId(memberId);

        assertThat(allCartItems.size()).isEqualTo(2);
        assertThat(allCartItems).extractingResultOf("getMemberName")
                .containsExactlyInAnyOrder(Data.member.getName(), Data.member.getName());
        assertThat(allCartItems).extractingResultOf("getProductName")
                .containsExactlyInAnyOrder(Data.pizza.getName(), Data.chicken.getName());
        assertThat(allCartItems).extractingResultOf("getQuantity")
                .containsExactlyInAnyOrder(1, 5);
    }

    @DisplayName("장바구니에 담긴 상품 수량을 변경할 수 있다.")
    @Test
    void updateQuantityInCart() {
        //given
        CartEntity cartEntity = new CartEntity(memberId, chickenId, 1);
        Long cartId = cartDao.insert(cartEntity);

        CartItemDetail originalCartItem = cartDao.findById(cartId);
        Assertions.assertThat(originalCartItem.getQuantity()).isEqualTo(1);

        //when
        cartDao.updateQuantity(cartId, 5);

        //then
        CartItemDetail changedCartItem = cartDao.findById(cartId);
        Assertions.assertThat(changedCartItem.getQuantity()).isEqualTo(5);
    }

    @DisplayName("장바구니의 상품을 삭제할 수 있다.")
    @Test
    void deleteCartItemByIdTest() {
        //given
        CartEntity cartEntity = new CartEntity(memberId, chickenId, 5);
        Long cartId = cartDao.insert(cartEntity);

        assertDoesNotThrow(() -> cartDao.findById(cartId));
        //when
        cartDao.deleteById(cartId);

        //then
        assertThatThrownBy(() -> cartDao.findById(cartId))
                .isInstanceOf(DatabaseException.IllegalDataException.class);
    }

    @DisplayName("장바구니의 상품 여러개를 한 번에 삭제할 수 있다.")
    @Test
    void deleteCartItemByIdsTest() {
        //given
        CartEntity cartOfChicken = new CartEntity(memberId, chickenId, 5);
        CartEntity cartOfPizza = new CartEntity(memberId, pizzaId, 5);

        Long cartIdOfChicken = cartDao.insert(cartOfChicken);
        Long cartIdOfPizza = cartDao.insert(cartOfPizza);

        assertDoesNotThrow(() -> cartDao.findById(cartIdOfChicken));
        assertDoesNotThrow(() -> cartDao.findById(cartIdOfPizza));
        //when
        cartDao.deleteByIds(List.of(cartIdOfChicken, cartIdOfPizza));

        //then
        assertThatThrownBy(() -> cartDao.findById(cartIdOfChicken))
                .isInstanceOf(DatabaseException.IllegalDataException.class);
        assertThatThrownBy(() -> cartDao.findById(cartIdOfPizza))
                .isInstanceOf(DatabaseException.IllegalDataException.class);
    }

    static class Data {
        static MemberEntity member = new MemberEntity("쥬니", "1234");
        static ProductEntity pizza = new ProductEntity("피자", 20000, "피자.com");
        static ProductEntity chicken = new ProductEntity("치킨", 30000, "치킨.com");
    }
}
