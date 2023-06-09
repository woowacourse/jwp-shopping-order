package cart.fixture;

import cart.dao.entity.CartItemEntity;
import cart.domain.cart.CartItem;

public class CartItemFixture {
    public static final CartItemEntity 장바구니1_엔티티 = new CartItemEntity(1L, 1L, 1L, 2);
    public static final CartItemEntity 장바구니2_엔티티 = new CartItemEntity(2L, 1L, 2L, 4);
    public static final CartItemEntity 장바구니3_엔티티 = new CartItemEntity(3L, 2L, 3L, 5);
    public static final CartItem 장바구니1 = new CartItem(1L, 2, ProductFixture.지구, MemberFixture.라잇);
    public static final CartItem 장바구니2 = new CartItem(2L, 4, ProductFixture.화성, MemberFixture.라잇);
    public static final CartItem 장바구니3 = new CartItem(3L, 5, ProductFixture.달, MemberFixture.엽토);
}
