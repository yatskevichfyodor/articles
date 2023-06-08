package fyodor.service;

import fyodor.dto.CategoryDto;
import fyodor.model.Category;
import fyodor.model.User;
import fyodor.repository.CategoryRepository;
import fyodor.util.CategoryHierarchyToListConverter;
import fyodor.util.HierarchicalCategoryHierarchyToListConverter;
import fyodor.util.UsedCategoriesHierarchyBuilder;
import fyodor.util.UserCategoriesHierarchyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired private CategoryService categoryService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    public List<Category> findCategoriesAndSubcategoriesById(Long id) {
        Category primaryCategory = categoryRepository.findById(id).get();
        Category subhierarchy = usedCategoriesHierarchyBuilder.getSubhierarchy(primaryCategory);
        CategoryHierarchyToListConverter categoryHierarchyToListConverter = new CategoryHierarchyToListConverter();
        List<Category> categories = categoryHierarchyToListConverter.convert(subhierarchy);
        return categories;
    }

    public List<Category> getHierarchicalListOfUsedCategories() {
        List<Category> hierarchicalList = new HierarchicalCategoryHierarchyToListConverter()
                .convert(usedCategoriesHierarchyBuilder.getHierarchy());
        return hierarchicalList;
    }

    public List<Category> getHierarchicalListOfUserCategories(User user) {
        UserCategoriesHierarchyBuilder userCategoriesHierarchyBuilder = new UserCategoriesHierarchyBuilder();
        userCategoriesHierarchyBuilder.setCategoryService(categoryService);
        userCategoriesHierarchyBuilder.build(user);
        List<Category> hierarchicalList = new HierarchicalCategoryHierarchyToListConverter()
                .convert(userCategoriesHierarchyBuilder.getHierarchy());
        return hierarchicalList;
    }

    private List<CategoryDto> convertToDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category: categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());

            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }

    public boolean categoryExists(String name) {
        if (categoryRepository.findByName(name) == null)
            return false;
        return true;
    }

    public Category save(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        if (categoryDto.getParentId() != 0)
            category.setParentCategory(findById(categoryDto.getParentId()));
        return categoryRepository.save(category);
    }

    public List<Category> getUsedParentsHierarchicalList() {
        Category categoryHierarchy = usedCategoriesHierarchyBuilder.getHierarchy();
        List<Category> categoryList = new HierarchicalCategoryHierarchyToListConverter().convert(categoryHierarchy, 4);
        return categoryList;
    }

    public List<Category> getFullUsedHierarchicalList() {
        Category categoryHierarchy = usedCategoriesHierarchyBuilder.getHierarchy();
        List<Category> categoryList = new HierarchicalCategoryHierarchyToListConverter().convert(categoryHierarchy);
        return categoryList;
    }

    public Category getFullHierarchy() {
        Category hierarchy = new Category();
        List<Category> allCategories = findAll();
        Set<Category> rootHierarchies = new HashSet<>();
        for (Category category: allCategories) {
            if (category.getParentCategory() == null) {
                rootHierarchies.add(category);
            }
        }
        hierarchy.setSubcategories(rootHierarchies);

        return hierarchy;
    }

    public List<Category> getFullHierarchicalList() {
        Category hierarchy = getFullHierarchy();
        return new HierarchicalCategoryHierarchyToListConverter().convert(hierarchy);
    }

    public List<Category> getFullParentsHierarchicalList() {
        Category hierarchy = getFullHierarchy();
        return new HierarchicalCategoryHierarchyToListConverter().convert(hierarchy, 4);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(categoryRepository.findById(id).get());
    }

    public List<Category> findUsedCategoriesByUser(User user) {
        return categoryRepository.findUsedCategoriesByUserId(user.getId());
    }
}
