package com.apb.dream_shop.repository;

import com.apb.dream_shop.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
