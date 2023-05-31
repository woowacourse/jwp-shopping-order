package cart.ui;

import cart.application.CartItemService;
import cart.application.ProductService;
import cart.domain.Member;
import cart.domain.ProductCartItem;
import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;
import cart.dto.HomePagingResponse;
import cart.dto.ProductCartItemResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;
    private final CartItemService cartItemService;

    public ProductApiController(final ProductService productService,
                                final CartItemService cartItemService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<Product> products = productService.getAllProducts();

        final List<ProductResponse> responses = products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/cart-items")
    public ResponseEntity<HomePagingResponse> getHomePagingProduct(final Member member,
                                                                   @RequestParam final Long lastId,
                                                                   @RequestParam final int pageItemCount) {
        final List<Product> products = productService.getProductsInPaging(lastId, pageItemCount);

        final List<ProductCartItem> productCartItems = productService.getProductCartItemsByProduct(member, products);
        final boolean isLast = productService.hasLastProduct(lastId, pageItemCount);

        final List<ProductCartItemResponse> responses = productCartItems.stream()
                .map(ProductCartItemResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(HomePagingResponse.of(responses, isLast));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable final Long id) {
        final Product product = productService.getProductById(id);
        return ResponseEntity.ok(ProductResponse.of(product));
    }

    @GetMapping("/{productId}/cart-items")
    public ResponseEntity<ProductCartItemResponse> getProductCartItemByProductId(@PathVariable final Long productId,
                                                                                 final Member member) {
        final Product product = productService.getProductById(productId);
        final CartItem cartItem = cartItemService.findByMemberAndProduct(member, product);

        final ProductCartItemResponse productCartItemResponse = ProductCartItemResponse.of(product, cartItem);

        return ResponseEntity.ok(productCartItemResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody final ProductRequest productRequest) {
        final Product product = productRequest.toDomain();
        final Long id = productService.createProduct(product);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable final Long id,
                                              @Valid @RequestBody final ProductRequest productRequest) {
        final Product product = productRequest.toDomain();
        productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
