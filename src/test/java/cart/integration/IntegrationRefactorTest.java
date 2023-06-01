package cart.integration;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IntegrationRefactorTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MemberDao memberdao;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CartItemDao cartItemDao;

    @Autowired
    protected OrderRepository orderRepository;
}
