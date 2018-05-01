package fyodor.util;

import fyodor.model.Category;

import java.util.LinkedList;
import java.util.List;

public class HierarchicalCategoryHierarchyToListConverter {
    private List<Category> list = new LinkedList<>();

    public List<Category> convert(Category rootCategory) {
        for (Category subcategory: rootCategory.getSubcategories()) {
            makeList(subcategory, 0);
        }

        return list;
    }

    private void makeList(Category c, int nestingLevel) {
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            newName.append("-");
        }
        newName.append(" ");
        newName.append(c.getName());

        Category category = new Category();
        category.setId(c.getId());
        category.setName(newName.toString());

        list.add(category);

        for (Category subcategory: c.getSubcategories()) {
            makeList(subcategory, nestingLevel + 1);
        }
    }
}
