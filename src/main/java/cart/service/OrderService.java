package cart.service;

import cart.controller.dto.request.OrderRequest;
import cart.domain.Member;
import cart.exception.NotOwnerException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.dto.CartItemWithProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public long save(final Member member, final OrderRequest request) {
        final List<CartItemWithProductDto> cartItemWithProductDtos = cartItemRepository.findByIds(request.getCartItems());
        checkOwner(member, cartItemWithProductDtos);
        return 1;
    }

    private void checkOwner(final Member member, final List<CartItemWithProductDto> dtos) {
        final boolean isIdEquals = dtos.stream()
                .map(CartItemWithProductDto::getMemberId)
                .allMatch(member::isIdEquals);
        if (isIdEquals) {
            return;
        }
        throw new NotOwnerException();
    }
}
