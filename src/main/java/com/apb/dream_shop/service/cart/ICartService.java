package com.apb.dream_shop.service.cart;

import com.apb.dream_shop.modal.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

}
