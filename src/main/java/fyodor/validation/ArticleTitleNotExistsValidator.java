package fyodor.validation;

import fyodor.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArticleTitleNotExistsValidator implements ConstraintValidator<ArticleTitleNotExists, String> {

    @Autowired
    private ArticleService articleService;

    @Override
    public void initialize(ArticleTitleNotExists contactNumber) {
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext cxt) {
        return articleService.findByTitleIgnoreCase(title) == null;
    }
}