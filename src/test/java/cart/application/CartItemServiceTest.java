package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartItemDao cartItemDao;

    @Test
    void makeCheckout() {

    }
}
