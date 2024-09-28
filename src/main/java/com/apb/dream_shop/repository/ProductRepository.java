package com.apb.dream_shop.repository;

import com.apb.dream_shop.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
