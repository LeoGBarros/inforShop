package br.com.leonardo.InforShop.Service;

import br.com.leonardo.InforShop.Exception.NoProductsFoundException;
import br.com.leonardo.InforShop.Exception.ProductNotFoundException;
import br.com.leonardo.InforShop.Model.Product;
import br.com.leonardo.InforShop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoProductsFoundException();
        }
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        // Primeiro verifica se o produto existe antes de tentar deletar
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
