package cart.dao.rowmapper;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;

public final class CartItemRowMapper {

    private CartItemRowMapper() {
    }

    public static final RowMapper<CartItem> joinMemberAndProduct = (rs, rowNum) -> {
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");

        long memberId = rs.getLong("member.id");
        String email = rs.getString("member.email");
        String password = rs.getString("member.password");
        BigDecimal money = rs.getBigDecimal("member.money");
        BigDecimal point = rs.getBigDecimal("member.point");
        Member member = new Member(memberId, email, password, Money.from(money), Money.from(point));

        Long productId = rs.getLong("product.id");
        String name = rs.getString("product.name");
        int price = rs.getInt("product.price");
        String imageUrl = rs.getString("product.image_url");
        Product product = new Product(productId, name, price, imageUrl);

        return new CartItem(cartItemId, product, member, quantity);
    };
}
