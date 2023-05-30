package cart.ui;

import cart.application.OrderService;
import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartItemDao cartItemDao;

    //장바구니에 담긴 상품을 주문
    //TODO: 주문된 상품들 장바구니에서 제거

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderCreateRequest orderCreateRequest){
        Long id = orderService.createOrder(member, orderCreateRequest);
        return ResponseEntity.created(URI.create("/orders" + id)).build();
    }
}
