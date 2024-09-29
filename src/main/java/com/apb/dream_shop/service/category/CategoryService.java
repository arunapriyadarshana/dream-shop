package com.apb.dream_shop.service.category;

import com.apb.dream_shop.exception.AlreadyExistsException;
import com.apb.dream_shop.exception.ResourceNotFoundException;
import com.apb.dream_shop.modal.Category;
import com.apb.dream_shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepo;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found!")
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepo.findByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(
                c -> !categoryRepo.existsByName(c.getName())
        ).map(
                categoryRepo::save
        ).orElseThrow(
                () -> new AlreadyExistsException(category.getName() + " already exists")
        );
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepo.save(oldCategory);
        }).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found!")
        );

    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepo.findById(id).ifPresentOrElse(
                categoryRepo::delete,
                () -> {
                    throw new ResourceNotFoundException("Category Not Found!");
                }
        );
    }
}
