package cart.service;

import cart.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.controller.dto.request.CartItemRequest;
import cart.controller.dto.response.CartItemResponse;
import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.entity.CartItemEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());
        return cartItemEntities.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItemEntity(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id);
        cartItemEntity.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItemEntity.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItemEntity);
    }

    public void remove(Member member, Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id);
        cartItemEntity.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
