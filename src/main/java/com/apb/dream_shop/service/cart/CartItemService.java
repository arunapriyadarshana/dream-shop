package com.apb.dream_shop.service.cart;

import com.apb.dream_shop.exception.ResourceNotFoundException;
import com.apb.dream_shop.modal.Cart;
import com.apb.dream_shop.modal.CartItem;
import com.apb.dream_shop.modal.Product;
import com.apb.dream_shop.repository.CartItemRepository;
import com.apb.dream_shop.repository.CartRepository;
import com.apb.dream_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;
    private final ICartService cartService;
    private final IProductService productService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
//        get the cart
//        get the product
//        check if product already in the cart
//        if yes, then increase the quantity with the requested quantity
//        if no, initiate new cartItem entry

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = cart.getItems().stream().filter(
                        item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(
                        new CartItem()
                );
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());

        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepo.save(cartItem);
        cartRepo.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);

        cart.removeItem(itemToRemove);
        cartRepo.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().filter(
                cartItem -> cartItem.getProduct().getId().equals(productId)
        ).findFirst().ifPresent(cartItem -> {
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(cartItem.getProduct().getPrice());
            cartItem.setTotalPrice();
        });

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepo.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream().filter(
                cartItem -> cartItem.getProduct().getId().equals(productId)
        ).findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
