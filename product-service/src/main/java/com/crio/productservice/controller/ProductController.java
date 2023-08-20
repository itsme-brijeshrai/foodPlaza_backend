package com.crio.productservice.controller;

import com.crio.productservice.dto.ProductRequest;
import com.crio.productservice.dto.ProductResponse;
import com.crio.productservice.model.Product;
import com.crio.productservice.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final IProductService iProductService;


    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        iProductService.createProduct(productRequest);
        return new ResponseEntity<>("Product Created", HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody ProductResponse productResponse) {

        iProductService.updateProduct(id, productResponse);
        return new ResponseEntity<>("Product {" + productResponse.getName() + "} Updated", HttpStatus.ACCEPTED);
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return new ResponseEntity<>(iProductService.getAllProducts(), HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        iProductService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }
}
