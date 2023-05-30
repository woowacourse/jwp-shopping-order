package cart.initializer;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.repository.MemberRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final ProductDao productDao;
    private final MemberRepository memberRepository;
    private final CartItemDao cartItemDao;

    public DataInitializer(final ProductDao productDao,
                           final MemberRepository memberRepository,
                           final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.memberRepository = memberRepository;
        this.cartItemDao = cartItemDao;
    }

    @PostConstruct
    public void init() {
        final Long 키키 = productDao.createProduct(new Product("키키", 10_000, "#"));
        final Long 지지 = productDao.createProduct(new Product("지지", 20000, "#"));
        final Long 깃짱 = productDao.createProduct(new Product("깃짱", 13000, "#"));
        final Long 제리 = productDao.createProduct(new Product("제리", 13000, "#"));
        final Long 호이 = productDao.createProduct(new Product("호이", 13000, "#"));
        final Long 주노 = productDao.createProduct(new Product("주노", 13000, "#"));
        final Long 다이진 = productDao.createProduct(new Product("다이진", 13000, "#"));
        final Long 깃짱코딩 = productDao.createProduct(new Product("깃짱코딩", 1393900020, "#"));
        final Long 링링 = productDao.createProduct(new Product("링링", 13000, "#"));
        final Long 글로 = productDao.createProduct(new Product("글로", 13000, "#"));
        final Long 이리내 = productDao.createProduct(new Product("이리내", 13000, "#"));

        final Member savedMember1 = memberRepository.save(new Member("a@a.com", "1234"));
        final Member savedMember2 = memberRepository.save(new Member("b@b.com", "1234"));
        final Member savedMember3 = memberRepository.save(new Member("ringlo@email.com", "ringlo1010235"));

        cartItemDao.save(new CartItem(savedMember1, productDao.getProductById(키키)));
        cartItemDao.save(new CartItem(savedMember2, productDao.getProductById(지지)));
        cartItemDao.save(new CartItem(savedMember3, productDao.getProductById(깃짱)));
        cartItemDao.save(new CartItem(savedMember2, productDao.getProductById(제리)));
        cartItemDao.save(new CartItem(savedMember1, productDao.getProductById(호이)));
        cartItemDao.save(new CartItem(savedMember3, productDao.getProductById(주노)));
        cartItemDao.save(new CartItem(savedMember1, productDao.getProductById(다이진)));
        cartItemDao.save(new CartItem(savedMember3, productDao.getProductById(깃짱코딩)));
        cartItemDao.save(new CartItem(savedMember3, productDao.getProductById(링링)));
        cartItemDao.save(new CartItem(savedMember2, productDao.getProductById(글로)));
        cartItemDao.save(new CartItem(savedMember2, productDao.getProductById(이리내)));
    }
}
