package fyodor.util;

import fyodor.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryHierarchyToListConverter {
    private List<Category> list = new ArrayList<>();

    public List<Category> convert(Category primaryCategory) {
        list.add(primaryCategory);

        for (Category subcategory: primaryCategory.getSubcategories()) {
            makeList(subcategory);
        }

        return list;
    }

    private void makeList(Category c) {
        list.add(c);
        for (Category subcategory: c.getSubcategories()) {
            makeList(subcategory);
        }
    }
}
