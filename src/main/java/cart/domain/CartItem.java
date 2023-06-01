package cart.domain;

import cart.exception.IllegalMemberException;

public class CartItem {
    private final Long id;
    private final Member member;
    private final Item item;

    public CartItem(Product product, Member member) {
        this(null, member, new Item(product));
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this(id, member, new Item(product, quantity));
    }

    public CartItem(Long id, Member member, Item item) {
        this.id = id;
        this.member = member;
        this.item = item;
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new IllegalMemberException();
        }
    }

    public void changeQuantity(int quantity) {
        item.changeQuantity(quantity);
    }

    public Money calculateCartPrice() {
        return item.calculateItemPrice();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return item.getProduct();
    }

    public int getQuantity() {
        return item.getQuantity();
    }

    public Item getItem() {
        return item;
    }
}
