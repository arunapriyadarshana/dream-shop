package com.apb.dream_shop.service.product;

import com.apb.dream_shop.dto.ImageDTO;
import com.apb.dream_shop.dto.ProductDTO;
import com.apb.dream_shop.exception.ResourceNotFoundException;
import com.apb.dream_shop.modal.Category;
import com.apb.dream_shop.modal.Image;
import com.apb.dream_shop.modal.Product;
import com.apb.dream_shop.repository.CategoryRepository;
import com.apb.dream_shop.repository.ImageRepository;
import com.apb.dream_shop.repository.ProductRepository;
import com.apb.dream_shop.request.AddProductRequest;
import com.apb.dream_shop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepo;

    @Override
    public Product addProduct(AddProductRequest request) {
//        check if the category  is found in the DB
//        if yes, set it as the new product category
//        if no, the save it as a new category
//        then set as the new product category

        Category category = Optional.ofNullable(categoryRepo.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                            Category newCategory = new Category(request.getCategory().getName());
                            return categoryRepo.save(newCategory);
                        }
                );
        request.setCategory(category);
        return productRepo.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {

        return productRepo.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepo::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepo.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepo.findById(productId).ifPresentOrElse(productRepo::delete,
                () -> {
                    throw new ResourceNotFoundException("Product Not Found");
                }
        );
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepo.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepo.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepo.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepo.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepo.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageRepo.findByProductId(product.getId());
        List<ImageDTO> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDTO.class)).toList();
        productDTO.setImages(imageDtos);
        return productDTO;
    }

}

