package cart.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderCreateRequest;

@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
@SpringBootTest
public class ServiceTest {

	protected static final String EMAIL = "a@a.com";
	protected static final String PASSWORD = "1234";
	protected static final CartItemRequest CART_ITEM_REQUEST_1 = new CartItemRequest(1L, 1L, 2);
	protected static final CartItemRequest CART_ITEM_REQUEST_2 = new CartItemRequest(2L, 2L, 4);
	protected static final OrderCreateRequest ORDER_CREATE_REQUEST = new OrderCreateRequest(200, List.of(
		CART_ITEM_REQUEST_1, CART_ITEM_REQUEST_2));

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected ProductService productService;

	@Autowired
	protected MemberDao memberDao;

	@Autowired
	protected ProductDao productDao;

	@Autowired
	protected CartItemDao cartItemDao;
}
