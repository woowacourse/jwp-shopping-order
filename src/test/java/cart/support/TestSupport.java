package cart.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestSupport {

    @Autowired
    protected MemberTestSupport memberTestSupport;
    @Autowired
    protected ProductTestSupport productTestSupport;
    @Autowired
    protected CartItemTestSupport cartItemTestSupport;
    @Autowired
    protected OrderHistoryTestSupport orderHistoryTestSupport;
    @Autowired
    protected OrderItemTestSupport orderItemTestSupport;
}
