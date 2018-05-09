package fyodor.service;

import fyodor.dto.CategoryDto;
import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.repository.CategoryRepository;
import fyodor.util.CategoryHierarchyToListConverter;
import fyodor.util.HierarchicalCategoryHierarchyToListConverter;
import fyodor.util.UsedCategoriesHierarchyBuilder;
import org.hibernate.jpa.event.internal.jpa.ListenerCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.cldr.de.CalendarData_de_AT;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findCategoriesAndSubcategoriesById(Long id) {
        Category primaryCategory = categoryRepository.findById(id);
        Category subhierarchy = usedCategoriesHierarchyBuilder.getSubhierarchy(primaryCategory);
        CategoryHierarchyToListConverter categoryHierarchyToListConverter = new CategoryHierarchyToListConverter();
        List<Category> categories = categoryHierarchyToListConverter.convert(subhierarchy);
        return categories;
    }

    @Override
    public List<Category> getHierarchicalListOfUsedCategories() {
        List<Category> hierarchicalList = new HierarchicalCategoryHierarchyToListConverter()
                .convert(usedCategoriesHierarchyBuilder.getHierarchy());
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

    @Override
    public boolean categoryExists(String name) {
        if (categoryRepository.findByName(name) == null)
            return false;
        return true;
    }

    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setParentCategory(findById(categoryDto.getParentId()));
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getUsedParentsHierarchicalList() {
        Category categoryHierarchy = usedCategoriesHierarchyBuilder.getHierarchy();
        List<Category> categoryList = new HierarchicalCategoryHierarchyToListConverter().convert(categoryHierarchy, 4);
        return categoryList;
    }

    @Override
    public List<Category> getFullUsedHierarchicalList() {
        Category categoryHierarchy = usedCategoriesHierarchyBuilder.getHierarchy();
        List<Category> categoryList = new HierarchicalCategoryHierarchyToListConverter().convert(categoryHierarchy);
        return categoryList;
    }

    @Override
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

    @Override
    public List<Category> getFullHierarchicalList() {
        Category hierarchy = getFullHierarchy();
        return new HierarchicalCategoryHierarchyToListConverter().convert(hierarchy);
    }

    @Override
    public List<Category> getFullParentsHierarchicalList() {
        Category hierarchy = getFullHierarchy();
        return new HierarchicalCategoryHierarchyToListConverter().convert(hierarchy, 4);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        categoryRepository.delete(id);
    }
}
