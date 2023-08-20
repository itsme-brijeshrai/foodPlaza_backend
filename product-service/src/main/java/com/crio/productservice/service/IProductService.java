package com.crio.productservice.service;

import com.crio.productservice.dto.ProductRequest;
import com.crio.productservice.dto.ProductResponse;

import java.util.List;

public interface IProductService {

    void createProduct(ProductRequest productRequest);

    void updateProduct(String id,ProductResponse productResponse);
    List<ProductResponse> getAllProducts ();

    void deleteProduct(String id);
}
