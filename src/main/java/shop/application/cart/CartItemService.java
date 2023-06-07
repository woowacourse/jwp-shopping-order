package shop.application.cart;

import shop.application.cart.dto.CartDto;
import shop.domain.member.Member;

import java.util.List;

public interface CartItemService {
    Long add(Member member, Long productId);

    List<CartDto> findByMember(Member member);

    void updateQuantity(Member member, Long id, Integer quantity);

    void remove(Member member, Long id);

    void removeItems(Member member, List<Long> ids);
}
