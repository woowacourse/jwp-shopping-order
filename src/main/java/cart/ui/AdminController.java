package cart.ui;

import cart.dao.AdminCartDao;
import cart.domain.cart.CartItem;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/cart")
public class AdminController {
    private final AdminCartDao adminCartDao;

    public AdminController(AdminCartDao adminCartDao) {
        this.adminCartDao = adminCartDao;
    }

    @GetMapping
    public String adminCart(Model model) {
        List<CartItem> cartItems = adminCartDao.findAll();
        model.addAttribute("cartItems", cartItems);
        return "admin_cart";
    }

    @DeleteMapping("/{cartId}")
    @ResponseBody
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        adminCartDao.delete(cartId);
        return ResponseEntity.ok().build();
    }
}
