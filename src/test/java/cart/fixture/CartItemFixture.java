package cart.fixture;

import cart.domain.cartitem.CartItem;

import static cart.fixture.MemberFixture.레오_ID포함;
import static cart.fixture.ProductFixture.*;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemFixture {

    public static final CartItem 레오_레오배변패드 = new CartItem(5, 배변패드_ID포함, 레오_ID포함);
    public static final CartItem 레오_비버꼬리요리 = new CartItem(1, 꼬리요리_ID포함, 레오_ID포함);
    public static final CartItem 레오_디노통구이 = new CartItem(1, 통구이_ID포함, 레오_ID포함);

}
