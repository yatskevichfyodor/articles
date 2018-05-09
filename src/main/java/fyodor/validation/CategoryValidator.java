package fyodor.validation;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.service.ICategoryService;
import fyodor.service.IUserAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CategoryValidator {

    @Autowired
    private ICategoryService categoryService;

    public Set<Integer> validateAdd(String categoryName) {
        Set<Integer> errorCodesSet = new HashSet<>();
        if (!validNameLength(categoryName)) {
            errorCodesSet.add(1);
            return errorCodesSet;
        }
        if (attributeExists(categoryName)) {
            errorCodesSet.add(2);
        }
        return  errorCodesSet;
    }

    private boolean validNameLength(String name) {
        int length = name.length();
        return (length >= 3 && length <= 20);
    }

    private boolean attributeExists(String name) {
        return categoryService.categoryExists(name);
    }

    public Set<Integer> validateDelete(Long categoryId) {
        Category category = categoryService.findById(categoryId);

        Set<Integer> errorCodesSet = new HashSet<>();

        if (categoryHasSubcategories(category)) {
            errorCodesSet.add(1);
        }

        if (categoryHasArticles(category)) {
            errorCodesSet.add(3);
        }

        return  errorCodesSet;
    }

    private boolean categoryHasSubcategories(Category category) {
        return category.getSubcategories().size() != 0;
    }

    private boolean categoryHasArticles(Category category) {
        return category.getArticles().size() != 0;
    }
}
