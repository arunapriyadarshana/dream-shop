package com.apb.dream_shop.service.cart;

import com.apb.dream_shop.exception.ResourceNotFoundException;
import com.apb.dream_shop.modal.Cart;
import com.apb.dream_shop.repository.CartItemRepository;
import com.apb.dream_shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart Not Found")
        );
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepo.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepo.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepo.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

//        return cart.getItems().stream()
//                .map(CartItem::getTotalPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return cart.getTotalAmount();
    }
}
