package fyodor.service;

import fyodor.model.*;
import fyodor.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService implements IArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    IUserService userService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IImageService imageService;

    @Override
    public void save(Article article, Principal author) {
        User user = userService.findByUsernameIgnoreCase(author.getName());
        article.setAuthor(user);
        article.setDate(new Date());

        articleRepository.save(article);
    }

    @Override
    public void save(ArticleDto articleDto, Principal author) {
        Article article = new Article();
        User user = userService.findByUsernameIgnoreCase(author.getName());
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setCategory(categoryService.findById(articleDto.getCategory()));
        article.setAuthor(user);
        article.setDate(new Date());

        String data = articleDto.getPicture();
//        String base64Image = data.split(",")[1];
//        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
//        Image image = imageService.save(imageBytes);
        Image image = imageService.save(data);
        article.setImage(image);

        articleRepository.save(article);
    }

    @Override
    public Article findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public List<Article> findByCategoryAndAuthor(Category category, User author) {
        return articleRepository.findByCategoryAndAuthor(category, author);
    }

    @Override
    public List<Article> findByAuthor(User author) {
        return articleRepository.findByAuthor(author);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
