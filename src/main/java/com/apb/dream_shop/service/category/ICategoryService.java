package com.apb.dream_shop.service.category;

import com.apb.dream_shop.modal.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategory();

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategoryById(Long id);

}
