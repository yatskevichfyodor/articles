package fyodor.util;

import fyodor.model.Category;
import fyodor.model.User;
import fyodor.service.ICategoryService;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserCategoriesHierarchyBuilder {
    private ICategoryService categoryService;

    private final Category hierarchy = new Category();
    private Set<Category> usedCategories;
    private Set<Category> usedCategoriesAndParentsSet;// used on building stage
    private List<Category> usedCategoriesAndParentsList = new ArrayList<>(); // used after building stage

    public void build(User user) {
        usedCategories = new HashSet<>(categoryService.findUsedCategoriesByUser(user));
        usedCategoriesAndParentsSet = new HashSet<>(usedCategories);
        for (Category category : usedCategories) {
            insertParentCategories(category);
        }

        Set<Category> rootCategories = new HashSet<>();
        for (Category category : usedCategoriesAndParentsSet) {
            if (category.getParentCategory() == null)
                rootCategories.add(category);
        }

        hierarchy.setSubcategories(rootCategories);

        for (Category rootCategory: hierarchy.getSubcategories()) {
            rootCategory.setParentCategory(hierarchy);
        }

        usedCategoriesAndParentsList = new ArrayList<>();
        usedCategoriesAndParentsList.addAll(rootCategories);

        for (Category category : hierarchy.getSubcategories()) {
            filterSubcategories(category);
        }
    }

    private void insertParentCategories(Category c) {
        Category parentCategory = c.getParentCategory();
        if (c.getParentCategory() != null) {
            usedCategoriesAndParentsSet.add(parentCategory);
            insertParentCategories(parentCategory);
        }
    }

    private void filterSubcategories(Category primaryCategory) {
        Set<Category> subcategories = primaryCategory.getSubcategories();
        Set<Category> notUsedSubcategories = new HashSet<>();
        for (Category subcategory : subcategories) {
            // don't replace the code below in method, because it uses usedCategoriesAndParentsSet - not List
            // so it's not usable at runtime
            boolean isAmongUsedCategoriesAndParents = false;
            for (Category usedCategoryOrParent : usedCategoriesAndParentsSet) {
                if (usedCategoryOrParent.equals(subcategory)) {
                    isAmongUsedCategoriesAndParents = true;
                    break;
                }
            }
            if (!isAmongUsedCategoriesAndParents) {
                notUsedSubcategories.add(subcategory);
            }
        }
        subcategories.removeAll(notUsedSubcategories);

        usedCategoriesAndParentsList.addAll(subcategories);

        for (Category subcategory : subcategories) {
            filterSubcategories(subcategory);
        }
    }

    public Category getSubhierarchy(Category primaryCategory) {
        for (Category subcategory : usedCategoriesAndParentsList) {
            if (subcategory.equals(primaryCategory))
                return subcategory;
        }

        throw new RuntimeException("Subhierarchy wasn't found");
    }
}