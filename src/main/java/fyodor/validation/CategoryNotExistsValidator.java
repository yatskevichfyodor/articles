package fyodor.validation;

import fyodor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryNotExistsValidator implements ConstraintValidator<CategoryNotExists, String> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(CategoryNotExists contactNumber) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
        return !categoryService.categoryExists(name);
    }
}