package fyodor.validation;

import fyodor.model.Category;
import fyodor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryWithoutArticlesValidator implements ConstraintValidator<CategoryWithoutArticles, Long> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(CategoryWithoutArticles contactNumber) {
    }

    @Override
    public boolean isValid(Long categoryId, ConstraintValidatorContext cxt) {
        Category category = categoryService.findById(categoryId);

        return category.getArticles().size() == 0;
    }
}