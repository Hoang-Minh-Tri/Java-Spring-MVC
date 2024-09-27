package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService) {
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    public Product handleSaveProduct(Product tri) {
        Product eric = this.productRepository.save(tri);
        return eric;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Optional<Product> geProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);
                cart = this.cartRepository.save(otherCart);
            }
            Optional<Product> p = this.productRepository.findById(productId);
            if (p.isPresent()) {
                Product realProduct = p.get();
                CartDetail oldCartDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
                if (oldCartDetail == null) {
                    CartDetail cd = new CartDetail();
                    cd.setCart(cart);
                    cd.setProduct(realProduct);
                    cd.setQuantity(1);
                    cd.setPrice(realProduct.getPrice());
                    this.cartDetailRepository.save(cd);
                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    session.setAttribute("sum", s);
                    this.cartRepository.save(cart);
                } else {
                    oldCartDetail.setQuantity(oldCartDetail.getQuantity() + 1);
                    this.cartDetailRepository.save(oldCartDetail);
                }

            }
        }
    }

    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteAProductCart(long id) {
        this.cartDetailRepository.deleteById(id);
    }

    public void deleteCart(Cart cart) {
        this.cartRepository.delete(cart);
    }
}
