package fyodor.service;

import fyodor.dto.CategoryDto;
import fyodor.model.Article;
import fyodor.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();

    Category findById(Long id);

    List<Category> findCategoriesAndSubcategoriesById(Long id);

    List<Category> getHierarchicalListOfUsedCategories();

    boolean categoryExists(String name);

    Category save(CategoryDto categoryDto);

    void delete(Long id);

    List<Category> getUsedParentsHierarchicalList();

    List<Category> getFullUsedHierarchicalList();

    Category getFullHierarchy();

    List<Category> getFullHierarchicalList();

    List<Category> getFullParentsHierarchicalList();

}
