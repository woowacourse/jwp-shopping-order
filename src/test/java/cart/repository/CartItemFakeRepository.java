package cart.repository;

import static cart.domain.fixture.OrderFixture.member;
import static cart.domain.fixture.ProductFixture.ANOTHER_PRODUCT;
import static cart.domain.fixture.ProductFixture.OTHER_PRODUCT;
import static cart.domain.fixture.ProductFixture.PRODUCT;

import cart.domain.CartItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemFakeRepository implements CartItemRepository {
    List<CartItem> cartItems = new ArrayList<>(
            List.of(
                    new CartItem(1L, 1, PRODUCT, member),
                    new CartItem(2L, 2, OTHER_PRODUCT, member),
                    new CartItem(3L, 1, ANOTHER_PRODUCT, member)
            )
    );

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst();
    }


}