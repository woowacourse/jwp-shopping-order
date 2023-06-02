package cart.support;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemTestSupport {

    private static Integer defaultQuantity = 1;

    private final CartItemDao cartItemDao;
    private final ProductTestSupport productTestSupport;
    private final MemberTestSupport memberTestSupport;

    public CartItemTestSupport(final CartItemDao cartItemDao, final ProductTestSupport productTestSupport,
                               final MemberTestSupport memberTestSupport) {
        this.cartItemDao = cartItemDao;
        this.productTestSupport = productTestSupport;
        this.memberTestSupport = memberTestSupport;
    }

    public CartItemBuilder builder() {
        return new CartItemBuilder();
    }

    public final class CartItemBuilder {

        private Long id;
        private Integer quantity;
        private Product product;
        private Member member;

        public CartItemBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public CartItemBuilder quantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartItemBuilder product(final Product product) {
            this.product = product;
            return this;
        }

        public CartItemBuilder member(final Member member) {
            this.member = member;
            return this;
        }

        public CartItem build() {
            CartItem cartItem = make();
            Long cartItemId = cartItemDao.insert(cartItem);
            return new CartItem(cartItemId, cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
        }

        public CartItem make() {
            return new CartItem(
                    id == null ? null : id,
                    quantity == null ? defaultQuantity : quantity,
                    product == null ? productTestSupport.builder().build() : product,
                    member == null ? memberTestSupport.builder().build() : member);
        }
    }
}
