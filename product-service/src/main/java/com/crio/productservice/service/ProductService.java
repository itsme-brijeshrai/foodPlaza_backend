package com.crio.productservice.service;

import com.crio.productservice.dto.ProductRequest;
import com.crio.productservice.dto.ProductResponse;
import com.crio.productservice.model.Product;
import com.crio.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    private final ProductRepository productRepository;


    @Override
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public void updateProduct(String id, ProductResponse productResponse) {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Product> opt = productRepository.findById(String.valueOf(objectId));
            log.info("Searching product with ID: {}", id);
            if (opt.isPresent()) {
                Product existingProduct = opt.get();
                existingProduct.setName(productResponse.getName());
                existingProduct.setDescription(productResponse.getDescription());
                existingProduct.setPrice(productResponse.getPrice());
                productRepository.save(existingProduct);
                log.info("Product {} is updated", existingProduct.getId());
            } else {
                log.warn("Product with id {} not found for updating", objectId);
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid Object Id found");
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Products : {}",products);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    @Override
    public void deleteProduct(String id) {
        log.info("Searching product with ID: {}", id);
        ObjectId objectId = new ObjectId(id);
        Optional<Product> opt = productRepository.findById(String.valueOf(objectId));
        if (opt.isPresent()){
            Product product = opt.get();
            productRepository.delete(product);
            log.info("Product {} deleted",id);
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
