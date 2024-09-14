package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handleSaveProduct(Product tri) {
        Product eric = this.productRepository.save(tri);
        return eric;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }
}
