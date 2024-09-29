package com.apb.dream_shop.service.product;

import com.apb.dream_shop.modal.Product;
import com.apb.dream_shop.request.AddProductRequest;
import com.apb.dream_shop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);

    Product getProductById(Long productId);

    Product updateProduct(UpdateProductRequest request, Long productId);

    void deleteProduct(Long productId);

    List<Product> getAllProduct();

    List<Product> getProductByCategory(String category);

    List<Product> getProductByBrand(String brand);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    List<Product> getProductByName(String name);

    List<Product> getProductByBrandAndName(String brand, String name);

    Long countProductByBrandAndName(String brand, String name);

}
