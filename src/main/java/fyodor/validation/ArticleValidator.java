package fyodor.validation;

import fyodor.dto.ArticleDto;
import fyodor.model.Article;
import fyodor.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ArticleValidator implements Validator {
    @Autowired
    private ArticleService articleService;

    @Override
    public boolean supports(Class<?> aClass) {
        return ArticleDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors err) {
        checkIfEmpty(err);
        if (err.hasErrors()) return;

        ArticleDto articleDto = (ArticleDto)obj;
        checkLength(articleDto, err);
        if (err.hasErrors()) return;

        checkDuplication(articleDto, err);
    }

    public void checkIfEmpty(Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "title", "article.form.error.title.length");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "content", "article.form.error.content.length");
    }

    public void checkLength(ArticleDto articleDto, Errors err) {
        int titleLength = articleDto.getTitle().length();
        if (titleLength < 4 || titleLength > 32)
            err.rejectValue("title", "article.form.error.title.length");

        int contentLength = articleDto.getContent().length();
        if (contentLength < 1000 || contentLength > 100000)
            err.rejectValue("content", "article.form.error.content.length");
    }

    public void checkDuplication(ArticleDto articleDto, Errors err) {
        Article article = articleService.findByTitleIgnoreCase(articleDto.getTitle());
        if (article != null)
            err.rejectValue("title", "article.form.error.title.duplicate");
    }
}
