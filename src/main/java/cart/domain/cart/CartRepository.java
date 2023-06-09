package cart.domain.cart;

public interface CartRepository {

	Long save(Cart cart);

	Cart findByMemberId(Long memberId);

}
